package com.aile.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AileDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "aile_name";
    String image_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        image_path = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(cheeseName);
        collapsingToolbar.setTitle("Aile");

        loadBackdrop();
    }

    // Option 버튼을 눌렀을때의 경우
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    // 버튼을 누르면 상단에 뜨는 이미지
    private void loadBackdrop() {
        final ImageView imageView = (ImageView)findViewById(R.id.backdrop);
        Glide.with(this).load(image_path).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
