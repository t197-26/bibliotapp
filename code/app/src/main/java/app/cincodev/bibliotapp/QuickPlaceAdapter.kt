package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuickPlaceAdapter(private val dataSet: Array<QuickPlace>) :
    RecyclerView.Adapter<QuickPlaceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icone: ImageView = view.findViewById(R.id.quickPlaceBookIcone)
        val sala: TextView = view.findViewById(R.id.quickPlaceSala)
        val dataHora: TextView = view.findViewById(R.id.quickPlaceDataHora)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.quick_place_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.icone.setImageResource(dataSet[position].icone)
        viewHolder.sala.text = dataSet[position].sala
        viewHolder.dataHora.text = dataSet[position].dataHora
    }

    override fun getItemCount() = dataSet.size
}