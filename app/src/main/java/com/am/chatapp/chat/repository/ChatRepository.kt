package com.am.chatapp.chat.repository

import com.am.chatapp.chat.model.Message

interface ChatRepository {
    fun sendMessage(chatId: String, message: Message)
    fun listenForMessages(chatId: String, callback: (List<Message>) -> Unit)
}