package app.cincodev.bibliotapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserBookSearch : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MaterialAdapter
    private lateinit var inputSearch: EditText

    private val listaMateriais = mutableListOf<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_search)

        recycler = findViewById(R.id.recyclerBooks)
        inputSearch = findViewById(R.id.inputSearch)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MaterialAdapter(listaMateriais)
        recycler.adapter = adapter

        configurarBusca()
        carregarMateriais()
    }

    private fun configurarBusca() {
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
