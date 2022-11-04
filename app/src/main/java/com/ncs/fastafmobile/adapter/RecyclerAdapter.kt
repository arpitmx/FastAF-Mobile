package com.ncs.fastafmobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ncs.fastafmobile.R

class RecyclerAdapter(title: Array<String>, email: Array<String>, hidden: String, images: Array<Int>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder> (){

    var title1 =title
    var email1= email
    var  hidden1 = hidden
    var images1 = images




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return title1.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = title1[position]
        holder.itemEmail.text = email1[position]
        holder.itemHidden.text = hidden1
        holder.itemImage.setImageResource(images1[position])
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