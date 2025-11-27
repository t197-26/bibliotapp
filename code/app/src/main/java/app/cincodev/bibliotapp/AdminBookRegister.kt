package app.cincodev.bibliotapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminBookRegister : AppCompatActivity() {
    lateinit var capa: ImageView
    lateinit var selecionarCapa: ImageButton
    lateinit var cadastrarButton: Button
    lateinit var descartarButton: Button

    lateinit var etTitulo: EditText
    lateinit var etMaterial: EditText
    lateinit var etIdioma: EditText
    lateinit var etIsbn: EditText
    lateinit var etAutor: EditText
    lateinit var etCdu: EditText
    lateinit var etEdicao:EditText
    lateinit var etPublicacao:EditText

    lateinit var fb:FirebaseFirestore

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capa.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_register)

        fb = Firebase.firestore

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)
        cadastrarButton = findViewById(R.id.cadastrarButton)
        descartarButton = findViewById(R.id.descartarButton)

        etTitulo = findViewById(R.id.inputTitulo)
        etMaterial = findViewById(R.id.inputMaterial)
        etIdioma = findViewById(R.id.inputIdioma)
        etIsbn = findViewById(R.id.inputISBN)
        etAutor = findViewById(R.id.inputAutor)
        etCdu = findViewById(R.id.inputCDUCutter)
        etEdicao = findViewById(R.id.inputEdicao)
        etPublicacao = findViewById(R.id.inputPublicacao)

        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        cadastrarButton.setOnClickListener {
            mostrarDialogoConfirmacao()
        }
    }

    private fun addMaterial() {

        fb.collection("materiais")
            .add(
                mapOf(
                    "titulo" to etTitulo.text.toString(),
                    "material" to etMaterial.text.toString(),
                    "idioma" to etIdioma.text.toString(),
                    "isbn" to etIsbn.text.toString(),
                    "autor" to etAutor.text.toString(),
                    "cdu" to etCdu.text.toString(),
                    "edicao" to etEdicao.text.toString(),
                    "publicacao" to etPublicacao.text.toString()
                )
            )
            .addOnSuccessListener {
                // só passa pra próxima tela quando o Create é concluído
                val intencao = Intent(this, AdminHome::class.java)
                startActivity(intencao)
            }
    }


    private fun mostrarDialogoConfirmacao() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirmation_book_register, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            addMaterial()
            dialog.dismiss()
        }

        dialog.show()
    }
}
