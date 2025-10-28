package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AdminEditExemplar : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    lateinit var btnSalvar: Button
    lateinit var btnDescartar: Button
    lateinit var btnAposentar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_exemplar)



        arrowBackButtonView = findViewById(R.id.adminEditExemplarArrowBack)

        btnSalvar = findViewById(R.id.btn_salvar)
        btnDescartar = findViewById(R.id.btn_descartar)
        btnAposentar = findViewById(R.id.btn_aposentar)



        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        btnDescartar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        btnAposentar.setOnClickListener {
            aposentar(this)
        }
    }

    private fun aposentar(context: android.content.Context) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_retire_exemplar, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            dialog.dismiss()

        }

        dialog.show()
    }
}