package com.aile.photos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;

public class FullScreenViewActivity extends Activity {
    private static final String LOG_TAG1 = "FullScreenViewActivity";
    private static final String LOG_TAG2 = Common.LOG_TAG_STRING;

//	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager)findViewById(R.id.pager);

		Intent i = getIntent();
        String[] img_date = i.getStringArrayExtra("IMG_LIST");
        int img_position = i.getIntExtra("IMG_POSITION", 0);

        Logger.e(LOG_TAG1, LOG_TAG2, "fullScreen img_position : " + img_position);

        // for Test
        for(int j = 0; j < img_date.length; j++) {
            Logger.e(LOG_TAG1, LOG_TAG2, "img[" + j + "] : " + img_date[j]);
        }

        // Convert String Array to ListArray
        ArrayList<String> images = new ArrayList(Arrays.asList(img_date));
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, images);

		viewPager.setAdapter(adapter);

		// 여기서 index를 넘기면 viewPager 의 instantiateItem()의 position으로 받음
		viewPager.setCurrentItem(img_position);
	}
}
