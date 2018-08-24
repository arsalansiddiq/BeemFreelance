package com.example.arsalansiddiq.beem.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;

/**
 * Created by arsalansiddiq on 2/19/18.
 */

public class BeemDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "beem.db";

    // Table names
    private static final String TABLE_LOGIN = "user_info";
    public static String TABLE_USER_LOCATION = "ba_location_table";
    public static String TABLE_BA_ATTENDANCE = "ba_attendance_table";

    // user_info Columns names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";

    //Location Table Columns Names
    private static final String KEY_LOCATION_ID = "location_ids";
    private static final String KEY_PERIMETER_LATITUDE = "latitude";
    private static final String KEY_PERIMETER_LONGITUDE = "longitude";

    //TABLE_BA_ATTENDANCE Columns Names
    private static final String KEY_BA_ATTENDANCE_ID = "ba_attendance_ids";
    private static final String KEY_BA_NAME = "ba_name";
    private static final String KEY_BA_ATTENDANCE_DATE = "ba_attendance_date";
    private static final String KEY_START_ATTENDANCE_TIME = "ba_start_attendance_time";
    private static final String KEY_END_ATTENDANCE_TIME = "ba_end_attendance_time";
    private static final String KEY_START_ATTENDANCE_LATITUDE = "ba_start_attendance_latitude";
    private static final String KEY_START_ATTENDANCE_LONGITUDE = "ba_start_attendance_longitude";
    private static final String KEY_END_ATTENDANCE_LATITUDE = "ba_end_attendance_latitude";
    private static final String KEY_END_ATTENDANCE_LONGITUDE = "ba_start_attendance_longitude";
    private static final String KEY_BA_ATTENDANCE_STATUS = "ba_attendance_status";

    private final String userTable = "create table " + TABLE_LOGIN + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY, " + KEY_NAME +
            " TEXT ," + KEY_BRAND + " TEXT);";

    private final String perimeterTable = "create table " + TABLE_USER_LOCATION + " (" + KEY_LOCATION_ID + " INTEGER PRIMARY KEY autoincrement, " + KEY_PERIMETER_LATITUDE +
            " float, " + KEY_PERIMETER_LONGITUDE + " float);";

    private final String attendanceTable = "create table " + TABLE_BA_ATTENDANCE + " (" + KEY_BA_ATTENDANCE_ID + " INTEGER PRIMARY KEY autoincrement, " + KEY_BA_NAME +
            " TEXT, " + KEY_BA_ATTENDANCE_DATE + " TEXT, " + KEY_START_ATTENDANCE_TIME + "TEXT, "
            + KEY_END_ATTENDANCE_TIME + " TEXT, " + KEY_START_ATTENDANCE_LATITUDE + " float, "
            + KEY_START_ATTENDANCE_LONGITUDE + " float, " + KEY_END_ATTENDANCE_LATITUDE + " float, " +
            KEY_END_ATTENDANCE_LONGITUDE + " float, " + KEY_BA_ATTENDANCE_STATUS + " float);";

    public BeemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("DB", "DB cReated");

        sqLiteDatabase.execSQL(userTable);

        sqLiteDatabase.execSQL(perimeterTable);

//        sqLiteDatabase.execSQL(attendanceTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOCATION);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BA_ATTENDANCE);

        onCreate(sqLiteDatabase);
    }


    public void insertBAInfo(int id, String name,String brand) {
        SQLiteDatabase db = this.getWritableDatabase();

//        DBModel dbModel = new DBModel(id, number,devId);
//
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_BRAND, brand);

        // Inserting Row
        long check = db.insert(TABLE_LOGIN, null, values);

