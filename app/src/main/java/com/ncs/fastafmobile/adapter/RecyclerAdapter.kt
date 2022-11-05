package com.ncs.fastafmobile.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ncs.fastafmobile.R
import com.ncs.fastafmobile.UserData

class RecyclerAdapter(): RecyclerView.Adapter<RecyclerAdapter.ViewHolder> (){

    lateinit var title1 : Array<String>
   lateinit var email1 : Array<String>
    lateinit var  hidden1 : Array<Int>


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
        if (hidden1[position]==1){
            holder.itemHidden.visibility= View.VISIBLE
            holder.itemLock.setImageResource( R.drawable.ic_baseline_lock_open_24)
        }else {
            holder.itemHidden.visibility= View.GONE
            holder.itemLock.setImageResource( R.drawable.ic_baseline_lock_24)
        }
        val initials = getInitials(title1[position])
        holder.itemImage.text = initials
    }

    fun getInitials(title: String ): String {
        val initials = StringBuffer("")
       val letters = title.split(" ")
       for (l in letters){
            initials.append(l.get(0))
       }
        return initials.toString()
    }

    fun setList(itemData : UserData){
      title1 = itemData.siteName
      email1 = itemData.userEmail
      hidden1 = itemData.newRequest
      notifyDataSetChanged()

 }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: TextView
        var itemTitle: TextView
        var itemEmail: TextView
        var itemHidden: TextView
        var itemLock: ImageView

        init {
            itemImage=itemView.findViewById(R.id.iv_image)
            itemTitle=itemView.findViewById(R.id.tv_name)
            itemEmail = itemView.findViewById(R.id.tv_email)
            itemHidden= itemView.findViewById(R.id.tv_hidden_auth_request)
            itemLock= itemView.findViewById(R.id.lock)
        }
    }



}