package com.am.chatapp.chat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.am.chatapp.chat.model.Message
import com.am.chatapp.chat.repository.ChatRepository
import com.am.chatapp.data.local.SessionManager
import com.am.chatapp.domain.repository.AuthRepository
import com.am.chatapp.domain.repository.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val sessionManager: SessionManager,

) : ViewModel() {


    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    val currentUserId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var currentUserNickname : String  = ""


    init {
        listenForMessages()
        if (currentUserId.isNotEmpty()) {
            getCurrentUserNickname()
        }
    }
    private fun getCurrentUserNickname() {
        viewModelScope.launch {
            try {

                currentUserId.takeIf { it.isNotEmpty() }?.let { uid ->
                    currentUserNickname = chatRepository.getUserNickname(uid)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to get user nickname: ${e.message}")
            }
        }
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
            senderName = currentUserNickname,
            timestamp = System.currentTimeMillis()
        )
        chatRepository.sendMessage("group_chat", message)
    }

}