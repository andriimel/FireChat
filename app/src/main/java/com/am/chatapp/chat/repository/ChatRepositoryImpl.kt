package com.am.chatapp.chat.repository

import android.system.Os.close
import android.util.Log
import com.am.chatapp.chat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository{

    private val chatCollection = firestore.collection("group_chat")

    override fun sendMessage(chatId: String, message: Message) {
        chatCollection.document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d("Firebase", "Message sent!")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error sending message", e)
            }
    }

    override fun listenForMessages(chatId: String, callback: (List<Message>) -> Unit) {
        chatCollection.document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {

                    Log.e("Firebase", "Error fetching messages", error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.map {
                    it.toObject(Message::class.java)!!
                } ?: emptyList()

                callback(messages)
            }
    }
}