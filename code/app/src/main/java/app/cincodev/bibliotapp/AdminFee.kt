package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AdminFee : AppCompatActivity() {

    private lateinit var closeButton: Button
    private lateinit var arrowBackButtonView: ImageButton

    private lateinit var textDelayDay: TextView
    private lateinit var textFeeAmount: TextView
    private lateinit var textReturnDate: TextView

    private var loanId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_fee)

        closeButton = findViewById(R.id.closeButtonAdminFee)
        arrowBackButtonView = findViewById(R.id.feeArrowBack)

        textDelayDay = findViewById(R.id.text_delay_day)
        textFeeAmount = findViewById(R.id.fee_amount)
        textReturnDate = findViewById(R.id.text_return_date)

        arrowBackButtonView.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        closeButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        loanId = intent.getStringExtra("loanId")

        if (loanId != null) {
            carregarDadosDoEmprestimo(loanId!!)
        }
    }

    private fun carregarDadosDoEmprestimo(id: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("emprestimos")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->

                val devolverEm = doc.getTimestamp("devolver_em")
                val emprestadoEm = doc.getTimestamp("emprestado_em")

                if (devolverEm != null && emprestadoEm != null) {
                    calcularMulta(devolverEm, emprestadoEm)
                }
            }
    }

    private fun calcularMulta(devolverEm: Timestamp, emprestadoEm: Timestamp) {

        val dataDevolver = devolverEm.toDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val hoje = LocalDate.now()

        val diasAtraso = ChronoUnit.DAYS.between(dataDevolver, hoje).toInt()

        val diasMulta = if (diasAtraso > 0) diasAtraso else 0

        val multa = diasMulta * 1.50

        textDelayDay.text = "$diasMulta dias"
        textFeeAmount.text = "R$ ${String.format("%.2f", multa)}"
        textReturnDate.text = formatarDataLegivel(dataDevolver)
    }

    private fun formatarDataLegivel(data: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")
        return data.format(formatter)
    }
}
