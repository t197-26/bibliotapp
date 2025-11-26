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
    private val lista: List<Material>
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.material_image)
        val nome = view.findViewById<TextView>(R.id.material_name)
        val tipo = view.findViewById<TextView>(R.id.material_type)
        val codigo = view.findViewById<TextView>(R.id.material_code)
        val autor = view.findViewById<TextView>(R.id.material_writer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book_list_search_user, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val item = lista[position]

        holder.nome.text = item.titulo
        holder.tipo.text = "Tipo: ${item.material}"
        holder.codigo.text = "ISBN: ${item.isbn}"
        holder.autor.text = "Autor: ${item.autor}"

        // --- BASE64 IMAGE ---
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



    override fun getItemCount() = lista.size
}

