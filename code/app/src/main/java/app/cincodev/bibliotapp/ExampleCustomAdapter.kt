package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ExampleCustomAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<ExampleCustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var key: TextView
        lateinit var values : TextView
        lateinit var btn: Button

        init {
            // Define click listener for the ViewHolder's View
            key = view.findViewById(R.id.textViewKey)
            values = view.findViewById(R.id.textViewValue)
            btn = view.findViewById(R.id.button2)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itemkeyvaluepair, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.values.text = "values:${dataSet[position]}"
        viewHolder.key.text = "key:${dataSet[position]}"

        viewHolder.btn.setOnClickListener {
            Toast.makeText(viewHolder.itemView.context
                ,viewHolder.key.text
                , Toast.LENGTH_SHORT).show()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}