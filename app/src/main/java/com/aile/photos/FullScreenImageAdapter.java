package com.aile.photos;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by JW on 16. 2. 28..
 */
public class FullScreenImageAdapter extends PagerAdapter {
    private static final String LOG_TAG1 = "FullScreenImageAdapter";
    private static final String LOG_TAG2 = Common.LOG_TAG_STRING;

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        /**
         * Glide 사용법
         * ex) Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
         */
        Glide.with(imgDisplay.getContext()).load(_imagePaths.get(position)).into(imgDisplay);

        ((ViewPager)container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
