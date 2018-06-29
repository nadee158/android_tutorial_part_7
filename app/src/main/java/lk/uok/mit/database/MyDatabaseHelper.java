package lk.uok.mit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "message_db";
    public static final int DB_VERSION = 1;

    //TABLE NAMES
    public static final String TABLE_MESSAGE = "message";

    //COLUMN NAMES
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_SENT_TIME = "sent_time";
    public static final String COLUMN_SENT_STATUS = "sent_status";
    public static final String COLUMN_RETRY_COUNT = "retry_count";

    //SQL STATEMENTS
    /*CREATE TABLE message (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            contact_number TEXT,
            message_text TEXT,
            sent_time TEXT,
            sent_status INT UNSIGNED,
            retry_count INT UNSIGNED
    );*/
    //CREATE TABLE - message
    public static final String SQL_CREATE_TABLE_MESSAGE =
            new StringBuilder("CREATE TABLE ").append(TABLE_MESSAGE).append(" ( ")
                    .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ").append(",")
                    .append(COLUMN_CONTACT_NUMBER).append(" TEXT ").append(",")
                    .append(COLUMN_MESSAGE_TEXT).append(" TEXT ").append(",")
                    .append(COLUMN_SENT_TIME).append(" TEXT ").append(",")
                    .append(COLUMN_SENT_STATUS).append(" INT UNSIGNED ").append(",")
                    .append(COLUMN_RETRY_COUNT).append(" INT UNSIGNED ")
                    .append(" ) ").toString();

    //DROP TABLE - message
    public static final String SQL_DROP_TABLE_MESSAGE =
            new StringBuilder("DROP TABLE IF EXISTS ").append(TABLE_MESSAGE).toString();

    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute the CREATE TABLE STATEMENT
        db.execSQL(SQL_CREATE_TABLE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE_MESSAGE);
        db.execSQL(SQL_CREATE_TABLE_MESSAGE);
    }

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
