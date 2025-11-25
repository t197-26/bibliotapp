package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserBookSearch : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MaterialAdapter
    private val listaMateriais = mutableListOf<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_search)

        recycler = findViewById(R.id.recyclerBooks)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MaterialAdapter(listaMateriais)
        recycler.adapter = adapter

        carregarMateriais()
    }

    private fun carregarMateriais() {
        val db = FirebaseFirestore.getInstance()

        db.collection("materiais").get()
            .addOnSuccessListener { result ->
                listaMateriais.clear()

                for (doc in result) {
                    val material = doc.toObject(Material::class.java).copy(id = doc.id)
                    listaMateriais.add(material)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
