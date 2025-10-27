package app.cincodev.bibliotapp

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

class AdminLoansAdapter(
    private val dataSet: Array<Loan>
) : RecyclerView.Adapter<AdminLoansAdapter.ViewHolder>() {

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
            Toast.makeText(context, "Recebendo...", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.btnGerarMulta).setOnClickListener {
            val intent = Intent(context, AdminFee::class.java)
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, -85, 0)
    }
}
