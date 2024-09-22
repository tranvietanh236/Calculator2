package com.v1.testorc.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChatDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chat_database.db"
        private const val DATABASE_VERSION = 3
        const val TABLE_NAME = "chat_history"
        const val TABLE_NAME_MATH = "chat_history1"
        const val COLUMN_ID = "id"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_IS_BOT = "is_bot"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_CONVERSATION_ID = "conversation_id"

        const val TABLE_NAME_HISTORY_SMART = "history_smart"
        const val COLUMN_MESSAGE_BOT_SMART = "message_bot_smart"

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

        val createTableHistorySmart = "CREATE TABLE $TABLE_NAME_HISTORY_SMART (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MESSAGE TEXT, " +
                "$COLUMN_MESSAGE_BOT_SMART TEXT, " +
                "$COLUMN_TIMESTAMP TEXT, " +
                "$COLUMN_CONVERSATION_ID TEXT)"



        db.execSQL(createTable)
        db.execSQL(createTableMath)
        db.execSQL(createTableHistorySmart)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MATH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_HISTORY_SMART")
        onCreate(db)
    }
}