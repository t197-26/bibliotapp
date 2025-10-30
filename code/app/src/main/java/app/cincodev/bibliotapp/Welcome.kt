package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {
    lateinit var loginButton : Button
    lateinit var registerAccountButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        loginButton = findViewById(R.id.welcomeLoginButton)
        loginButton.setOnClickListener {
            startActivity(Intent(this@Welcome, LoginActivity::class.java))
        }

        registerAccountButton = findViewById(R.id.welcomeRegisterAccountButton)
        registerAccountButton.setOnClickListener {
            startActivity(Intent(this@Welcome, AccountRegister::class.java))
        }
    }
}