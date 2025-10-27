package app.cincodev.bibliotapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminLoanSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_loan_search)

        val dataset = arrayOf(
            Loan("2357911", "245099", "0 dias", "Em dia"),
            Loan("2357912", "245100", "3 dias", "Atrasado")
        )

        val customAdapter = AdminLoansAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerEmprestimo)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
}