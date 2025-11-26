package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminBookSearch : AppCompatActivity(),
    AdminBookSearchAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminBookSearchAdapter
    private val listaMateriais = mutableListOf<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_search)

        recyclerView = findViewById(R.id.recyclerAdminBookSearch)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdminBookSearchAdapter(this, listaMateriais, this)
        recyclerView.adapter = adapter

        carregarMateriais()
    }

    private fun carregarMateriais() {
        FirebaseFirestore.getInstance()
            .collection("materiais")
            .get()
            .addOnSuccessListener { result ->
                listaMateriais.clear()

                for (doc in result) {
                    val material =
                        doc.toObject(Material::class.java).copy(id = doc.id)
                    listaMateriais.add(material)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ao carregar: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onEditClick(material: Material) {

        val intent = Intent(this, AdminBookEditor::class.java)

        intent.putExtra("materialId", material.id)
        intent.putExtra("titulo", material.titulo)
        intent.putExtra("autor", material.autor)
        intent.putExtra("material", material.material)
        intent.putExtra("cdu", material.cdu)
        intent.putExtra("capa", material.capa)

        startActivity(intent)
    }
}
