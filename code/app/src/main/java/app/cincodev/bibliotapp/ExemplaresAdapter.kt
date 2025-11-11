package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ExemplaresAdapter(
    private val context: Context,
    private val exemplares: Array<Exemplar>
) : RecyclerView.Adapter<ExemplaresAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val suporte: TextView = view.findViewById(R.id.bookExemplarSuporte)
        val registro: TextView = view.findViewById(R.id.bookExemplarRegistro)
        val disponibilidade: TextView = view.findViewById(R.id.bookExemplarDisponibilidade)
        val status: TextView = view.findViewById(R.id.bookExemplarStatus)
        val menuButton: ImageView = view.findViewById(R.id.bookExemplarMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exemplar, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exemplar = exemplares[position]

        holder.suporte.text = exemplar.suporte
        holder.registro.text = exemplar.registro
        holder.disponibilidade.text = exemplar.disponibilidade
        holder.status.text = exemplar.status

        holder.menuButton.setOnClickListener {
            showPopupMenu(holder.menuButton, position)
        }
    }

    override fun getItemCount(): Int = exemplares.size
    private fun showPopupMenu(anchorView: ImageView, position: Int) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.custom_menu_exemplar, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.elevation = 16f

        popupView.findViewById<Button>(R.id.btnReservar).setOnClickListener {
            confirmarReserva(context, position)
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.btnDigitalizar).setOnClickListener {
            popupWindow.dismiss()
            context.startActivity(Intent(context, UserDigitalizationOrder::class.java))
        }

        popupView.findViewById<Button>(R.id.btnVerNoMapa).setOnClickListener {
            popupWindow.dismiss()
            context.startActivity(Intent(context, UserSpaceMap::class.java))
        }

        popupWindow.showAsDropDown(anchorView, -150, -350)
    }


    private fun confirmarReserva(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_reservation_book, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            context.startActivity(Intent(context, UserProcessingBooking::class.java))
            exemplares[position].status = "Emprestado"
            notifyItemChanged(position)
            dialog.dismiss()
        }

        dialog.show()
    }
}
