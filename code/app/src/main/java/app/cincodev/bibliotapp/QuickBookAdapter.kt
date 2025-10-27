package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuickBookAdapter(private val dataSet: Array<QuickBook>) :
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
        viewHolder.capa.setImageResource(dataSet[position].capa)
        viewHolder.titulo.text = dataSet[position].titulo
        viewHolder.devolucao.text = dataSet[position].devolucao
    }

    override fun getItemCount() = dataSet.size

}