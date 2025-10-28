package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserFailedBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_failed_booking)

        lifecycleScope.launch {
            delay(2000) // 2 seconds
            val intent = Intent(this@UserFailedBooking, UserSuccesfulBooking::class.java)
            startActivity(intent)
            finish()
        }
    }
}
