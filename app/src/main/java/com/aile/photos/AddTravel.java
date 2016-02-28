package com.aile.photos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class AddTravel extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText mDestnation;
    TextView mDepartDate;
    TextView mArriveDate;
    Button mBtnAdd;
    Button mBtnDel;

    String tempDate = "";
    boolean depart = false;
    boolean arrive = false;

    private static String dest;
    private static String start_date;
    private static String end_date;

    // Database에 저장하기 위한 부분
    private static DBHelper mHelper;

    String status; // 추가인지 수정인지 확인해주는 부분
    String editDest; // 바꿔야 할 이름(쿼리에 조건절로 사용하기 위함)
    String editStart;
    String editEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        mDestnation = (EditText)findViewById(R.id.editDest);
        mDestnation.setOnClickListener(this);

        mDepartDate = (TextView)findViewById(R.id.editDepart);
        mDepartDate.setOnClickListener(this);

        mArriveDate = (TextView)findViewById(R.id.editArrive);
        mArriveDate.setOnClickListener(this);

        mBtnAdd = (Button)findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);

        mBtnDel = (Button)findViewById(R.id.btnDel);
        mBtnDel.setOnClickListener(this);

        // Extra로 보낸 상태 가져와서 추가인지 수정인지 판단하는 부분
        Intent i = getIntent();
        status = i.getStringExtra("status");

        if(status.equals("new")) {
            mBtnAdd.setText("ADD");
        }
        else {
            mBtnAdd.setText("EDIT");
            mBtnDel.setVisibility(View.VISIBLE);

            editDest = i.getStringExtra("dest");
            editStart = i.getStringExtra("start");
            editEnd = i.getStringExtra("end");

            mDestnation.setText(editDest);
            mDepartDate.setText(editStart);
            mArriveDate.setText(editEnd);
        }

//        Toast.makeText(getApplicationContext(), "status : " + status, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.editDepart :
                depart = true;
                arrive = false;

                showDatePicker();

                break;

            case R.id.editArrive :
                depart = false;
                arrive = true;

                showDatePicker();

                break;

            // add 버튼을 누르면 동작하는 리스너
            case R.id.btnAdd :
                dest = mDestnation.getText().toString();
                start_date = mDepartDate.getText().toString();
                end_date = mArriveDate.getText().toString();

                Log.e("dest", dest);
                Log.e("start_date", start_date);
                Log.e("end_date", end_date);

                if(!dest.equals("") && !start_date.equals("") && !end_date.equals("")) {
                    // 새로운 여행 추가때의 로직
                    if(status.equals("new")) {
                        insertDB(dest, start_date, end_date);
                        Toast.makeText(this, "여행이 추가 되었습니다", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    // 기존 여행 수정때의 로직
                    else {
                        updateDB(dest, start_date, end_date);
                        Toast.makeText(this, "여행이 수정 되었습니다", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else {
                    Toast.makeText(this, "모든 항목을 채워야 합니다.", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnDel :
                deleteDB();
                Toast.makeText(this, "여행이 제거 되었습니다.", Toast.LENGTH_LONG).show();
                finish();

                break;
        }
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String strYear = year + "";
        String strMonth = (monthOfYear + 1) + "";
        String strDay = dayOfMonth + "";

        if(dayOfMonth < 10) {
            strDay = "0" + dayOfMonth;
        }

        if((monthOfYear + 1) < 10) {
            strMonth = "0" + (monthOfYear + 1);
        }

//        String date = "You picked the following date: "+strYear + "-" + strMonth + "-" + strDay;
//        Toast.makeText(this, date, Toast.LENGTH_LONG).show();
        tempDate = strYear + "-" + strMonth + "-" + strDay;

        if(depart) {
            mDepartDate.setText(tempDate);

        }
        else if(arrive) {
            mArriveDate.setText(tempDate);
        }

        depart = false;
        arrive = false;

    }

    //여기부터 데이터베이스에 넣어주는 부분
    public void insertDB(String dest, String start_date, String end_date) {
        try {
            mHelper = new DBHelper(getApplicationContext());

            //SQLite에 쓸 수 있게 만듦
            SQLiteDatabase db = mHelper.getWritableDatabase();

            String query = String.format("INSERT INTO %s (dest, start_date, end_date)" +
                    "VALUES('%s', '%s', '%s');", Common.TRAVEL_TABLE, dest, start_date, end_date);

            //만들어진 Query가 정상적인지 확인하는 부분
            Log.d("addTravel", "Query : " + query);

            //쿼리 실행
            db.execSQL(query);

            //다 썼으니 닫아줌
            mHelper.close();
        }
        catch(Exception e) { ; }
    }

    public void updateDB(String dest, String start_date, String end_date) {
        try {
            mHelper = new DBHelper(getApplicationContext());

            //SQLite에 쓸 수 있게 만듦
            SQLiteDatabase db = mHelper.getWritableDatabase();

            String query = String.format("UPDATE %s SET dest = '%s', start_date = '%s', end_date = '%s' WHERE dest = '%s';",
                    Common.TRAVEL_TABLE, dest, start_date, end_date, editDest);

            //만들어진 Query가 정상적인지 확인하는 부분
            Log.d("editTravel", "Query : " + query);

            //쿼리 실행
            db.execSQL(query);

            //다 썼으니 닫아줌
            mHelper.close();
        }
        catch(Exception e) { ; }
    }

    public void deleteDB() {
        try {
            mHelper = new DBHelper(getApplicationContext());

            //SQLite에 쓸 수 있게 만듦
            SQLiteDatabase db = mHelper.getWritableDatabase();

            String query = String.format("DELETE FROM %s WHERE dest = '%s';",
                    Common.TRAVEL_TABLE, editDest);

            //만들어진 Query가 정상적인지 확인하는 부분
            Log.d("editTravel", "Query : " + query);

            //쿼리 실행
            db.execSQL(query);

            //다 썼으니 닫아줌
            mHelper.close();
        }
        catch(Exception e) { ; }
    }

    public void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddTravel.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
}