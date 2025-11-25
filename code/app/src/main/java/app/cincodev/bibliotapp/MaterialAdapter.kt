package app.cincodev.bibliotapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
            .inflate(R.layout.item_book_list_seach_user, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val item = lista[position]

        holder.nome.text = item.nome
        holder.tipo.text = "Tipo: ${item.tipo}"
        holder.codigo.text = "Código: ${item.codigo}"
        holder.autor.text = "Autor: ${item.autor}"

        // --- SAFE GLIDE ---
        if (!item.imagemUrl.isNullOrEmpty()) {
            Glide.with(holder.img.context)
                .load(item.imagemUrl)
                .placeholder(R.drawable.book_01)
                .error(R.drawable.book_01)
                .into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.book_01)
        }

        // --- CLICK EVENT ---
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val prefs = context.getSharedPreferences("arquivo", Context.MODE_PRIVATE)
            prefs.edit().putString("BOOK_ID", item.id).apply()

            context.startActivity(Intent(context, UserBookDetail::class.java))
        }
    }


    override fun getItemCount() = lista.size
}

