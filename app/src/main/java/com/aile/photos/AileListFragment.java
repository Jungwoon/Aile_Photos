package com.aile.photos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// 아래쪽에 리스트 보여주는 부분
public class AileListFragment extends Fragment {
    final static String TAG = "AileListFragment";

    // 맨 처음 뷰를 생성하는 부분
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");

        Log.e(TAG, "num : " + getArguments().getInt("num"));
        Log.e(TAG, "date : " + getArguments().getString("date"));

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_aile_list, container, false);
        setupRecyclerView(rv);

        // MainActivity의 fragment에 반환한다.
        return rv;
    }


    // Date를 받아서 해당 날짜에 해당하는 ArrayList를 받는다
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        Log.e(TAG, "getDate from Bundle : " + getArguments().getString("date"));

        /**
         * Bundle로 넣어준 date를 가져와서
         * 해당 날짜에 해당하는 이미지 리스트를 가져온다
         * 실제 리스트로부터 받은 이미지를 넣어주는 부분
         */

        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(getActivity(), loadImages(getArguments().getString("date"))));
    }

    public static class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public SimpleRecyclerViewAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;
            public final View mView;
            public final ImageView mImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar); // 사진 들어가는 부분
            }
        }

        public String getValueAt(int position) {
            Log.e(TAG, "getValueAt : " + mValues.get(position));
            return mValues.get(position);
        }

        // 사진을 보여줄 곳
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // list_item 이 사진 보여주는 곳
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        // 각 이미지를 눌렀을때 넘어가는 부분
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);

            // 각 리스트 버튼을 눌렀을때 안쪽으로 넘어가는 부분
            // 인텐트를 써서
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AileDetailActivity.class);
                    intent.putExtra(AileDetailActivity.EXTRA_NAME, holder.mBoundString);
                    context.startActivity(intent);
                }
            });

            /**
             * 사진 이미지 넣어주는 부분
             * Cheeses.getRandomCheeseDrawable() -> getRandomCheeseDrawable(날짜) 이런식으로
             *
             * Glide 사용법
             * ex) Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
             */

            Glide.with(holder.mImageView.getContext()).load(mValues.get(position)).fitCenter().into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        Log.e(TAG, "onDestroyView()");
    }

    // 여행시작날짜와 종료날짜를 보내면 거기에 해당하는 사진을 조회해서 넘기는 부분
    public ArrayList loadImages(String date) {
        ArrayList<String> list = new ArrayList<>();
        DBHelper mHelper = new DBHelper(getContext());
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Log.e(TAG, "loadImages() date : " + date);
        String query = String.format("SELECT image_path FROM %s WHERE date = '%s';", Common.IMAGE_TABLE, date);

        // 쿼리를 실행하고 거기에 대한 결과를 cursor에 넣음
        Cursor cursor = db.rawQuery(query, null);

        // 해당하는 걸 가져오는 부분
        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
            Log.e(TAG, "cursor : " + cursor.getString(0));
        }

        //다 썼으니 닫아줌
        cursor.close();
        mHelper.close();

        return list;
    }
}