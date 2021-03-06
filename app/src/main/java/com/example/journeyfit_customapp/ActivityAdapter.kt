package com.example.journeyfit_customapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

//To get colours from https://www.schemecolor.com/sample?getcolor=00C44F

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

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v), View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        //assign widgets to variables
        private val textType: TextView = v.findViewById(R.id.textActivityType)
        private val textDate: TextView = v.findViewById(R.id.textDate)
        private val textLoc: TextView = v.findViewById(R.id.textLocation)
        private val textFeel: TextView = v.findViewById(R.id.textFeel)
        private  lateinit var db: DatabaseHandler
        private lateinit var mItem: Activity

        @SuppressLint("SetTextI18n")
        fun bind(item: Activity, pos: Int) {
            //make calling the context easier
            val context = ViewHolder(v).itemView.context
            db= DatabaseHandler(context)
            //make item data a global variable
            mItem = item
            //set colour of row object
            if(item.status == 0){
               v.setBackgroundResource(R.drawable.list_red)
            } else {
               v.setBackgroundResource(R.drawable.list_green)
            }
            //set title
            textType.text = item.type
            //set subtitle
            textDate.text = item.date
            textLoc.text = item.location
            textFeel.text = "Rating: "+ item.feel
            //when clicked
            v.setOnCreateContextMenuListener(this)
        }

        //when a row is clicked
        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            //set title of context menu
            p0?.setHeaderTitle("Action Menu");
            //set values in item list
            val itemView = p0?.add(0, v.id, 0, "View");
            val itemDelete = p0?.add(0, v.id, 0, "Delete");
            //set listener for each item in menu
            itemView?.setOnMenuItemClickListener(this)
            itemDelete?.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            if (p0?.title == "View"){
                //if menu item title is view send to add activity page and pass the activity data to it for editing
                val intent = Intent(ViewHolder(v).itemView.context,AddActivity::class.java)
                intent.putExtra("activity",mItem)
                ViewHolder(v).itemView.context.startActivity(intent)
            }else if (p0?.title == "Delete"){
                //if title is delete
                Log.i("Testing", "Delete")
                //if id item exists
                mItem.id?.let { db.deleteActivity(it) }
                //delete from database
                data = db.viewActivity("SELECT * FROM ${DatabaseHandler.TABLE_ACTIVITY}")
                //toast to say item deleted
                Toast.makeText(ViewHolder(v).itemView.context,"Activity Deleted",Toast.LENGTH_LONG).show()
                //notify that the data set has changed and update list
                notifyDataSetChanged()
            }
            return true
        }
    }
}