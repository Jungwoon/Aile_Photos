package com.aile.photos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class FullScreenViewActivity extends Activity {

//	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

        Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();

		viewPager = (ViewPager)findViewById(R.id.pager);

		Intent i = getIntent();
        String[] img_date = i.getStringArrayExtra("IMG_LIST");

        // for Test
        for(int j = 0; j < img_date.length; j++) {
            Log.e("TEST", "img[" + j + "] : " + img_date[j]);
        }

        // Convert String Array to ListArray
        ArrayList<String> images = new ArrayList(Arrays.asList(img_date));
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, images);

		viewPager.setAdapter(adapter);

		// displaying selected image first
//		viewPager.setCurrentItem(position);
	}
}
