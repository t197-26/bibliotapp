package app.cincodev.bibliotapp

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.edit

class QuickBookAdapter(private val dataSet: List<QuickBook>) :
    RecyclerView.Adapter<QuickBookAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val capa: ImageView = view.findViewById(R.id.quickBookCapa)
        val titulo: TextView = view.findViewById(R.id.quickBookTitulo)
        val devolucao: TextView = view.findViewById(R.id.quickBookDevolucao)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.quick_book_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val book = dataSet[position]

        if (book.capa != null) {
            viewHolder.capa.setImageBitmap(book.capa)
        } else {
            viewHolder.capa.setImageResource(R.drawable.book_01)
        }

        viewHolder.titulo.text = book.titulo
        viewHolder.devolucao.text = book.devolucao

        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.itemView.context
            val intent = Intent(context, UserBookDetail::class.java)

            // Pass the document ID to the detail activity
            //intent.putExtra("BOOK_ID", book.id)

            val x = context.getSharedPreferences("arquivo",MODE_PRIVATE)
            x.edit {
                putString("BOOK_ID", book.id)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size

}