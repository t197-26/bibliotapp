package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserProcessingBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_processing_booking)

        lifecycleScope.launch {
            delay(2000) // 2 seconds
            val intent = Intent(this@UserProcessingBooking, UserFailedBooking::class.java)
            startActivity(intent)
            finish()
        }
    }
}
