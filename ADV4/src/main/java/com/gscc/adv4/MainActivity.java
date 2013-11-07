package com.gscc.adv4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;


public class MainActivity extends Activity {
    //@@@@@@全區宣告start
    VideoView vv;
    TextView tx1, tx2;
    ImageView mv;
    int i;//計時器用count
    Thread countToloop;
    private Button btnT1, btnT2;//測試按鈕
    private UpdateManager mUpdateUpdate;
    String newVerName = "";//新版本名稱
    int newVerCode = -1;//新版本號
    //@@@@@全區宣告end

    //@@@@@接收Message，當接到特定Message時，執行特定動作
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int a = msg.getData().getInt("count", 0);

            if (a == 1) {

                VideoPlayer1();
                //tx1.setText("影片1");
            }
            if (a == 61) {
                VideoPlayer2();
                //marquee.setText("影片2");
            }
            if (a == 121) {
                //marquee.setText("影片3");
                VideoPlayer3();
            }
            if (a == 181) {
                //marquee.setText("影片4");
                VideoPlayer4();
            }
            if (a == 241) {
                //marquee.setText("影片5");
                VideoPlayer5();
            }
        }
    };
    //@@@@@

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //@@@@@全畫面
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //@@@@@
        //notNewVersionUpdate();
        //@@@@@檢查是否需要更新
        if(getServerVer()){
            int verCode = this.getVerCode(this);
            if(newVerCode>verCode){
                mUpdateUpdate = new UpdateManager(this);
                mUpdateUpdate.checkUpdateInfo();
            }else{

            }
        }
        //@@@@@

        btnT1 = (Button) findViewById(R.id.btn_test1);
        btnT2 = (Button) findViewById(R.id.btn_test2);

        //@@@@@開啟撥放器控制項
        //MediaController mediaController = new MediaController(this);
        //mediaController.setAnchorView(vv);
        //@@@@@

        tx2 = (TextView) findViewById((R.id.textView2));

        countToloop = new countToloop();
        countToloop.start();

    }

    //@@@@@不更新版本
    public void notNewVersionUpdate() {
        int verCode = this.getVerCode(this);
        //String verName = this.getVerName(this);
        StringBuffer sb = new StringBuffer();
        //sb.append("目前版本:");
        //sb.append(verName);
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

    //@@@@@或得版本號
    public int getVerCode(Context context){
        int verCode = -1;
        try{
            verCode = context.getPackageManager().getPackageInfo("com.gscc.adv4",0).versionCode;
        }catch (PackageManager.NameNotFoundException e){
            Log.e("版本號獲取異常", e.getMessage());
        }
        return verCode;
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

    //@@@@@測試按鈕的功能
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void BtnT1onClick(View view) {
        //VideoPlayer();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void BtnT2onClick(View view) {
        //VideoPlayer2();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }
    //@@@@@


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void VideoPlayer1() {
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Uri video1 = Uri.parse("sdcard/test.m4v");
        vv = (VideoView) findViewById(R.id.videoView1);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setText("Video1");
        tx1.setSelected(true);
        vv.setVideoURI(video1);
        vv.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void VideoPlayer2() {
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Uri video1 = Uri.parse("sdcard/test2.mp4");
        vv = (VideoView) findViewById(R.id.videoView1);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setText("Video2");
        vv.setVideoURI(video1);
        vv.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void VideoPlayer3() {
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Uri video1 = Uri.parse("sdcard/test3.mp4");
        vv = (VideoView) findViewById(R.id.videoView1);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setText("Video3");
        vv.setVideoURI(video1);
        vv.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void VideoPlayer4() {
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Uri video1 = Uri.parse("sdcard/test4.mp4");
        vv = (VideoView) findViewById(R.id.videoView1);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setText("Video4");
        vv.setVideoURI(video1);
        vv.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void VideoPlayer5() {
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Uri video1 = Uri.parse("sdcard/test5.mp4");
        vv = (VideoView) findViewById(R.id.videoView1);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setText("Video5");
        vv.setVideoURI(video1);
        vv.start();
    }

    public void ImagePlayer() {
        setContentView(R.layout.main2);
        Uri image1 = Uri.parse("sdcard/test1.jpg");
        mv = (ImageView) findViewById(R.id.imageView);
        mv.setImageURI(image1);
        //Bitmap bitmap = BitmapFactory.decodeFile("sdcard/test1.jpg");
    }

    protected void onPause() {
        super.onPause();
        if (countToloop != null) {
            countToloop.interrupt();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class countToloop extends Thread {
        @Override
        public void run() {
            super.run();
            goPlay();
        }

        public void goPlay() {
            try {
                while (true) {
                    for (i = 0; i < 300; i++) {
                        Thread.sleep(1000);
                        Bundle countBundle = new Bundle();
                        countBundle.putInt("count", i + 1);

                        Message msg = new Message();
                        msg.setData(countBundle);

                        mHandler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}