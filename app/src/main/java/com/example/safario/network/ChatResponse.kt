package com.example.safario.network

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

data class MessageResponse(
    val role: String,
    val content: String
)