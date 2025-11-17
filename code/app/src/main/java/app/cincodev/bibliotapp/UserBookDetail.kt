package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.util.Base64
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserBookDetail : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton

    // Firebase
    lateinit var fb:FirebaseFirestore

    // Campos do detalhamento de material
    lateinit var bookCapa: ImageView
    lateinit var etBookTitle: TextView
    lateinit var etBookMaterial: TextView
    lateinit var bookIdioma: TextView
    lateinit var bookISBN: TextView
    lateinit var bookAutor: TextView
    lateinit var bookCDU: TextView
    lateinit var bookEdicao: TextView
    lateinit var bookPublicacao: TextView
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_detail)

        // Instância do Firebase
        fb = Firebase.firestore

        // Campos de detalhamento de material
        etBookTitle = findViewById(R.id.bookTitle)
        etBookMaterial = findViewById(R.id.bookMaterial)
        bookIdioma = findViewById(R.id.bookIdioma)
        bookISBN = findViewById(R.id.bookISBN)
        bookAutor = findViewById(R.id.bookAutor)
        bookCDU = findViewById(R.id.bookCDU)
        bookEdicao = findViewById(R.id.bookEdicao)
        bookPublicacao = findViewById(R.id.bookPublicacao)
        bookCapa = findViewById(R.id.bookCover)

        arrowBackButtonView = findViewById(R.id.userBookDetailArrowBack)
        arrowBackButtonView.setOnClickListener {
            startActivity(Intent(this, UserHome::class.java))
        }

        val x = getSharedPreferences("arquivo", MODE_PRIVATE)
        val bookId = x.getString("BOOK_ID", "default") ?: "default"

        // Chamada dos detalhes do material
        getBookDetails(bookId)

        val exemplares = mutableListOf<Exemplar>()

        recyclerView = findViewById<RecyclerView>(R.id.exemplaresRecyler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExemplaresAdapter(this, exemplares)

        // Chamada de exemplares
        loadExemplares(bookId, exemplares, recyclerView)

    }

    // Função para chamada da informações
    private fun getBookDetails(bookId:String) {

        fb.collection("materiais")
            .document(bookId)
            .get()
            .addOnSuccessListener { result ->
                etBookTitle.text = result.get("titulo").toString()
                etBookMaterial.text = result.get("material").toString()
                bookIdioma.text = result.get("idioma").toString()
                bookISBN.text = result.get("isbn").toString()
                bookAutor.text = result.get("autor").toString()
                bookCDU.text = result.get("cdu").toString()
                bookEdicao.text = result.get("edicao").toString()
                bookPublicacao.text = result.get("publicacao").toString()

                val capaBase64 = result.get("capa") as? String
                if (capaBase64 != null && capaBase64.isNotEmpty()) {
                    val bitmap = decodeBase64ToBitmap(capaBase64)
                    if (bitmap != null) {
                        bookCapa.setImageBitmap(bitmap)
                    }
                }
            }
    }
    private fun loadExemplares(bookId: String, exemplares: MutableList<Exemplar>, recycler: RecyclerView) {
        fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .get()
            .addOnSuccessListener { snapshot ->
                exemplares.clear()
                for (doc in snapshot) {
                    val id = doc.id
                    val suporte = doc.getString("suporte") ?: ""
                    val registro = doc.getString("registro") ?: ""
                    val disponibilidade = doc.getString("disponibilidade") ?: ""
                    val status = doc.getString("status") ?: ""
                    exemplares.add(Exemplar(id, suporte, registro, disponibilidade, status))
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
