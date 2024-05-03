package com.example.pleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";  // Database name
    private static final int DATABASE_VERSION = 2;  // Database version

    // User table structure
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";

    // User interests table structure
    private static final String TABLE_USER_INTERESTS = "user_interests";
    private static final String COLUMN_USER_ID = "user_id";  // Foreign key to user table
    private static final String COLUMN_INTEREST = "interest";  // Stores user interests

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  // Database initialization
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_PHONE + " TEXT)";
        db.execSQL(createTableQuery);

        // Create user interests table
        String createInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " ("
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_INTEREST + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createInterestsTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // If upgrading, create the new user interests table
            String createInterestsTableQuery = "CREATE TABLE " + TABLE_USER_INTERESTS + " ("
                    + COLUMN_USER_ID + " INTEGER, "
                    + COLUMN_INTEREST + " TEXT, "
                    + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
            db.execSQL(createInterestsTableQuery);
        }
    }

    // Add a new user
    public boolean addUser(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();  // Get writable database
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;  // If insertion is successful, return true
    }

    // Check user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();  // Get read-only database
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USERNAME + " = ? AND "
                + COLUMN_PASSWORD + " = ?";  // Query to check user
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean userExists = cursor.getCount() > 0;  // Check if user exists
        cursor.close();  // Close cursor
        return userExists;  // Return check result
    }

    // Get user ID by username
    public long getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();  // Get read-only database
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            long userId = cursor.getLong(0);  // Get the user ID
            cursor.close();
            return userId;  // Return user ID
        }

        cursor.close();
        return -1;  // Return -1 if user not found
    }

    // Add interests for a specific user
    public boolean addInterests(long userId, List<String> interests) {
        SQLiteDatabase db = this.getWritableDatabase();  // Get writable database
        for (String interest : interests) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_INTEREST, interest);

            long result = db.insert(TABLE_USER_INTERESTS, null, values);
            if (result == -1) {
                return false;  // If insertion failed, return false
            }
        }
        return true;  // If successful, return true
    }
}
