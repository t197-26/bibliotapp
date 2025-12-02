package app.cincodev.bibliotapp

import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class AdminLoanSearch : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    lateinit var searchBar: EditText
    lateinit var recyclerView: RecyclerView

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val loans: MutableList<Loan> = mutableListOf()
    private var filteredLoans: MutableList<Loan> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_loan_search)

        arrowBackButtonView = findViewById(R.id.adminLoanSearchArrowBack)
        searchBar = findViewById(R.id.search_input)
        recyclerView = findViewById(R.id.recyclerEmprestimo)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        searchBar.addTextChangedListener { text ->
            val textString = text.toString()
            if (textString.isEmpty()) {
                filteredLoans.clear()

                recyclerView.swapAdapter(AdminLoansAdapter(loans), false)
            } else {
                filteredLoans = loans.filter { loan ->
                    loan.registro.lowercase()
                        .contains(textString.lowercase()) || loan.matricula.contains(textString)
                }.toMutableList()

                recyclerView.swapAdapter(AdminLoansAdapter(filteredLoans), false)
            }
        }
    }

    override fun onStart() {
        loans.clear()

        db
            .collection("emprestimos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val todayDate = Date()
                    val returnDate = (document["devolver_em"] as Timestamp).toDate()
                    val daysOverdue = ChronoUnit.DAYS.between(
                        todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        returnDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    )

                    loans.add(
                        Loan(
                            document.id,
                            document["users_id"] as String,
                            (if (daysOverdue < 0) "0" else daysOverdue.toString()) + " dia(s)",
                            if (returnDate.after(todayDate)) "Em dia" else "Atrasado"
                        )
                    )
                }

                val customAdapter = AdminLoansAdapter(loans)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = customAdapter
            }

        super.onStart()
    }
}