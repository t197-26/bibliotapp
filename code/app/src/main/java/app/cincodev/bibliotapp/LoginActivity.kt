package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val matriculaEditText = findViewById<EditText>(R.id.LoginMatriculaEditText)

        val arrowBackButtonView = findViewById<ImageButton>(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val AccessButton = findViewById<Button>(R.id.LoginAccessButton)
        AccessButton.setOnClickListener {
            if (matriculaEditText.text.toString()[0] == '7') {
                startActivity(Intent(this, AdminHome::class.java))
            } else {
                startActivity(Intent(this, UserHome::class.java))
            }
        }

        val loginForgetPasswordButton = findViewById<Button>(R.id.LoginForgetPasswordButton)
        loginForgetPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }
}