//        Log.i("ID and Number Added: ", String.valueOf(check));
        db.close();
    }

    public void insertBA_AttendanceInfo(int id, String date, String name, String startTime,
                                        float sLatitude, float sLongitude, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BA_ATTENDANCE_ID, id);
        values.put(KEY_BA_ATTENDANCE_DATE, date);
        values.put(KEY_BA_NAME, name);
        values.put(KEY_START_ATTENDANCE_TIME, startTime);
        values.put(KEY_START_ATTENDANCE_LATITUDE, sLatitude);
        values.put(KEY_START_ATTENDANCE_LONGITUDE, sLongitude);
        values.put(KEY_BA_ATTENDANCE_STATUS, status);

        // Inserting Row
        long check = db.insert(TABLE_LOGIN, null, values);

        Log.i("raw inserted ", String.valueOf(check));
        db.close();
    }

    public void updateBA_AttendanceInfo(int id, String endTime, float eLatitude, float eLongitude) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_END_ATTENDANCE_TIME, endTime);
        values.put(KEY_END_ATTENDANCE_LATITUDE, eLatitude);
        values.put(KEY_END_ATTENDANCE_LONGITUDE, eLongitude);

        // Updating Row
        int  done = db.update(TABLE_BA_ATTENDANCE, values, KEY_BA_ATTENDANCE_ID+"="+id, null);

        Log.i("rowUpdated: ", String.valueOf(done));
        db.close();
    }



    public int getBA_Attendance_ID() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "select * from " + TABLE_BA_ATTENDANCE + " order by " + KEY_BA_ATTENDANCE_ID + " desc limit 1;";

        Cursor cursor = db.rawQuery(countQuery, null);

        int id = 0;
        if (cursor.moveToFirst()) {
            int getid = cursor.getInt(cursor.getColumnIndex(KEY_BA_ATTENDANCE_ID));
            id = getid;
        }
        cursor.close();
        db.close();
         return id;
    }





//
//    public int getID() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String countQuery = "select * from " + TABLE_NAME + " order by " + KEY_ID + " desc limit 1;";
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int id = 0;
//        if (cursor.moveToFirst()) {
//            int getid = cursor.getInt(cursor.getColumnIndex(KEY_ID));
//            String devId = cursor.getString(cursor.getColumnIndex(DEVICE_ID));
//            Log.i("gotid: ", String.valueOf(getid));
//            Log.i("gotid: ", String.valueOf(devId));
//
//            id = getid;
//        }
//        cursor.close();
//        db.close();
//         return id;
//    }

    public LoginResponse getUserDetail() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "select * from " + TABLE_LOGIN + " order by " + KEY_USER_ID + " desc limit 1;";

        Cursor cursor = db.rawQuery(countQuery, null);
        LoginResponse loginResponse = new LoginResponse();

        String id = "";

        if (cursor.moveToFirst()) {

            String userID = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
            String userBrand = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));

            loginResponse.setUserId(Integer.valueOf(userID));
            loginResponse.setName(userName);
            loginResponse.setBrand(userBrand);

        }

        cursor.close();
        db.close();
        return loginResponse;
    }


//    public String getNumber(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String countQuery = "select "+ KEY_NUMBER + " from " + TABLE_NAME + " where " + KEY_ID + " = " + "'" + id + "'" + ";";
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        String numb = null;
//
//        if (cursor.moveToFirst()) {
//            numb = cursor.getString(cursor.getColumnIndex(KEY_NUMBER));
//            Log.i("got Number: ", numb);
//
//        }
//        cursor.close();
//        db.close();
//        return numb;
//    }



//    public void insertTime (Date time, int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TIME, String.valueOf(time));
//
//        int  done = db.update(TABLE_NAME, contentValues, KEY_ID+"="+id, null);
//
//        Log.i("Time ", done+"");
//        db.close();
//    }

//    public void uopdateTime (Date time, int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TIME, String.valueOf(time));
//
//        int  done = db.update(TABLE_NAME, contentValues, KEY_ID+"="+id, null);
//
//        Log.i("Time ", done+"");
//        db.close(); // Closing database connection
//    }



