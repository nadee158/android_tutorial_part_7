package lk.uok.mit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.uok.mit.model.Message;

public class MyDataBaseOperations {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public MyDataBaseOperations(Context context) {
        this.dbHelper = new MyDatabaseHelper(context);
    }

    private void openDatabase(boolean readOnly) {
        if (readOnly) {
            this.database = this.dbHelper.getReadableDatabase();
        } else {
            this.database = this.dbHelper.getWritableDatabase();
        }
    }

    private void closeDatabase() {
        this.dbHelper.close();
    }

    //to format the "sent_time" to a string
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");

    //the method to insert a record to "message" table
    public Message createMessage(Message message) {
        //Initialize a variable named “contentValues” of type “android.content.ContentValues”
        ContentValues contentValues = new ContentValues();
        //Put the column names, and values in to contentValues
        contentValues.put(MyDatabaseHelper.COLUMN_CONTACT_NUMBER, message.getContactNumber());
        contentValues.put(MyDatabaseHelper.COLUMN_MESSAGE_TEXT, message.getMessageText());
        contentValues.put(MyDatabaseHelper.COLUMN_SENT_TIME, simpleDateFormat.format(message.getSentTime()));
        contentValues.put(MyDatabaseHelper.COLUMN_SENT_STATUS, message.isSentStatus());
        contentValues.put(MyDatabaseHelper.COLUMN_RETRY_COUNT, message.getRetryCount());
        //create/open a writable database
        this.openDatabase(false);
        //Call insert method of “database” and assigned the returned generated primary key to message
        long generatedId = this.database.insert(MyDatabaseHelper.TABLE_MESSAGE, null, contentValues);
        message.setId(Integer.parseInt(Long.toString(generatedId)));
        //close the database
        this.closeDatabase();
        //return the message with assigned generated key
        return message;
    }

    private static final String[] ALL_COLUMNS_OF_TABLE_MESSAGE = {
            MyDatabaseHelper.COLUMN_ID,
            MyDatabaseHelper.COLUMN_CONTACT_NUMBER,
            MyDatabaseHelper.COLUMN_MESSAGE_TEXT,
            MyDatabaseHelper.COLUMN_SENT_TIME,
            MyDatabaseHelper.COLUMN_SENT_STATUS,
            MyDatabaseHelper.COLUMN_RETRY_COUNT
    };


    public List<Message> getAllMessages() {
        //open the database as read only - not updating anything now
        this.openDatabase(true);
        //execute the query for given parameters and get the resulting Cursor object, which is positioned before the first entry.
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_MESSAGE, ALL_COLUMNS_OF_TABLE_MESSAGE,
                null, null, null, null, null);
        //initalize an empty ArrayList of type "Message" to contain the created java objects from result set
        List<Message> messages = new ArrayList<Message>();
        //checkif there are any resulting records, count should be greater than 0
        if (cursor.getCount() > 0) {
            //next iterate over the resulting rows, and create a Message object for each row
            while (cursor.moveToNext()) {
                //create a Message object for each row
                Message message = new Message();
                //set properties of Message object by retrieving the values from relavent column
                message.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ID)));
                message.setContactNumber(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_CONTACT_NUMBER)));
                message.setMessageText(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_MESSAGE_TEXT)));
                //recall, we set the "sent_time", as a string, now we have to retrieve it as a string
                // and parse it to a "java.util.Date" by using the same "java.text.SimpleDateFormat" object - same date pattern
                //the Date parsing operation can throw a "java.text.ParseException", handle it in a try-catch block
                try {
                    message.setSentTime(simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_SENT_TIME))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //even though we set the boolean value to "sent_status" column, SQLite database stores it as an int
                // value 1-true, value 0- false ; based on that we have set the sent_status
                message.setSentStatus(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_SENT_STATUS)) > 0);
                message.setRetryCount(cursor.getShort(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_RETRY_COUNT)));
                //after assigning all the properties, add the created message object to the list we initialized
                messages.add(message);
            }
        }
        this.closeDatabase();
        // return All Messages
        return messages;
    }
}
