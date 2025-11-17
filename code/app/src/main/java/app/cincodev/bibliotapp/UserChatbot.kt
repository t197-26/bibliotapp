package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class UserChatbot : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var arrowBackButton: ImageButton
    private lateinit var navbarHome: ImageView
    private lateinit var generative: GenerativeModel
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chatbot)

        recyclerView = findViewById(R.id.chatRecyclerView)
        inputMessage = findViewById(R.id.inputMessage)
        btnSend = findViewById(R.id.btnSend)
        arrowBackButton = findViewById(R.id.UserChatbotArrowBack)
        navbarHome = findViewById(R.id.AdminHomeBottomBarHomeImageView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        generative = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = ""
        )

        arrowBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        navbarHome.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSend.setOnClickListener {
            val userInput = inputMessage.text.toString().trim()
            if (userInput.isNotEmpty()) {
                addMessage(userInput, true)
                inputMessage.text.clear()

                lifecycleScope.launch {
                    val response = generative.generateContent(userInput)
                    val reply = response.text ?: "Desculpe, não consegui responder agora."
                    addMessage(reply, false)
                }
            }
        }
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(Message(text, isUser))
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }
}
