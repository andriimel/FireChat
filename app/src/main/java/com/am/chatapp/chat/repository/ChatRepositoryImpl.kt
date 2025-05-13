package com.am.chatapp.chat.repository

import android.system.Os.close
import android.util.Log
import com.am.chatapp.chat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository{

    private val chatCollection = firestore.collection("group_chat")
    override suspend fun getUserNickname(userId: String): String {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()


            println(" Document ID: $userId")
            println(" Document Data: ${snapshot.data}")
            println(" Nickname: ${snapshot.getString("nickname")}")

            snapshot.getString("nickname") ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    override fun sendMessage(chatId: String, message: Message) {
        Log.d("Firebase", "Message to send: $message")
        chatCollection
            .document(chatId)
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
        chatCollection
            .document(chatId)
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
                messages.forEach { message ->
                    Log.d("Firebase", "Fetched message: $message")
                }

                callback(messages)
            }

    }
}