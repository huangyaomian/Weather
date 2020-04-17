package com.hym.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public static SQLiteDatabase database;
    //初始化數據庫信息
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    //查找數據庫當中城市列表
    public static List<String> queryAllCityName() {
        Cursor cursor = database.query("info", null, null, null, null, null, null, null);
        ArrayList<String> cityList = new ArrayList<>();
        while (cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    //更新城市名称，替换信息内容
    public static int updateInfoByCity(String city, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        return database.update("info", contentValues,"city=?", new String[]{city});
    }

    //新增一条城市记录
    public static long addCityInfo(String city, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("city", city);
        contentValues.put("content", content);
        return database.insert("info", null, contentValues);
    }

    //根据城市名称，查询数据库当中的内容
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    //存储城市天气要求最多存储5个城市的信息，一旦超过5个城市了就不能存储了，获取目前已经存储的数量
    public static int getCityCount(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        int count = cursor.getCount();
        return count;
    }

    //查詢數據庫當中的全部信息
    public static List<DatabaseBean> queryAllInfo(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<DatabaseBean> list = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DatabaseBean databaseBean = new DatabaseBean(id, city, content);
            list.add(databaseBean);
        }
        return list;
    }

    //根據城市名稱，刪除這個城市在數據庫當中的數據
    public static int deleteInfoCity(String city){
        return database.delete("info","city=?",new String[]{city});
    }

    //清空info表，不刪除表
    public static void deleteAllInfo(){
        database.execSQL("delete from info");
    }

}
