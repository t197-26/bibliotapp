package app.cincodev.bibliotapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
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

    private var capaBase64: String? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capa.setImageURI(it)

                val bytes = getBytesFromUri(it)
                bytes?.let { imageBytes ->
                    capaBase64 = android.util.Base64.encodeToString(
                        imageBytes,
                        android.util.Base64.NO_WRAP
                    )
                }
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
                    "capa" to capaBase64,
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
            .addOnSuccessListener { documentReference ->
                Log.i("AddMaterial", "Material adicionado com sucesso! ID: ${documentReference.id}")
                val intencao = Intent(this, AdminHome::class.java)
                startActivity(intencao)
            }
            .addOnFailureListener { e ->
                Log.e("AddMaterial", "Erro ao adicionar material: ${e.message}", e)
                Toast.makeText(this, "Erro ao adicionar material: ${e.message}", Toast.LENGTH_LONG).show()
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

    private fun getBytesFromUri(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
