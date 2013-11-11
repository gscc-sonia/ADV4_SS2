package com.gscc.adv4;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {
    //下載中
    private static final int DOWNLOAD = 1;
    //下載完成
    private static final int DOWNLOAD_FINISH = 2;
    //保存解析的XML信息
    HashMap<String, String> mHashMap;
    //下載保存路徑
    private String mSavePath;
    //紀錄進度條數量
    private int progress;
    //是否取消更新
    private boolean cancelUpdate = false;

    private Context mContext;
    //更新進度條
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
//正在下載
                case DOWNLOAD:
//設置進度條位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
//安裝文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 檢測APK更新
     */
    public void checkUpdate() {
        if (isUpdate()) {
//顯示提示對話框
            showNoticeDialog();
        } else {
            Toast.makeText(mContext,R.string.update_no, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 檢查APK是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
//獲取目前APK的版本
        int versionCode = getVersionCode(mContext);
// 把version.xml放到網路上，然後獲取文件信息
        InputStream inStream = ParseXmlService.class.getClassLoader().getResourceAsStream("http://apkupdate.gscc.net.tw/ADV4/version.xml");
// 解析XML文件。 由於XML文件比較小，因此使用DOM方式進行解析
        ParseXmlService service = new ParseXmlService();
        try {
            mHashMap = service.parseXml(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mHashMap) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
//版本判斷
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 獲取APK版本號
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
//獲取軟件版本號，對應AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.szy.update", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 顯示APK更新對話框
     */
    private void showNoticeDialog() {
// 構造對話框构造对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.update_title);
        builder.setMessage(R.string.update_info);
// 更新
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//顯示下載對話框
                showDownloadDialog();
            }
        });
//稍後更新
        builder.setNegativeButton("下次再說", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 顯示APK下載對話框
     */
    private void showDownloadDialog() {
//構造APK下載對話框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle("正在更新");
//給下載對話框增加進度條
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
//取消更新
      builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//設置取消狀態
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
//現在文件
        downloadApk();
    }

    /**
     * 下載apk文件
     */
    private void downloadApk() {
//啟動新線程下載APK
        new downloadApkThread().start();
    }

    /**
     * 下載文件線程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
//判斷SD卡是否存在，並且是否具有讀寫權限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//獲得SD卡的路徑
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(mHashMap.get("url"));
//建立連接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
//獲取文件大小
                    int length = conn.getContentLength();
//建立輸入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
//判斷文件目錄是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mHashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
//緩衝
                    byte buf[] = new byte[1024];
//寫入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
//計算進度位置
                        progress = (int) (((float) count / length) * 100);
//更新進度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
//下載完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
//寫入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);//點擊就取消下載.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//取消下載對話框顯示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安裝APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, mHashMap.get("name"));
        if (!apkfile.exists()) {
            return;
        }
//通過Intent安裝APK
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
