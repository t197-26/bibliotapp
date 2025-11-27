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
            delay(2000) // Espera 2 segundos

            val intent = Intent(this@UserFailedBooking, SelectSpace::class.java)
            startActivity(intent)
            finish()
        }
    }
}