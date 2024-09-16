package com.v1.smartv1alculatorv1.Model

import com.google.gson.annotations.SerializedName

class UserMessage(
    var inputs: Map<String, Any>,
    var query: String,
    @SerializedName("response_mode") var responseMode: String,
    @SerializedName("conversation_id") var conversationId: String,
    var user: String
)