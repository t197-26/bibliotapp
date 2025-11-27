package app.cincodev.bibliotapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminLoanSearch : AppCompatActivity() {

    private lateinit var arrowBackButtonView: ImageButton
    private lateinit var adapter: AdminLoansAdapter
    private val loansList = mutableListOf<Loan>()
    private val filteredList = mutableListOf<Loan>()

    // Instância do Firebase
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_loan_search)
        // ^ Verifique se o nome do seu layout é esse mesmo.
        // Se for activity_admin_book_search, altere aqui.

        setupViews()
        fetchLoansFromFirebase()
    }

    private fun setupViews() {
        // Botão Voltar
        arrowBackButtonView = findViewById(R.id.SpaceBookingSearchArrowBack) // Ajuste o ID se necessário
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa o Adapter
        adapter = AdminLoansAdapter(filteredList) { loan ->
            // Ação ao clicar no item (Ex: Devolver livro)
            Toast.makeText(this, "Empréstimo selecionado: ${loan.bookTitle}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        // Barra de Pesquisa
        val searchEditText = findViewById<EditText>(R.id.etPesquisa)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
    }

    private fun fetchLoansFromFirebase() {
        // Busca na coleção "loans" (ou "emprestimos", dependendo de como você criou no Firebase)
        db.collection("emprestimos")
            .get()
            .addOnSuccessListener { documents ->
                loansList.clear()
                for (document in documents) {
                    val loan = document.toObject(Loan::class.java)
                    loan.id = document.id
                    loansList.add(loan)
                }
                // Atualiza a lista visual
                filter("")
            }
            .addOnFailureListener { exception ->
                Log.w("AdminLoanSearch", "Erro ao buscar empréstimos", exception)
                Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filter(text: String) {
        filteredList.clear()
        if (text.isEmpty()) {
            filteredList.addAll(loansList)
        } else {
            val query = text.lowercase()
            for (item in loansList) {
                if (item.bookTitle.lowercase().contains(query) ||
                    item.userName.lowercase().contains(query)) {
                    filteredList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }
}