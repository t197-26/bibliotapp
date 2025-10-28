package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserBookSearch : AppCompatActivity() {

    lateinit var arrowBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_search)

        arrowBackButton = findViewById(R.id.UserBookSearchArrowBack)
        arrowBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}