package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.EditText


class AdminBookEditor : AppCompatActivity() {

    lateinit var capa: ImageView

    lateinit var arrowBackButtonView: ImageButton
    lateinit var selecionarCapa: ImageButton

    lateinit var editarMaterial: Button

    lateinit var fb:FirebaseFirestore

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

        // Instância do Firestore
        fb = Firebase.firestore

        capa = findViewById(R.id.capa)
        selecionarCapa = findViewById(R.id.selecionarCapa)

        arrowBackButtonView = findViewById(R.id.adminEditBookArrowBack)

        editarMaterial = findViewById(R.id.editarButton)
        btnDescartar = findViewById(R.id.descartarButton)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        editarMaterial.setOnClickListener {
            confirmarEdicao(this)
        }

        btnDescartar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnCreateExemplar = findViewById(R.id.btnCreateExemplar)

        // Dados do material
        titulo = findViewById(R.id.bookTitle)
        material = findViewById(R.id.bookMaterial)
        idioma = findViewById(R.id.bookIdioma)
        isbn = findViewById(R.id.bookISBN)
        autor = findViewById(R.id.bookAutor)
        cdu = findViewById(R.id.bookCDU)
        edicao = findViewById(R.id.bookEdicao)
        publicacao = findViewById(R.id.bookPublicacao)

        // RecyclerView de exemplares
        recyclerView = findViewById(R.id.editExemplaresAdapter)

        // Dataset de exemplares
        dataset = mutableListOf()
        // Adapter para os exemplares
        adapter = EditExemplaresAdapter(this, dataset)

        // Instância do adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("arquivo", MODE_PRIVATE)

        /*
        prefs.edit()
            .putString("BOOK_ID", "Ga7CwjMH46qjxaEKFtKf")
            .apply()

        */

        val bookId = prefs.getString("BOOK_ID", "") ?: ""

        loadExemplares(bookId)
        readMaterial(bookId)

        btnCreateExemplar.setOnClickListener {
            createExemplar(bookId)
        }
    }

    private fun updateMaterial(bookId:String) {
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


    private fun readMaterial(bookId:String) {
        fb.collection("materiais")
            .document(bookId)
            .get()
            .addOnSuccessListener { result ->
                titulo.setText(result.get("titulo").toString())
                material.setText(result.get("material").toString())
                idioma.setText(result.get("idioma").toString())
                isbn.setText(result.get("isbn").toString())
                autor.setText(result.get("autor").toString())
                cdu.setText(result.get("cdu").toString())
                edicao.setText(result.get("edicao").toString())
                publicacao.setText(result.get("publicacao").toString())
            }
    }


    private fun loadExemplares(bookId:String) {
        fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .addSnapshotListener { snapshot, e ->

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

    private fun createExemplar(bookId:String) {
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

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            val x = getSharedPreferences("arquivo", MODE_PRIVATE)
            val bookId = x.getString("BOOK_ID", "") ?: ""

            updateMaterial(bookId)
            startActivity(Intent(this, AdminHome::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }

}