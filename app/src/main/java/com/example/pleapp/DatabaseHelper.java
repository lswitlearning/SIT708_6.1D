package com.example.pleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;

    // 用户表结构
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static  final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static  final String COLUMN_PASSWORD = "password";
    private static  final String COLUMN_PHONE = "phone";

    // 用户兴趣表结构
    private static final String TABLE_USER_INTERESTS = "user_interests";
    private static final String COLUMN_USER_ID = "user_id";  // 关联到用户表的ID
    private static final String COLUMN_INTEREST = "interest";  // 存储兴趣主题

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_PHONE + " TEXT)";
        db.execSQL(createTableQuery);


    // 创建用户兴趣表
    String createInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " ("
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_INTEREST + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createInterestsTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 仅在版本升级时创建新的表
            String createInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " ("
                    + COLUMN_USER_ID + " INTEGER, "
                    + COLUMN_INTEREST + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
            db.execSQL(createInterestsTableQuery);
        }
    }

    public boolean addUser(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;  // 如果成功返回true
    }

    // 检查用户
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();  // 获取只读数据库
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";  // 检索条件
        Cursor cursor = db.rawQuery(query, new String[]{username, password});  // 查询数据库

        boolean userExists = cursor.getCount() > 0;  // 如果有匹配记录，说明用户存在
        cursor.close();  // 关闭游标
        return userExists;  // 返回检查结果
    }

    public long getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            long userId = cursor.getLong(0);
            cursor.close();
            return userId;  // 返回用户ID
        }

        cursor.close();
        return -1;  // 如果没有找到用户
    }

    public boolean addInterests(long userId, List<String> interests) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (String interest : interests) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_INTEREST, interest);

            long result = db.insert(TABLE_USER_INTERESTS, null, values);
            if (result == -1) {
                return false;  // 如果失败，返回false
            }
        }
        return true;  // 成功返回true
    }
}
