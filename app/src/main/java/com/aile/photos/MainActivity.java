package com.aile.photos;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;
    SharedPreferences prefsDefault;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefsDefault = PreferenceManager.getDefaultSharedPreferences(this);

        // 만약 설치하고 맨 처음이라면 사진목록을 업데이트 시켜준다.
        if(!prefsDefault.getBoolean(Common.PREF_INIT, false)) {
            updateImages();
        }

        // 선언해주는 부분
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        listView = (ListView)findViewById(R.id.nav_list); // Navigation ListView

        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_menu); // 좌측 상단에 메뉴 아이콘
        ab.setDisplayHomeAsUpEnabled(true);

        ImageView refresh = (ImageView)findViewById(R.id.refresh);

        /**
         * Refresh 버튼을 눌렀을때 Image Table에 사진 업데이트 해주는 부분
         * 날짜가 있는 그림만 테이블에 Insert 한다.
         */
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImages();

                // 로딩 다이얼로그 보여주는 부분
                LoadingImageProgress task = new LoadingImageProgress();
                task.execute();
            }
        });

        // + 버튼 누르면 여행 추가하는 부분
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AddTravel.class);
                i.putExtra("status","new");
                startActivity(i);
            }
        });
    }

    // Setting 해주는 부분을 이쪽으로 옮겨야 함
    public void onResume() {
        super.onResume();
        setupDrawerContent(); // Drawer 추가하는 부분

        // 마지막에 열었던 여행의 리스트를 열어주는 부분
        String selectedDest = prefsDefault.getString(Common.PREF_LATEST, "");
        setupViewPager(viewPager, selectedDest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param viewPager
     * 상단에 탭을 스와이프로 넘기는 부분
     * 시작일과 종료일을 가져와서 그 차이만큼 날짜를 구해서 생성해줌
     */

    private void setupViewPager(ViewPager viewPager, String destination) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Adapter adapter = new Adapter(getSupportFragmentManager());
        // adapter의 갱신을 위한 부분
//        adapter.notifyDataSetChanged();

        String startDate = ""; // 여행의 시작 날짜
        String endDate = ""; // 여행의 종료 날짜

        DBHelper mHelper = new DBHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;
        String query = "select start_date, end_date from " + Common.TRAVEL_TABLE + " where dest = '" + destination + "'";

        Log.e(TAG, "test query : " + query);

        // 쿼리를 실행하고 거기에 대한 결과를 cursor에 넣음
        cursor = db.rawQuery(query, null);

        // SQLite에 값이 비어있는지 아닌지 확인하고 들어가야 한다
        if(cursor != null && cursor.getCount() != 0 ) {

            // 맨 처음 한번 이동하고 시작해야한다
            cursor.moveToFirst();

            // DB로부터 읽어온 여행의 시작날짜와 종료날짜를 변수에 넣어준다.
            startDate = cursor.getString(0); // start_date
            endDate = cursor.getString(1); // end_date

            Log.e(TAG, "db startDate : " + startDate);
            Log.e(TAG, "db endDate : " + endDate);

            //커서를 다 썼으니 닫아주는 부분
            cursor.close();
            mHelper.close();
        }

        /**
         * 설정한 날짜(시작날짜 & 종료 날짜)가 있을때만
         * fragment에 추가로 보여준다
         */
        if(!startDate.equals("") && !endDate.equals("")) {
            Calendar cal = Calendar.getInstance();

            try {
                // 시작날짜와 종료날짜의 간격
                int count = getInterval(startDate, endDate);
                cal.setTimeInMillis(dateFormat.parse(startDate).getTime());

                // 아래에서 1일씩 증가시키기 때문에 애초에 하나 줄이고 시작
                cal.add(Calendar.DATE, -1);

                Log.e(TAG, "Interval : " + count);

                for (int i=0; i<=count; i++) {
                    // 하루씩 증가
                    cal.add(Calendar.DATE, 1);

                    // 형식에 맞게 String을 만들어주는 부분
                    String month;
                    String day;

                    // 10 이하이면 앞에 0을 붙여준다.
                    if((cal.get(Calendar.MONTH)+1) < 10) {
                        month = "0" + (cal.get(Calendar.MONTH)+1);
                    }
                    else {
                        month = (cal.get(Calendar.MONTH)+1) +"";
                    }

                    // 10 이하이면 앞에 0을 붙여준다.
                    if(cal.get(Calendar.DATE) < 10) {
                        day = "0" + cal.get(Calendar.DATE);
                    }
                    else {
                        day = cal.get(Calendar.DATE) + "";
                    }

                    // 2015-09-07 와 같은 형태로 만들어준다.
                    String date = cal.get(Calendar.YEAR) + "-" + month + "-" + day;
                    Log.e(TAG, "test date : " + date);

                    /**
                     * Adapter에 날짜 하나씩 추가해주는 부분
                     * 해당 날짜를 같이 aileListFragment에 넘겨준다
                     * 각 날짜마다 개별적인 fragment를 만들어서 각 날짜마다 fragment를 넘겨준다
                     */

                    // 위에서 만든 fragment 객체에 Bundle 형태로 해당 날짜를 던져준다.
                    Bundle bundle = new Bundle();

                    Log.e(TAG, "Bundle date : " + date);
                    bundle.putString("date", date);

                    Log.e(TAG, "Bundle num : " + i);
                    bundle.putInt("num", i);

                    // AileListFragment의 객체를 생성한다.
                    Fragment fragment = new AileListFragment();
                    fragment.setArguments(bundle);

                    // adapter에 Fragment를 추가시켜준다.
                    adapter.addFragment(fragment, date);
                    Log.e(TAG, i + "th date : " + date);
                }
            } catch(Exception e){}
        }
        else {
            Toast.makeText(this, "등록된 여행이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // 위에서 추가한 날짜를 viewPager에 설정해야한다
        viewPager.setAdapter(adapter);

        // 위쪽 날짜부분 넣어주는 부분
        tabLayout.setupWithViewPager(viewPager);
    }

    // Drawer 추가하는 부분
    private void setupDrawerContent() {
        // 데이터베이스 테이블에 저장이 된 테이블의 이름을 목록으로 만들어서 가져오기
        ArrayList<String> mValues = getTravelList();

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mValues));

        // 아이템을 [클릭]시의 이벤트 리스너를 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_LONG).show();
                setupViewPager(viewPager, item);

                // 마지막으로 열어본 부분 갱신해주는 부분
                prefsDefault.edit().putString(Common.PREF_LATEST, item).apply();
                mDrawerLayout.closeDrawers();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item + " LongClick!!", Toast.LENGTH_LONG).show();
                editTravel(item);

                return false;
            }
        });
    }

    // 파라미터로 받는 두개의 날짜 사이의 일 수를 반환한다
    public int getInterval(String fromDate, String toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int interval = 0;

        try {
            interval = (int)((dateFormat.parse(toDate).getTime()-dateFormat.parse(fromDate).getTime())/1000/60/60/24);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return interval;
    }

    /**
     * 사진 라이브러리에 있는 사진들 날짜와 사진 경로를 가져오는 부분(Refresh 버튼)
     */
    public void updateImages() {
        Log.e(TAG, "updateImages()");

        //여기부터 데이터베이스에 넣어주는 부분
        DBHelper mHelper = new DBHelper(this);

        //SQLite에 쓸 수 있게 만듦
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 갤러리에 접근한 커서를 가져온다
        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);


        // 기존의 내용을 지우고 다시 갱신하는 부분
        String deleteQuery = String.format("DELETE FROM %s", Common.IMAGE_TABLE);
        db.execSQL(deleteQuery);

        //만들어진 Query가 정상적인지 확인하는 부분
        Log.e(TAG, "Query : " + deleteQuery);

        try {
            // 위에서 가져온 커서가 끝날때까지 loop를 돈다
            if(c != null) {
                Log.e("Load Start : ", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()));
                while(c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    // uri를 통해서 실제 이미지의 경로를 가져와서 imgPath에 넣어준다
                    String imgPath = getRealPathFromURI(uri);

                    // 사진으로부터 정보를 가져오는 부분
                    ExifInterface exif = new ExifInterface(imgPath);
                    String date = getTagString(ExifInterface.TAG_DATETIME, exif);

                    // 만약 날짜가 있으면 저장한다
                    if(date != null) {
                        // 2015:11:12 14:42:11 -> 2015-11-12 이렇게 바꾸는 소스
                        date = date.substring(0, date.indexOf(" ")).replace(":", "-");

                        String query = String.format("INSERT INTO %s (date, image_path) VALUES('%s', '%s');", Common.IMAGE_TABLE, date, imgPath);

                        // 만들어진 Query가 정상적인지 확인하는 부분
//                        Log.e(TAG, "insertQuery : " + query);

                        //쿼리 실행
                        db.execSQL(query);
                    }
                }
                Log.e("Load End : ", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()));
                //다 썼으니 닫아줌
                mHelper.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // URI를 이용해서 실제 이미지의 경로를 가져오는 부분
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // exif 정보를 얻을 수 있는 부분
    private String getTagString(String tag, ExifInterface exif) {
        return exif.getAttribute(tag);
    }

    // 메뉴에 넣어주는 부분
    private ArrayList<String> getTravelList() {
        ArrayList<String> mValues = new ArrayList<>();

        try {
            DBHelper mHelper = new DBHelper(this);
            SQLiteDatabase db = mHelper.getReadableDatabase();
            Cursor cursor;
            String query = "select dest from " + Common.TRAVEL_TABLE;

            // 쿼리를 실행하고 거기에 대한 결과를 cursor에 넣음
            cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {
                Log.e(TAG, "travel name : " + cursor.getString(0));
                mValues.add(cursor.getString(0));
            }

            cursor.close();
            mHelper.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return mValues;
    }

    // Progress Dialog 생성하는 부분
    private class LoadingImageProgress extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);

        @Override
        // 작업시작, ProgressDialog 객체를 생성하고 시작합니다
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Loading...");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        // 진행중, ProgressDialog 의 진행 정도를 표현해 줍니다.
        protected Void doInBackground(Void... arg0) {
            try {
                for(int i=0; i<=5; i++) {
                    Thread.sleep(500);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        // 종료, ProgressDialog 종료 기능을 구현합니다.
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    } // End of AsyncTask

    private void editTravel(String dest) {
        String startDate="";
        String endDate="";

        try {
            DBHelper mHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = mHelper.getReadableDatabase();
            Cursor cursor;
            String query = "select start_date, end_date from " + Common.TRAVEL_TABLE + " where dest = '" + dest + "'";

            Log.e(TAG, "test query : " + query);

            // 쿼리를 실행하고 거기에 대한 결과를 cursor에 넣음
            cursor = db.rawQuery(query, null);

            // SQLite에 값이 비어있는지 아닌지 확인하고 들어가야 한다
            if(cursor != null && cursor.getCount() != 0 ) {

                // 맨 처음 한번 이동하고 시작해야한다
                cursor.moveToFirst();

                // DB로부터 읽어온 여행의 시작날짜와 종료날짜를 변수에 넣어준다.
                startDate = cursor.getString(0); // start_date
                endDate = cursor.getString(1); // end_date

                Log.e(TAG, "db startDate : " + startDate);
                Log.e(TAG, "db endDate : " + endDate);

                //커서를 다 썼으니 닫아주는 부분
                cursor.close();
                mHelper.close();
            }

            Intent i = new Intent(getBaseContext(), AddTravel.class);
            i.putExtra("status","edit");
            i.putExtra("dest", dest);
            i.putExtra("start", startDate);
            i.putExtra("end", endDate);
            startActivity(i);
        }
        catch(Exception e) {;}
    }
}