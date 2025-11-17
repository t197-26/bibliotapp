package app.cincodev.bibliotapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserHome : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        // Instância do Firebase
        fb = Firebase.firestore

        // Array de livros emprestados
        val dataset = mutableListOf<QuickBook>()

        // Declaração de adapter da lista de livros emprestados
        val quickBookAdapter = QuickBookAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.quickBookRecycler)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = quickBookAdapter

        // Chamada de livros emprestados do banco de dados
        getBooks(dataset, quickBookAdapter)

        val dataset_2 = arrayOf(
            QuickPlace(R.drawable.ic_quickplace, "Sala q12", "24/09/2025 11:00\n24/09/2025 11:00"),
            QuickPlace(R.drawable.book_01, "Gerencimento de TI", "Disponível"),
            QuickPlace(R.drawable.book_03, "Sistemas de Informação", "3 dias")
        )

        val quickPlaceAdapter = QuickPlaceAdapter(dataset_2)

        val recyclerView2: RecyclerView = findViewById(R.id.quickPlaceRecycler)
        recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = quickPlaceAdapter

        val btnDigitalizacoes: ImageView = findViewById(R.id.btnDIgitalizacoes)
        val btnReservarEspaco: ImageView = findViewById(R.id.btnReservarEspaco)
        val fabChatbot: FloatingActionButton = findViewById(R.id.fab)

        btnDigitalizacoes.setOnClickListener { openDigitalizacoes() }
        btnReservarEspaco.setOnClickListener { openReservarEspaco() }
        fabChatbot.setOnClickListener { openChatbot() }
    }

    private fun openDigitalizacoes() {
        val intent = Intent(this, UserDigitalizations::class.java)
        startActivity(intent)
    }

    private fun openReservarEspaco() {
        val intent = Intent(this, ReserveSpace::class.java)
        startActivity(intent)
    }

    private fun openChatbot() {
        val intent = Intent(this, UserChatbot::class.java)
        startActivity(intent)
    }

    private fun getBooks(dataset: MutableList<QuickBook>, adapter: QuickBookAdapter) {
        fb.collection("materiais")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val titulo = document.get("titulo").toString()
                    val devolucao = document.get("devolucao").toString()
                    val capaBase64 = document.get("capa").toString()

                    // Decodifica a imagem Base64
                    val capaBitmap = decodeBase64ToBitmap(capaBase64)

                    // Adiciona o livro à lista
                    val quickBook = QuickBook(id, capaBitmap, titulo, devolucao)

                    dataset.add(quickBook)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            // Remove o prefixo data URL se existir
            val cleanBase64 = if (base64String.contains(",")) {
                base64String.substring(base64String.indexOf(",") + 1)
            } else {
                base64String
            }

            val decodedBytes = Base64.decode(cleanBase64, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            Log.e("UserHome", "Erro ao decodificar Base64", e)
            null
        }
    }
}