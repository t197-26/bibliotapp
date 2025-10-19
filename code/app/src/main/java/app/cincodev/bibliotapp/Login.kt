package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var AccessButton: Button
    lateinit var ForgetPasswordButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        arrowBackButtonView = findViewById(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        AccessButton = findViewById(R.id.LoginAccessButton)
        AccessButton.setOnClickListener {
            startActivity(Intent(this, UserHome::class.java))
        }

        AccessButton = findViewById(R.id.LoginForgetPasswordButton)
        AccessButton.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }
}