package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity

class EditExemplaresAdapter(
    private val context: Context,
    private val exemplares: List<Exemplar>
) : RecyclerView.Adapter<EditExemplaresAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val suporte: TextView = view.findViewById(R.id.bookExemplarSuporte)
        val registro: TextView = view.findViewById(R.id.bookExemplarRegistro)
        val disponibilidade: TextView = view.findViewById(R.id.bookExemplarDisponibilidade)
        val status: TextView = view.findViewById(R.id.bookExemplarStatus)
        val editarExemplar: ImageView = view.findViewById(R.id.editarExemplar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edit_exemplar, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exemplar = exemplares[position]

        holder.suporte.text = exemplar.suporte
        holder.registro.text = exemplar.registro
        holder.disponibilidade.text = exemplar.disponibilidade
        holder.status.text = exemplar.status

        holder.editarExemplar.setOnClickListener {
            val intent = Intent(context, AdminEditExemplar::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = exemplares.size

}
