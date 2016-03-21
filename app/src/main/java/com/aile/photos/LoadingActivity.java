package com.aile.photos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {
    private static final String LOG_TAG1 = "LoadingActivity";
    private static final String LOG_TAG2 = Common.LOG_TAG_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 2000); // 3초 후에 hd Handler 실행
    }

    private class splashHandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoadingActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }
}