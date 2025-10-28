package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserChatbot : AppCompatActivity() {

    lateinit var arrowBackButton: ImageButton
    lateinit var navbar_home: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chatbot)

        arrowBackButton = findViewById(R.id.UserChatbotArrowBack)

        navbar_home = findViewById(R.id.AdminHomeBottomBarHomeImageView)

        arrowBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // volta para a tela anterior
        }

        navbar_home.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}