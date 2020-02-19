package com.example.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    //这个就是html的内容，视频素材来源于网络，如果过期请自行找一个新的覆盖掉src部分
    String htmlContent = "<p><video poster=\"\" controls=\"\" src=\"https://vdept.bdstatic.com/6a76443370547264754e447375354866/557953624c694146/06c2cb119cba52312b4305888e3b6be1eda7f96c061bcd96594249aed27eb55ed711e516a02f0873ef61a764a119010b1966762b4f6bee404eee394b64c6052b.mp4?auth_key=1582116068-0-0-745325d8a2e24397d5ec2299a13ba182\" width=\"100%\" height=\"300px\" class=\"note-video-clip\" preload=\"auto\"></video></p><p>测试视频测试视频测试视频</p>";

    //注意，在import的时候，要使用com.tencent.smtt.sdk.WebView
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.x5webview);

        requestPermission();

        initWebView();
    }

    private void initWebView() {

        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //true表示成功加载x5内核，false表示加载失败，使用内置的webview
                Log.i(TAG, "onViewInitFinished: " + b);
            }
        });

        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 获取网络设置
        WebSettings webSettings = webView.getSettings();

        //启用应用缓存
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        //开启DOM缓存，qq
        webSettings.setDomStorageEnabled(true);
        //适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        //这个一定要设置为false
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        if (webView.getX5WebViewExtension() != null){ //视频全屏的相关配置
            Toast.makeText(this, "如果弹出该Toast就能全屏", Toast.LENGTH_SHORT).show();
            //没有弹出的情况下全屏按钮是灰色的不可点击

            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }

        //加载网页
        webView.loadDataWithBaseURL("", htmlContent, "text/html", "UTF-8", "");
    }



    //踩坑，申请读写和网络
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSON = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET
    };


    private void requestPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSON, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
