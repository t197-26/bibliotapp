package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ForgetPassword : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        arrowBackButtonView = findViewById(R.id.ForgetPasswordArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        sendButton = findViewById(R.id.ForgetPasswordSendButton)
        sendButton.setOnClickListener {
            Toast.makeText(applicationContext, "E-mail de recuperação enviado.", Toast.LENGTH_LONG).show()
            onBackPressedDispatcher.onBackPressed()
        }
    }
}