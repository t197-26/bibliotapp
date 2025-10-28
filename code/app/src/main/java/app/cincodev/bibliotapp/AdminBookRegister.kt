package app.cincodev.bibliotapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AdminBookRegister : AppCompatActivity() {
    lateinit var capa: ImageView
    lateinit var selecionarCapa: ImageButton
    lateinit var cadastrarButton: Button
    lateinit var descartarButton: Button

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capa.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_register)

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)
        cadastrarButton = findViewById(R.id.cadastrarButton)
        descartarButton = findViewById(R.id.descartarButton)

        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        cadastrarButton.setOnClickListener {
            mostrarDialogoConfirmacao()
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
            registrarLivro()
            dialog.dismiss()

            var intencao = Intent(this, AdminHome::class.java)
            startActivity(intencao)
        }

        dialog.show()
    }


    private fun registrarLivro() {
        // Aqui vai o código para salvar o livro
        // Exemplo:
        // Toast.makeText(this, "Livro cadastrado!", Toast.LENGTH_SHORT).show()
    }
}
