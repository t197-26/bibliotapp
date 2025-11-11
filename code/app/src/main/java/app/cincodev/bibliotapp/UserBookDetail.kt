package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
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
    lateinit var etBookTitle: TextView
    lateinit var etBookMaterial: TextView
    lateinit var bookIdioma: TextView
    lateinit var bookISBN: TextView
    lateinit var bookAutor: TextView
    lateinit var bookCDU: TextView
    lateinit var bookEdicao: TextView
    lateinit var bookPublicacao: TextView

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

        arrowBackButtonView = findViewById(R.id.userBookDetailArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Chamada dos detalhes do material
        getBookDetails();

        val exemplares = mutableListOf<Exemplar>(
            Exemplar("Impresso", "235711", "Em 5 dia(s)", "Emprestado"),
            Exemplar("Digital", "998877", "Imediata", "Disponível"),
            Exemplar("Impresso", "112233", "Indisponível", "Indisponível"),
            Exemplar("Impresso", "556644", "Consultar balcão", "Emprestado"),
            Exemplar("Impresso", "774411", "Em 1 dia(s)", "Disponível"),
            Exemplar("Digital", "889900", "Indisponível", "Indisponível")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.exemplaresRecyler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExemplaresAdapter(this, exemplares)

    }

    // Função para chamada da informações
    private fun getBookDetails() {

        fb.collection("materiais")
            .document("default")
            .get()
            .addOnSuccessListener { result ->

                etBookTitle.setText(result.get("titulo").toString())
                etBookMaterial.setText(result.get("material").toString())
                bookIdioma.setText(result.get("idioma").toString())
                bookISBN.setText(result.get("isbn").toString())
                bookAutor.setText(result.get("autor").toString())
                bookCDU.setText(result.get("cdu").toString())
                bookEdicao.setText(result.get("edicao").toString())
                bookPublicacao.setText(result.get("publicacao").toString())

            }
    }
}
