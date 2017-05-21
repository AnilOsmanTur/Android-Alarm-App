package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import model.Alarm;

/**
 * Created by anilosman on 12.05.2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Alarm> AlarmList = new ArrayList<>();

    public DataBaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating tables
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                    + Constants.KEY_ID + " INTEGER PRIMARY KEY, "
                    + Constants.ALARM_NAME + " TEXT, "
                    + Constants.ALARM_HOUR + " INT, "
                    + Constants.ALARM_MIN + " INT, "
                    + Constants.ALARM_DATE + " LONG);";

        db.execSQL(CREATE_ALARMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old one
        db.execSQL("DROP TABLE IF EXITS" + Constants.TABLE_NAME);

        // create new
        onCreate(db);

    }

    // delete an alarm
    public void deleteAlarm(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                  new String[]{ String.valueOf(id) });
        db.close();

    }

    //adding content to table
    public void addAlarm( Alarm alarm ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.ALARM_NAME, alarm.getName());
        values.put(Constants.ALARM_HOUR, alarm.getHour());
        values.put(Constants.ALARM_MIN, alarm.getMinunte());
        values.put(Constants.ALARM_DATE, System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
        db.close();

    }

    //get all alarms
    public ArrayList<Alarm> getAlarms(){

        AlarmList.clear();

        //String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                                 Constants.ALARM_NAME, Constants.ALARM_HOUR, Constants.ALARM_MIN, Constants.ALARM_DATE},
                                 null, null, null, null, Constants.ALARM_DATE + " ASC ");

        // loop through cursor
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setName(cursor.getString(cursor.getColumnIndex(Constants.ALARM_NAME)));
                alarm.setHour(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_HOUR)));
                alarm.setMinunte(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_MIN)));
                alarm.setItemID(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String dateData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.ALARM_DATE))).getTime());

                alarm.setDate(dateData);

                AlarmList.add(alarm);

            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return AlarmList;
    }

}