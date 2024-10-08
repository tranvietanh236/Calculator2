package com.v1.testorc.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChatDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val TABLE_NAME_SCAN = "scan_chat"
        private const val DATABASE_NAME = "chat_database.db"
        private const val DATABASE_VERSION = 4
        const val TABLE_NAME = "chat_history"
        const val TABLE_NAME_MATH = "chat_history1"
        const val TABLE_NAME_HISTORY = "history"
        const val COLUMN_ID = "id"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_MESSAGE_BOT = "message_bot"
        const val COLUMN_IS_BOT = "is_bot"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_CONVERSATION_ID = "conversation_id"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_TEXT_LIST = "list"
        const val COLUMN_TEXT_RESUILT = "resuilt"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MESSAGE TEXT, " +
                "$COLUMN_IS_BOT INTEGER, " +
                "$COLUMN_TIMESTAMP TEXT, " +
                "$COLUMN_CONVERSATION_ID TEXT)"

        val createTableMath = "CREATE TABLE $TABLE_NAME_MATH (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MESSAGE TEXT, " +
                "$COLUMN_IS_BOT INTEGER, " +
                "$COLUMN_TIMESTAMP TEXT, " +
                "$COLUMN_CONVERSATION_ID TEXT)"

        val createTableHistory = "CREATE TABLE $TABLE_NAME_HISTORY (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MESSAGE TEXT, " +
                "$COLUMN_MESSAGE_BOT TEXT, " +
                "$COLUMN_TIMESTAMP TEXT, " +
                "$COLUMN_CONVERSATION_ID TEXT)"

        val createTableScan = "CREATE TABLE $TABLE_NAME_SCAN (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_IMAGE BLOB, " +
                "$COLUMN_TEXT_LIST TEXT, " +
                "$COLUMN_TEXT_RESUILT TEXT," +
                "$COLUMN_TIMESTAMP TEXT, " +
                "$COLUMN_CONVERSATION_ID TEXT)"

        db.execSQL(createTableScan)
        db.execSQL(createTable)
        db.execSQL(createTableMath)
        db.execSQL(createTableHistory)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MATH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_HISTORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_SCAN")

        onCreate(db)
    }
}