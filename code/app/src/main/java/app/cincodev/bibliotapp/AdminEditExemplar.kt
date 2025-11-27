package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminEditExemplar : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    lateinit var btnSalvar: Button
    lateinit var btnDescartar: Button
    lateinit var btnAposentar: Button

    lateinit var fb: FirebaseFirestore

    lateinit var etRegistro: EditText
    lateinit var etAno: EditText
    lateinit var etSituacao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_exemplar)

        // Instância do Firebase
        fb = Firebase.firestore

        arrowBackButtonView = findViewById(R.id.adminEditExemplarArrowBack)

        btnSalvar = findViewById(R.id.btn_salvar)
        btnDescartar = findViewById(R.id.btn_descartar)
        btnAposentar = findViewById(R.id.btn_aposentar)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Dados do exemplar
        etRegistro = findViewById(R.id.etRegistro)
        etAno = findViewById(R.id.etAno)
        etSituacao = findViewById(R.id.etSituacao)

        // Salvar edições do exemplar
        btnSalvar.setOnClickListener {
            val x = getSharedPreferences("arquivo", MODE_PRIVATE)
            val editExemplarId = x.getString("EDIT_EXEMPLAR_ID", "") ?: ""

            updateExemplar(editExemplarId)

            onBackPressedDispatcher.onBackPressed()
        }

        // Descartar edições do exemplar
        btnDescartar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Aposentar exemplar
        btnAposentar.setOnClickListener {
            aposentar(this)
        }

    }

    private fun updateExemplar(exemplarId: String) {

        fb.collection("materiais")
            .document("default")
            .collection("exemplares")
            .document(exemplarId)
            .update(
                mapOf(
                    "registro" to etRegistro.text.toString(),
                    "ano" to etAno.text.toString(),
                    "situacao" to etSituacao.text.toString()
                )
            )
    }

    private fun deleteExemplar(exemplarId: String) {
        fb.collection("materiais")
            .document("default")
            .collection("exemplares")
            .document(exemplarId)
            .delete()
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
            // Confirma aposentadoria
            val x = getSharedPreferences("arquivo", MODE_PRIVATE)
            val editExemplarId = x.getString("EDIT_EXEMPLAR_ID", "") ?: ""
            deleteExemplar(editExemplarId)
            onBackPressedDispatcher.onBackPressed()
        }

        dialog.show()
    }
}