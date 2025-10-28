package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AdminBookEditor : AppCompatActivity() {

    lateinit var capa: ImageView

    lateinit var arrowBackButtonView: ImageButton
    lateinit var editarExemplar: ImageView
    lateinit var selecionarCapa: ImageButton

    lateinit var editarMaterial: Button

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capa.setImageURI(it)
            }
        }


    lateinit var btnDescartar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_editor)

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)

        arrowBackButtonView = findViewById(R.id.adminEditBookArrowBack)
        editarExemplar = findViewById(R.id.editarExemplar)

        editarMaterial = findViewById(R.id.editarButton)
        btnDescartar = findViewById(R.id.descartarButton)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        editarExemplar.setOnClickListener {
            startActivity(Intent(this, AdminEditExemplar::class.java))

        }

        editarMaterial.setOnClickListener {
            confirmarEdicao(this);
        }

        btnDescartar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

    }

    private fun confirmarEdicao(context: android.content.Context) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_edit_material, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            startActivity(Intent(this, AdminHome::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }
}