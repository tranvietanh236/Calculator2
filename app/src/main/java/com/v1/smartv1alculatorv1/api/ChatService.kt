package com.v1.smartv1alculatorv1.api

import com.v1.smartv1alculatorv1.Model.UserMessage
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody

interface ChatService {
    @POST("chat-messages")
    fun sendMessage(@Body userMessage: UserMessage): Observable<ResponseBody>?
}