//    public String getTime (int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String countQuery = "select "+ TIME + " from " + TABLE_NAME + " where " + KEY_ID + " = " + "'" + id + "'" + ";";
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        String time = null;
//
//        if (cursor.moveToFirst()) {
//            time = cursor.getString(cursor.getColumnIndex(TIME));
//        }
//
//        cursor.close();
//        db.close();
//        return time;
//    }

//    public boolean getCount () {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String countQuery = "select * from " + TABLE_NAME;
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        Log.i("DB GETCOUNT", String.valueOf(cursor.getCount()));
//
//        if (cursor.getCount() == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    public boolean checkUserExist (int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "select * from " + TABLE_LOGIN + " where " + KEY_USER_ID + "=" + id;
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }

    }

//    public void insertPerimeterPoints(List<LatLng> points, List<Float> zoom) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        DBModel dbModel = new DBModel(points, zoom);
//        ContentValues values = new ContentValues();
//        LatLng latLng;
//        List<LatLng> fromModel = dbModel.getPoints();
//        long check = 0;
//
//        double lat, lang;
//        for (int i = 0; i < points.size(); i++) {
//
//            latLng = fromModel.get(i);
//            lat = latLng.latitude;
//            lang = latLng.longitude;
//            values.put(KEY_PERIMETER_LATITUDE, lat); // Contact Name
//            values.put(KEY_PERIMETER_LONGITUDE, lang); // Contact Name
//            values.put(KEY_PERIMETER_CAMERA_ZOOM, zoom.get(i));
//            check = db.insert(TABLE_NAME_PERIMETER, null, values);
//            Log.i("ID and Number Added: ", String.valueOf(check));
//        }
//        db.close();
//    }


//    public List<LatLng> getPerimeterPoints() {
//
//       List<LatLng> points = new ArrayList<LatLng>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String getPointsQuesry = "select * from " + TABLE_NAME_PERIMETER;
//
//        Cursor cursor = db.rawQuery(getPointsQuesry, null);
//
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                LatLng latLng = new LatLng(cursor.getDouble(cursor.getColumnIndex(KEY_PERIMETER_LATITUDE)),
//                        cursor.getDouble(cursor.getColumnIndex(KEY_PERIMETER_LONGITUDE)));
//                Log.i("FromDB", String.valueOf(latLng));
//                points.add(latLng);
//                cursor.moveToNext();
//            }
//        }
//
//        cursor.close();
//        db.close();
//
//        return points;
//    }

//    public float getAverageCameraPosition() {
//
////        List<Float> points = new ArrayList<LatLng>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String getPointsQuesry = "select " + KEY_PERIMETER_CAMERA_ZOOM+ " from " + TABLE_NAME_PERIMETER;
//
//        Cursor cursor = db.rawQuery(getPointsQuesry, null);
//
//        float allZoomPositions = 0;
//
//        int count = 0;
//
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                allZoomPositions += cursor.getFloat(cursor.getColumnIndex(KEY_PERIMETER_CAMERA_ZOOM));
//                Log.i("ZoomAverageFromDB", String.valueOf(allZoomPositions));
//                count++;
//                cursor.moveToNext();
//            }
//        }
//
//        Log.i("TotalZoom", String.valueOf(allZoomPositions));
//        cursor.close();
//        db.close();
//
//        return (allZoomPositions / count);
//    }
//
//    public boolean hasCoordinates(){
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String countQuery = "select * from " + TABLE_NAME_PERIMETER;
//
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        Log.i("Points Count", String.valueOf(cursor.getCount()));
//
//        if (cursor.getCount() == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public void isDeleted() {
//        SQLiteDatabase db = this.getWritableDatabase();
//
////        String countQuery = "delete from perimeter_table";
////
////        db.execSQL(countQuery);
//        db.delete(TABLE_NAME_PERIMETER, null, null);
//        db.execSQL("vacuum");
//
//        db.close();
//
//    }

}
