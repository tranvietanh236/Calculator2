package com.v1.smartv1alculatorv1.Database

import android.content.ContentValues
import android.content.Context
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.HistoryModel
import com.v1.smartv1alculatorv1.Model.MessageAnswer
import com.v1.testorc.Database.ChatDatabase


class ChatRepository(context: Context) {
    private val dbHelper = ChatDatabase(context)
    private val db = dbHelper.writableDatabase

    fun insertHistory(historyModel: HistoryModel){
        val values = ContentValues().apply {
            put(ChatDatabase.COLUMN_MESSAGE, historyModel.answer)
            put(ChatDatabase.COLUMN_MESSAGE_BOT, historyModel.answerBot)
            put(ChatDatabase.COLUMN_TIMESTAMP, historyModel.createdAt)
            put(ChatDatabase.COLUMN_CONVERSATION_ID, historyModel.conversationId)
        }
        db.insert(ChatDatabase.TABLE_NAME_HISTORY, null, values)
    }

    fun getAllHistory() : List<HistoryModel>{
        val hisList = mutableListOf<HistoryModel>()
        val cursor = db.query(
            ChatDatabase.TABLE_NAME_HISTORY,
            null, null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val message = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_MESSAGE))
                val messageBot = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_MESSAGE_BOT))
                val timestamp = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_TIMESTAMP))
                val conversationId =
                    getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_CONVERSATION_ID))
                hisList.add(HistoryModel(timestamp, message, messageBot, conversationId))
            }
            close()
        }
        return hisList
    }
    fun insertChat(chatAnswer: ChatAnswer) {
        val values = ContentValues().apply {
            put(ChatDatabase.COLUMN_MESSAGE, chatAnswer.answer)
            put(ChatDatabase.COLUMN_IS_BOT, if (chatAnswer.isBot) 1 else 0)
            put(ChatDatabase.COLUMN_TIMESTAMP, chatAnswer.createdAt)
            put(ChatDatabase.COLUMN_CONVERSATION_ID, chatAnswer.conversationId)
        }
        db.insert(ChatDatabase.TABLE_NAME, null, values)
    }

    fun getAllChats(): List<ChatAnswer> {
        val chatList = mutableListOf<ChatAnswer>()
        val cursor = db.query(
            ChatDatabase.TABLE_NAME,
            null, null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val message = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_MESSAGE))
                val isBot = getInt(getColumnIndexOrThrow(ChatDatabase.COLUMN_IS_BOT)) == 1
                val timestamp = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_TIMESTAMP))
                val conversationId =
                    getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_CONVERSATION_ID))
                chatList.add(ChatAnswer(timestamp, message, conversationId, isBot))
            }
            close()
        }
        return chatList
    }

    fun insertChatHis(chatAnswer: ChatAnswer) {
        val values = ContentValues().apply {
            put(ChatDatabase.COLUMN_MESSAGE, chatAnswer.answer)
            put(ChatDatabase.COLUMN_IS_BOT, if (chatAnswer.isBot) 1 else 0)
            put(ChatDatabase.COLUMN_TIMESTAMP, chatAnswer.createdAt)
            put(ChatDatabase.COLUMN_CONVERSATION_ID, chatAnswer.conversationId)
        }
        db.insert(ChatDatabase.TABLE_NAME_MATH, null, values)
    }

    fun getAllChatsHis(): List<ChatAnswer> {
        val chatList = mutableListOf<ChatAnswer>()
        val cursor = db.query(
            ChatDatabase.TABLE_NAME_MATH,
            null, null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val message = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_MESSAGE))
                val isBot = getInt(getColumnIndexOrThrow(ChatDatabase.COLUMN_IS_BOT)) == 1
                val timestamp = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_TIMESTAMP))
                val conversationId =
                    getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_CONVERSATION_ID))
                chatList.add(ChatAnswer(timestamp, message, conversationId, isBot))
            }
            close()
        }
        return chatList
    }

    fun deleteAllChats() {
        db.delete(ChatDatabase.TABLE_NAME, null, null)
    }

    fun getChatsByConversationId(conversationId: String): MutableList<ChatAnswer> {
        val chatList = mutableListOf<ChatAnswer>()
        val cursor = db.query(
            ChatDatabase.TABLE_NAME,
            null,
            "${ChatDatabase.COLUMN_CONVERSATION_ID} = ?",
            arrayOf(conversationId),
            null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val message = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_MESSAGE))
                val isBot = getInt(getColumnIndexOrThrow(ChatDatabase.COLUMN_IS_BOT)) == 1
                val timestamp = getString(getColumnIndexOrThrow(ChatDatabase.COLUMN_TIMESTAMP))
                chatList.add(ChatAnswer(timestamp, message, conversationId, isBot))
            }
            close()
        }
        return chatList
    }


    fun updateConversationId(oldConversationId: String, newConversationId: String): Int {

        val values = ContentValues().apply {
            put(ChatDatabase.COLUMN_CONVERSATION_ID, newConversationId)
        }
        return db.update(
            ChatDatabase.TABLE_NAME,
            values,
            "${ChatDatabase.COLUMN_CONVERSATION_ID} = ?",
            arrayOf(oldConversationId)
        )
    }


}
