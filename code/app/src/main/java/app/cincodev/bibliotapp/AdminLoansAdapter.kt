package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminLoansAdapter(
    private val dataSet: MutableList<Loan>
) : RecyclerView.Adapter<AdminLoansAdapter.ViewHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val registro: TextView = view.findViewById(R.id.emprestimoRegistro)
        val matricula: TextView = view.findViewById(R.id.emprestimoMatricula)
        val atraso: TextView = view.findViewById(R.id.emprestimoAtraso)
        val status: TextView = view.findViewById(R.id.emprestimoStatus)

        val menu: ImageView = view.findViewById(R.id.menuEmprestimoItem)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.registro.text = item.registro
        viewHolder.matricula.text = item.matricula
        viewHolder.atraso.text = item.atraso
        viewHolder.status.text = item.status

        viewHolder.menu.setOnClickListener { anchorView ->
            showPopupMenu(anchorView, position)
        }
    }

    override fun getItemCount() = dataSet.size

    private fun showPopupMenu(anchorView: View, position: Int) {

        val context = anchorView.context
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.custom_menu_emprestimo, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 12f
        }

        popupView.findViewById<Button>(R.id.btnReceber).setOnClickListener {
            confirmarRecebimento(context, dataSet[position].registro)
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.btnGerarMulta).setOnClickListener {
            val intent = Intent(context, AdminFee::class.java)
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, -85, 0)
    }

    private fun confirmarRecebimento(context: android.content.Context, loadId: String) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_loan_received, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            db
                .collection("emprestimos")
                .document(loadId).delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Emprestimo fechado com sucesso", Toast.LENGTH_LONG).show()
                    dataSet.removeIf { loan ->  loan.registro == loadId }

                    this.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Ocorreu um erro ao fechar o emprestimo. Tente novamente", Toast.LENGTH_LONG).show()
                }

            dialog.dismiss()
        }

        dialog.show()
    }

}
