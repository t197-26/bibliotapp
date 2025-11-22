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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.EditText
import android.widget.TextView


class AdminBookEditor : AppCompatActivity() {

    lateinit var capa: ImageView

    lateinit var arrowBackButtonView: ImageButton
    lateinit var selecionarCapa: ImageButton

    lateinit var editarMaterial: Button

    lateinit var fb:FirebaseFirestore

    lateinit var dataset: MutableList<Exemplar>
    lateinit var adapter: EditExemplaresAdapter
    lateinit var recyclerView: RecyclerView

    lateinit var titulo: TextView
    lateinit var material: EditText
    lateinit var idioma: EditText
    lateinit var isbn: EditText
    lateinit var autor: EditText
    lateinit var cdu: EditText
    lateinit var edicao: EditText
    lateinit var publicacao: EditText

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
            confirmarEdicao(this);
        }

        btnDescartar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        selecionarCapa.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

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

    override fun onStart() {
        super.onStart()
        loadExemplares()
        readMaterial()
    }

    private fun readMaterial() {
        fb.collection("materiais")
            .document("default")
            .get()
            .addOnSuccessListener { result ->
                titulo.text = result.get("titulo").toString()
                material.setText(result.get("material").toString())
                idioma.setText(result.get("idioma").toString())
                isbn.setText(result.get("isbn").toString())
                autor.setText(result.get("autor").toString())
                cdu.setText(result.get("cdu").toString())
                edicao.setText(result.get("edicao").toString())
                publicacao.setText(result.get("publicacao").toString())
            }
    }


    private fun loadExemplares() {
        fb.collection("materiais")
            .document("default")
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

}