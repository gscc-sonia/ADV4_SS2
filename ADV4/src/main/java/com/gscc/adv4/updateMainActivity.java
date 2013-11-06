package com.gscc.adv4;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sonia on 2013/11/6.
 * 備用的自動更新Activity(尚未使用)
 */
        public class updateMainActivity  extends Activity {
            String newVerName = "";//新版本名稱
            int newVerCode = -1;//新版本號
            ProgressDialog pd = null;
            String UPDATE_SERVERAPK = "ADV4_SS2.apk";

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //@@@@@全畫面
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //@@@@@


        //@@@@@取得版本資訊，決定是否更新
        if (getServerVer()) {
            int verCode = this.getVerCode(this);
            if (newVerCode > verCode) {
                doNewVersionUpdate();//更新版本
            } else {
                notNewVersionUpdate();
            }
        }
        //@@@@@
    }

    //@@@@@獲得版本號
    public int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.gscc.adv4", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本號獲取異常", e.getMessage());
        }
        return verCode;
    }
    //@@@@@

    //@@@@@獲得版本名稱
    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.gscc.adv4", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本名稱獲取異常", e.getMessage());
        }
        return verName;
    }
    //@@@@@

    //@@@@@從伺服器端獲得版本號與版本名稱
    public boolean getServerVer() {
        try {
            URL url = new URL("http://apkupdate.gscc.net.tw/ADV4/ADV4_SS2.apk");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            InputStreamReader reader = new InputStreamReader(httpConnection.getInputStream());
            BufferedReader bReader = new BufferedReader(reader);
            String json = bReader.readLine();
            JSONArray array = new JSONArray(json);
            JSONObject jsonObject = array.getJSONObject(0);
            newVerCode = Integer.parseInt(jsonObject.getString("verCode"));
            newVerName = jsonObject.getString("verName");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //@@@@@

    //@@@@@不更新版本
    public void notNewVersionUpdate() {
        int verCode = this.getVerCode(this);
        String verName = this.getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("目前版本:");
        sb.append(verName);
        sb.append("Code:");
        sb.append(verCode);
        sb.append("\n已是最新版本，無需更新");
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Apk更新")
                .setMessage(sb.toString())
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }
    //@@@@@

    //@@@@@更新版本
    public void doNewVersionUpdate() {
        int verCode = this.getVerCode(this);
        String verName = this.getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("目前版本:");
        sb.append(verName);
        sb.append("Code:");
        sb.append(verCode);
        sb.append(",發現新版本:");
        sb.append(newVerName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(",是否更新");
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Apk更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        pd = new ProgressDialog(updateMainActivity.this);
                        pd.setTitle("正在下載");
                        pd.setMessage("請稍後‧‧‧");
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        downFile("http://apkupdate.gscc.net.tw/ADV4/ADV4_SS2.apk");
                    }
                })
                .setNegativeButton("暫不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }
    //@@@@@

    //@@@@@下載APK
    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient clinet = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = clinet.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        int count = 0;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
    public void handleMessage(Message msg){
            super.handleMessage(msg);
            pd.cancel();
            update();
        }
    };

    //@@@@@下載完成，通過Handler將下載對話取消
    public void down(){
        new Thread(){
            public void run(){
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();
    }
    //@@@@@

    //@@@@@安裝APK
    public void update(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),UPDATE_SERVERAPK))
                ,"application/vnd.android.package-archive");
        startActivity(intent);
    }
    //@@@@@
}
