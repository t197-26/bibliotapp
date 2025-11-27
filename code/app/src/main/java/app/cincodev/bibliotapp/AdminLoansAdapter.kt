package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class AdminLoansAdapter(
    private val loans: List<Loan>, // Recebe a lista da classe Loan definida acima
    private val onItemClick: (Loan) -> Unit // Clique opcional (ex: para devolver)
) : RecyclerView.Adapter<AdminLoansAdapter.LoanViewHolder>() {

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    class LoanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Certifique-se que o layout item_loan.xml tem esses IDs
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle1: TextView = itemView.findViewById(R.id.tvSubtitle1)
        val tvSubtitle2: TextView = itemView.findViewById(R.id.tvSubtitle2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loan, parent, false)
        return LoanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val loan = loans[position]

        // Formata datas (trata nulos)
        val dataEmprestimo = if (loan.loanDate != null) sdf.format(loan.loanDate.toDate()) else "--"
        val dataDevolucao = if (loan.returnDate != null) sdf.format(loan.returnDate.toDate()) else "--"

        // --- AQUI ESTAVA O ERRO (CORRIGIDO) ---
        // Usamos os nomes corretos definidos na classe Loan
        holder.tvTitle.text = loan.bookTitle
        holder.tvSubtitle1.text = "Usuário: ${loan.userName}"
        holder.tvSubtitle2.text = "Devolução: $dataDevolucao"

        holder.itemView.setOnClickListener {
            onItemClick(loan)
        }
    }

    override fun getItemCount(): Int = loans.size
}