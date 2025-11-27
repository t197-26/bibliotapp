package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.EditText
import androidx.core.content.edit

class AdminBookEditor : AppCompatActivity() {

    private lateinit var bookId: String

    lateinit var capa: ImageView
    lateinit var arrowBackButtonView: ImageButton
    lateinit var selecionarCapa: ImageButton
    lateinit var editarMaterial: Button
    lateinit var fb: FirebaseFirestore

    lateinit var dataset: MutableList<Exemplar>
    lateinit var adapter: EditExemplaresAdapter
    lateinit var recyclerView: RecyclerView

    lateinit var titulo: EditText
    lateinit var material: EditText
    lateinit var idioma: EditText
    lateinit var isbn: EditText
    lateinit var autor: EditText
    lateinit var cdu: EditText
    lateinit var edicao: EditText
    lateinit var publicacao: EditText

    lateinit var btnCreateExemplar: ImageView
    lateinit var btnDescartar: Button

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { capa.setImageURI(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_editor)

        fb = Firebase.firestore

        // ===== RECEBENDO O LIVRO SELECIONADO =====
        bookId = intent.getStringExtra("materialId") ?: ""

        val base64Capa = intent.getStringExtra("capa")
        val tituloExtra = intent.getStringExtra("titulo")
        val autorExtra = intent.getStringExtra("autor")
        val materialExtra = intent.getStringExtra("material")
        val cduExtra = intent.getStringExtra("cdu")

        val prefs = getSharedPreferences("arquivo",MODE_PRIVATE)
        prefs.edit { putString("BOOK_ID", bookId) }

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)
        arrowBackButtonView = findViewById(R.id.adminEditBookArrowBack)
        editarMaterial = findViewById(R.id.editarButton)
        btnDescartar = findViewById(R.id.descartarButton)
        btnCreateExemplar = findViewById(R.id.btnCreateExemplar)

        titulo = findViewById(R.id.bookTitle)
        material = findViewById(R.id.bookMaterial)
        idioma = findViewById(R.id.bookIdioma)
        isbn = findViewById(R.id.bookISBN)
        autor = findViewById(R.id.bookAutor)
        cdu = findViewById(R.id.bookCDU)
        edicao = findViewById(R.id.bookEdicao)
        publicacao = findViewById(R.id.bookPublicacao)

        // ===== SETANDO OS VALORES RECEBIDOS =====
        titulo.setText(tituloExtra)
        autor.setText(autorExtra)
        material.setText(materialExtra)
        cdu.setText(cduExtra)

        if (!base64Capa.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(Base64.decode(base64Capa, Base64.DEFAULT))
                .into(capa)
        }

        // ===== RECYCLERVIEW DE EXEMPLARES =====
        recyclerView = findViewById(R.id.editExemplaresAdapter)
        dataset = mutableListOf()
        adapter = EditExemplaresAdapter(this, dataset)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // BOTÕES
        arrowBackButtonView.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        selecionarCapa.setOnClickListener { pickImageLauncher.launch("image/*") }
        btnDescartar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        editarMaterial.setOnClickListener { confirmarEdicao(this) }
        btnCreateExemplar.setOnClickListener { createExemplar(bookId) }

        // CARREGAR CAMPOS COMPLETOS DO FIRESTORE
        readMaterial(bookId)
        loadExemplares(bookId)
    }

    // ================================
    // FIRESTORE
    // ================================

    private fun readMaterial(bookId: String) {
        fb.collection("materiais")
            .document(bookId)
            .get()
            .addOnSuccessListener { result ->
                idioma.setText(result.getString("idioma") ?: "")
                isbn.setText(result.getString("isbn") ?: "")
                edicao.setText(result.getString("edicao") ?: "")
                publicacao.setText(result.getString("publicacao") ?: "")
            }
    }

    private fun updateMaterial(bookId: String) {
        fb.collection("materiais")
            .document(bookId)
            .update(
                mapOf(
                    "titulo" to titulo.text.toString(),
                    "material" to material.text.toString(),
                    "idioma" to idioma.text.toString(),
                    "isbn" to isbn.text.toString(),
                    "autor" to autor.text.toString(),
                    "cdu" to cdu.text.toString(),
                    "edicao" to edicao.text.toString(),
                    "publicacao" to publicacao.text.toString()
                )
            )
    }

    private fun loadExemplares(bookId: String) {
        fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    dataset.clear()
                    for (doc in snapshot) {
                        dataset.add(
                            Exemplar(
                                doc.id,
                                doc.getString("suporte") ?: "",
                                doc.getString("registro") ?: "",
                                doc.getString("disponibilidade") ?: "",
                                doc.getString("status") ?: ""
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun createExemplar(bookId: String) {
        fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .add(
                mapOf(
                    "disponibilidade" to "Imediata",
                    "registro" to "XXXXXX",
                    "status" to "Disponível",
                    "suporte" to "Impresso",
                    "ano" to "XXXX",
                    "situacao" to "Cativo"
                )
            )
    }

    private fun confirmarEdicao(context: android.content.Context) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_edit_material, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener { dialog.dismiss() }

        btnConfirmar.setOnClickListener {
            updateMaterial(bookId)
            startActivity(Intent(this, AdminHome::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }
}
