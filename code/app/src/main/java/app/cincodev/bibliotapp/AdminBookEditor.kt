package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AdminBookEditor : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    lateinit var editarExemplar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_editor)

        arrowBackButtonView = findViewById(R.id.adminEditBookArrowBack)
        editarExemplar = findViewById(R.id.editarExemplar)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        editarExemplar.setOnClickListener {
            startActivity(Intent(this, AdminEditExemplar::class.java))

        }

    }
}