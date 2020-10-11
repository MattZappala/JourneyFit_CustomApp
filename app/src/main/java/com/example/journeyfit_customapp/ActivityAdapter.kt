package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView


//adapter class
class ActivityAdapter(private var data: List<Activity>) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>()  {
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
        holder.bind(item,position)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        //assign widgets to variables
        private val textType: TextView = v.findViewById(R.id.textActivityType)
        private val textDate: TextView = v.findViewById(R.id.textDate)
        private val textLoc: TextView = v.findViewById(R.id.textLocation)
        private val btnView = v.findViewById<Button>(R.id.buttonView)
        private val btnDelete = v.findViewById<Button>(R.id.buttonDelete)

        @SuppressLint("SetTextI18n")
        fun bind(item: Activity, pos: Int) {
            val context = ViewHolder(v).itemView.context
            val db= DatabaseHandler(ViewHolder(v).itemView.context)
            //set title
            textType.text = item.type
            //set subtitle
            textDate.text = item.date
            textLoc.text = item.location
            //when clicked
            btnView.setOnClickListener{
                val intent = Intent(context,AddActivity::class.java)
                intent.putExtra("activity",item)
                context.startActivity(intent)
            }
            btnDelete.setOnClickListener{
                Toast.makeText(context,"Activity Deleted",Toast.LENGTH_LONG).show()
                db.deleteActivity(item)
                data = db.viewActivity()
                notifyDataSetChanged()
            }
        }
    }
}