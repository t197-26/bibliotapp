package app.cincodev.bibliotapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class UserHome : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore

    lateinit var welcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val prefs = getSharedPreferences("bibliotapp_shared_preferences", MODE_PRIVATE)
        val matricula = prefs.getString("matricula", null).toString()

        // Instância do Firebase
        fb = Firebase.firestore

        welcome = findViewById(R.id.boasVindasHomeUser)
        loadUserName(matricula)

        // Array de livros emprestados
        val dataset = mutableListOf<QuickBook>()

        // Declaração de adapter da lista de livros emprestados
        val quickBookAdapter = QuickBookAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.quickBookRecycler)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = quickBookAdapter

        // Chamada de livros emprestados do banco de dados
        getBooks(dataset, quickBookAdapter, matricula)

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
        var btnBuscarLivro: ImageView = findViewById(R.id.btnBuscarLivro)
        val fabChatbot: FloatingActionButton = findViewById(R.id.fab)

        btnDigitalizacoes.setOnClickListener { openDigitalizacoes() }
        btnReservarEspaco.setOnClickListener { openReservarEspaco() }
        btnBuscarLivro.setOnClickListener { openBuscarLivro() }
        fabChatbot.setOnClickListener { openChatbot() }
    }

    private fun openBuscarLivro(){
        val intent = Intent(this, UserBookSearch::class.java)
        startActivity(intent)
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



    private fun loadUserName(matricula: String) {
        fb.collection("users")
            .document(matricula)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val nome = doc.getString("name") ?: "Usuário"

                    // Incrementa no TextView (não substitui)
                    welcome.text = welcome.text.toString() + " " + nome
                }
            }
            .addOnFailureListener { e ->
                Log.i("UserHome", "Erro ao carregar nome do usuário", e)
            }
    }

    private fun getBooks(dataset: MutableList<QuickBook>, adapter: QuickBookAdapter, matricula: String?) {
        // O getBook é um getEmprestimos

        // Buscar empréstimos do usuário ativo
        fb.collection("emprestimos")
            .whereEqualTo("users_id", matricula)
            .get()
            .addOnSuccessListener { emprestimosSnapshot ->
                if (emprestimosSnapshot.isEmpty) {
                    adapter.notifyDataSetChanged()
                    Log.d("debugUserHome", "emprestimosSnapshot está vazio.")
                    return@addOnSuccessListener
                }

                for (emprestimo in emprestimosSnapshot) {
                    // Calcula os dias restantes ate a data de devolução
                    val devolver_em = emprestimo.getTimestamp("devolver_em")
                    var diferenca_dias : Long = 0
                    if (devolver_em != null) {
                        diferenca_dias = getDaysFromToday(devolver_em)
                        Log.d("FirestoreDays", "Difference in days = $diferenca_dias")
                    } else {
                        Log.d("FirestoreDays", "Timestamp field is null")
                    }

                    // Busca cada material emprestado e encapsula para o dataset
                    fb.collection("materiais")
                        .document(emprestimo.get("materiais_id").toString())
                        .get()
                        .addOnSuccessListener { material ->
                            val id = material.id
                            Log.i("UserHome", "material.id: " + id)

                            val titulo = material.get("titulo").toString()

                            val devolucao = "$diferenca_dias dia(s) restante(s)"
                            //val devolucao = material.get("devolucao").toString()
                            val capaBase64 = material.get("capa").toString()

                            // Decodifica a imagem Base64
                            val capaBitmap = decodeBase64ToBitmap(capaBase64)

                            // Adiciona o livro ao dataset
                            val quickBook = QuickBook(id, capaBitmap, titulo, devolucao)

                            dataset.add(quickBook)

                            adapter.notifyDataSetChanged()
                        }
                }
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
            Log.i("UserHome", "Erro ao decodificar Base64", e)
            null
        }
    }

    fun getDaysFromToday(timestamp: Timestamp): Long {
        val zoneId = ZoneId.systemDefault() // or ZoneId.of("America/Fortaleza"), por ex    .

        // Date from Firestore
        val targetDate = timestamp.toDate()
            .toInstant()
            .atZone(zoneId)
            .toLocalDate()

        // Today
        val today = LocalDate.now(zoneId)

        // If you want NEGATIVE when the date is in the past:
        //  - date in past -> negative
        //  - date in future -> positive
        return ChronoUnit.DAYS.between(today, targetDate)
    }
}