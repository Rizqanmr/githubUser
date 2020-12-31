package com.rizqanmr.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.TABLE_NAME

internal class DBHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "dbUser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${UserContract.FavColumns.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${UserContract.FavColumns.NAME} TEXT NOT NULL," +
                " ${UserContract.FavColumns.AVATAR} TEXT NOT NULL," +
                " ${UserContract.FavColumns.COMPANY} TEXT NOT NULL," +
                " ${UserContract.FavColumns.LOCATION} TEXT NOT NULL," +
                " ${UserContract.FavColumns.REPOSITORY} INTEGER NOT NULL," +
                " ${UserContract.FavColumns.FOLLOWERS} INTEGER NOT NULL," +
                " ${UserContract.FavColumns.FOLLOWING} INTEGER NOT NULL," +
                " ${UserContract.FavColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}