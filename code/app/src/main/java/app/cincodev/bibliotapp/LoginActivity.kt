package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var AccessButton: Button
    lateinit var ForgetPasswordButton: Button
    lateinit var matriculaEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        matriculaEditText = findViewById(R.id.LoginMatriculaEditText)

        arrowBackButtonView = findViewById(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        AccessButton = findViewById(R.id.LoginAccessButton)
        AccessButton.setOnClickListener {
            if (matriculaEditText.text.toString()[0] == '7') {
                startActivity(Intent(this, AdminHome::class.java))
            } else {
                startActivity(Intent(this, UserHome::class.java))
            }
        }

        AccessButton = findViewById(R.id.LoginForgetPasswordButton)
        AccessButton.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }
}