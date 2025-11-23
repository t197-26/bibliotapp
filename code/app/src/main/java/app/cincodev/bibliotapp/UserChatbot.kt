package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class UserChatbot : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var arrowBackButton: ImageButton
    private lateinit var generative: GenerativeModel
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()

    private var typingIndex: Int? = null

    private var prePrompt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chatbot)

        recyclerView = findViewById(R.id.chatRecyclerView)
        inputMessage = findViewById(R.id.inputMessage)
        btnSend = findViewById(R.id.btnSend)
        arrowBackButton = findViewById(R.id.UserChatbotArrowBack)

        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        generative = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = ""
        )


        prePrompt =
            "Você deve ser educado, e não ultrapassar 400 caracteres por resposta. "

        arrowBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSend.setOnClickListener {

            val userInput = inputMessage.text.toString().trim()
            if (userInput.isEmpty()) return@setOnClickListener

            addMessage(userInput, true)
            inputMessage.text.clear()

            lifecycleScope.launch {
                showTyping()

                try {

                    val response = generative.generateContent(prePrompt + userInput)

                    var reply = response.text ?: "Sem resposta."

                    hideTyping()
                    addMessage(reply, false)

                } catch (e: Exception) {
                    hideTyping()
                    addMessage("Erro: Tente novamente mais tarde!", false)
                }
            }
        }
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(Message(text, isUser))
        chatAdapter.notifyItemInserted(messages.size - 1)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    private fun showTyping() {
        val typingMsg = Message("Digitando...", false)
        messages.add(typingMsg)
        typingIndex = messages.lastIndex
        chatAdapter.notifyItemInserted(typingIndex!!)
        recyclerView.scrollToPosition(messages.size - 1)
    }

    private fun hideTyping() {
        typingIndex?.let { index ->
            messages.removeAt(index)
            chatAdapter.notifyItemRemoved(index)
            typingIndex = null
        }
    }
}
