package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserChatbot : AppCompatActivity() {

    private lateinit var arrowBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chatbot)

        arrowBackButton = findViewById(R.id.UserChatbotArrowBack)
        arrowBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // volta para a tela anterior
        }
    }
}