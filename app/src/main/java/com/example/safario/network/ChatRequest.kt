package com.example.safario.network

data class ChatRequest(
    val model: String,
    val messages: List<Message>
)