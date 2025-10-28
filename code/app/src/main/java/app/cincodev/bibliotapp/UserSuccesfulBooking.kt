package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserSuccesfulBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_succesful_booking)

        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@UserSuccesfulBooking, UserBookDetail::class.java)
            startActivity(intent)
        }
    }
}