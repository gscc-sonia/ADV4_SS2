package com.gscc.adv4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.gscc.adv4.UpdateManager;

public class MainActivity extends Activity {
    //@@@@@@全區宣告start
    VideoView vv;
    TextView tx1, tx2;
    ImageView mv;
    int i;//計時器用count
    Thread countToloop;
    private Button btnT1, btnT2;//測試按鈕
    //private UpdateManager mUpdateUpdate;
    //String newVerName = "";//新版本名稱
    //int newVerCode = -1;//新版本號
    private UpdateManager mUpdateManager;
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

        //@@@@@檢查是否需要更新
        mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdate();
       /*if(getServerVer()){
            int verCode = this.getVerCode(this);
            if(newVerCode>verCode){
                mUpdateUpdate = new UpdateManager(this);
                mUpdateUpdate.checkUpdateInfo();
            }else{

            }
        }*/

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