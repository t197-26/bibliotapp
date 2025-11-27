package app.cincodev.bibliotapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MaterialAdapter(
    private val fullList: MutableList<Material>
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    private val filteredList = mutableListOf<Material>()

    init {
        filteredList.addAll(fullList)
    }

    inner class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.material_image)
        val nome: TextView = view.findViewById(R.id.material_name)
        val tipo: TextView = view.findViewById(R.id.material_type)
        val codigo: TextView = view.findViewById(R.id.material_code)
        val autor: TextView = view.findViewById(R.id.material_writer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_list_search_user, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val item = filteredList[position]

        holder.nome.text = item.titulo
        holder.tipo.text = "Tipo: ${item.material}"
        holder.codigo.text = "ISBN: ${item.isbn}"
        holder.autor.text = "Autor: ${item.autor}"

        if (!item.capa.isNullOrEmpty()) {
            try {
                val bytes = android.util.Base64.decode(item.capa, android.util.Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.img.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.img.setImageResource(R.drawable.book_01)
            }
        } else {
            holder.img.setImageResource(R.drawable.book_01)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val prefs = context.getSharedPreferences("arquivo", Context.MODE_PRIVATE)
            prefs.edit().putString("BOOK_ID", item.id).apply()

            context.startActivity(Intent(context, UserBookDetail::class.java))
        }
    }

    override fun getItemCount() = filteredList.size
    fun atualizarLista(novaLista: List<Material>) {
        fullList.clear()
        fullList.addAll(novaLista)

        filteredList.clear()
        filteredList.addAll(novaLista)

        notifyDataSetChanged()
    }

    fun filtrar(texto: String) {
        val query = texto.lowercase()

        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(fullList)
        } else {
            filteredList.addAll(
                fullList.filter { item ->
                    item.titulo.lowercase().contains(query) ||
                            item.autor.lowercase().contains(query)
                }
            )
        }

        notifyDataSetChanged()
    }
}
