package app.cincodev.bibliotapp

import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdminBookSearchAdapter(
    private val context: Context,
    private val listaOriginal: MutableList<Material>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AdminBookSearchAdapter.ViewHolder>() {

    private var listaFiltrada = listaOriginal.toMutableList()

    interface OnItemClickListener {
        fun onEditClick(material: Material)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_book_list_search_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val material = listaFiltrada[position]

        holder.name.text = material.titulo
        holder.type.text = material.material
        holder.code.text = material.cdu
        holder.writer.text = material.autor

        if (material.capa.isNotEmpty()) {
            Glide.with(context)
                .asBitmap()
                .load(Base64.decode(material.capa, Base64.DEFAULT))
                .placeholder(R.drawable.book_02)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.book_02)
        }

        holder.itemView.setOnClickListener {
            listener.onEditClick(material)
        }

        holder.editBtn.setOnClickListener {
            listener.onEditClick(material)
        }
    }

    override fun getItemCount(): Int = listaFiltrada.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.material_image)
        val editBtn: ImageView = itemView.findViewById(R.id.imageView8)
        val name: TextView = itemView.findViewById(R.id.material_name)
        val type: TextView = itemView.findViewById(R.id.material_type)
        val code: TextView = itemView.findViewById(R.id.material_code)
        val writer: TextView = itemView.findViewById(R.id.material_writer)
    }

    fun filtrar(texto: String) {
        val query = texto.lowercase()

        listaFiltrada = if (query.isEmpty()) {
            listaOriginal.toMutableList()
        } else {
            listaOriginal.filter { item ->
                item.titulo.lowercase().contains(query) ||
                        item.autor.lowercase().contains(query)
            }.toMutableList()
        }

        notifyDataSetChanged()
    }
}
