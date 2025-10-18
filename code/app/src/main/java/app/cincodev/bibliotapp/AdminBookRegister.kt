package app.cincodev.bibliotapp

import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AdminBookRegister : AppCompatActivity() {
    lateinit var capa:ImageView
    lateinit var selecionarCapa: ImageButton

    // Inside your Activity or Fragment
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Here you get the selected image URI
                capa.setImageURI(it) // Example: set it to an ImageView
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_register)

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)

    }

    override fun onStart() {
        super.onStart()
        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
}

