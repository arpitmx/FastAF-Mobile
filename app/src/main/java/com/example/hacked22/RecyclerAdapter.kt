package com.example.hacked22

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rv.R

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var title = arrayOf("Dropbox","Slack","Facebook","Github","Gmail","Twitter","Linkedin")

    private var email= arrayOf("kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com")

    private  var hidden = "New auth requested"

    private  val images = intArrayOf(R.drawable.dropbox,R.drawable.slack,R.drawable.facebook,R.drawable.github,R.drawable.gmail,R.drawable.twitter,R.drawable.linkdin)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemEmail.text = email[position]
        holder.itemHidden.text = hidden
        holder.itemImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return title.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemEmail: TextView
        var itemHidden: TextView

        init {
            itemImage=itemView.findViewById(R.id.iv_image)
            itemTitle=itemView.findViewById(R.id.tv_name)
            itemEmail = itemView.findViewById(R.id.tv_email)
            itemHidden= itemView.findViewById(R.id.tv_hidden_auth_request)
        }
    }

//    fun showHide(view:View) {
//        view.visibility = if (view.visibility == View.VISIBLE){
//            View.INVISIBLE
//        } else{
//            View.VISIBLE
//        }
//    }
}