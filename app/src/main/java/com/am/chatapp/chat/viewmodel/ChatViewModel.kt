package com.am.chatapp.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.am.chatapp.chat.model.Message
import com.am.chatapp.chat.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    //
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val currentUserId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        listenForMessages()
    }

    private fun listenForMessages() {
        chatRepository.listenForMessages("group_chat") { messages ->
            _messages.value = messages
        }
    }

    fun sendMessage(text: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val message = Message(
            content = text,
            senderId = currentUser?.uid ?: "",
            senderName = currentUser?.displayName ?: "Unknown",
            timestamp = System.currentTimeMillis()
        )
        chatRepository.sendMessage("group_chat", message)
    }
}