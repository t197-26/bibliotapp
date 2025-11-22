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


class AdminBookEditor : AppCompatActivity() {

    lateinit var capa: ImageView

    lateinit var arrowBackButtonView: ImageButton
    lateinit var selecionarCapa: ImageButton

    lateinit var editarMaterial: Button

    lateinit var fb:FirebaseFirestore

    lateinit var dataset: MutableList<Exemplar>
    lateinit var adapter: EditExemplaresAdapter
    lateinit var recyclerView: RecyclerView

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

        recyclerView = findViewById(R.id.editExemplaresAdapter)

        dataset = mutableListOf()
        adapter = EditExemplaresAdapter(this, dataset)

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