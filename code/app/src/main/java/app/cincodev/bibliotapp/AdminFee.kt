package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminFee : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_fee)

            arrowBackButtonView = findViewById(R.id.feeArrowBack)
            arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}