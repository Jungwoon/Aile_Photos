package com.aile.photos;

/**
 * Created by JW on 15. 10. 9..
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB관련해서 Table생성해주는 부분
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG1 = "DBHelper";
    private static final String LOG_TAG2 = Common.LOG_TAG_STRING;

    public DBHelper(Context context){
        super(context, Common.DATABASE_NAME, null, Common.DATABASE_VERSION);
    }

    //DB가 없으면 onCreate를 생성
    public void onCreate(SQLiteDatabase db){
        Logger.d(LOG_TAG1, LOG_TAG2, "onCreate()");

        try {
            db.execSQL("CREATE TABLE " + Common.TRAVEL_TABLE + " (" +
                            "idx integer primary key autoincrement, " +
                            "dest varchar(50) not null, " +
                            "start_date varchar(45) not null, " +
                            "end_date varchar(45) not null);"
            );
        }
        catch(Exception e) { ; }

        try {

            db.execSQL("CREATE TABLE " + Common.IMAGE_TABLE + " (" +
                            "idx integer primary key autoincrement, " +
                            "date varchar(50), " +
                            "image_path varchar(50));"
            );
        }
        catch(Exception e) { ; }

    }

    //DB를 업그레이드할 때 호출된다. 기존 테이블을 삭제하고 새로 만들거나 ALTER TABLE로 스키마를 수정한다.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Logger.d(LOG_TAG1, LOG_TAG2, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + Common.TRAVEL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Common.IMAGE_TABLE);
        onCreate(db);
    }
}
