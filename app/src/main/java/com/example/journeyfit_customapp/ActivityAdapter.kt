package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView

//adapter class
class ActivityAdapter(private val data: List<Activity>) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>()  {
    //on create function is called
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //set layout inflater from main activity
        val layoutInflater = LayoutInflater.from(parent.context)
        //set view to layout_row.xml
        val view = layoutInflater
            .inflate(R.layout.recycler_view, parent, false) as View
        //return view
        return ViewHolder(view)
    }
    //get size of list
    override fun getItemCount() = data.size
    //add/remove data to list as the user scrolls
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //run bind function
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        //assign widgets to variables
        private val textType: TextView = v.findViewById(R.id.textActivityType)
        private val textDate: TextView = v.findViewById(R.id.textDate)
        private val textLoc: TextView = v.findViewById(R.id.textLocation)
        private val btnView = v.findViewById<Button>(R.id.buttonView)
        private val btnDelete = v.findViewById<Button>(R.id.buttonDelete)

        @SuppressLint("SetTextI18n")
        fun bind(item: Activity) {
            //set title
            textType.text = "Type: " + item.type
            //set subtitle
            textDate.text = "Date: "+item.date
            textLoc.text = "Location: "+item.location
            //when clicked
            btnView.setOnClickListener{
                Toast.makeText(ViewHolder(v).itemView.context, item.time, Toast.LENGTH_LONG).show()
            }
            btnDelete.setOnClickListener{
                Toast.makeText(ViewHolder(v).itemView.context, item.comments,Toast.LENGTH_LONG).show()
            }
        }
    }
}