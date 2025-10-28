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

class UserProcessingBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_processing_booking)

        lifecycleScope.launch {
            delay(1500)
            val intent = Intent(this@UserProcessingBooking, UserSuccesfulBooking::class.java)
            startActivity(intent)
        }
    }
}