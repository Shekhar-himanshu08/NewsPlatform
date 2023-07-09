package com.example.newsplatform

//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//
//class NewsListAdapter: RecyclerView.Adapter<> {
//}
//
//class NewsViewHolder(itemView: View): ViewHolder

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.newsplatform.R

class NewsListAdapter(private val listener: NewsItemClicked) : RecyclerView.Adapter<NewsViewHolder>() {
    private val items:ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        val viewHolder=NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem=items[position]
        holder.titleView.text=currentItem.title
        holder.media.text= currentItem.source.name
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)
    }
    fun updateNews(updateNews:ArrayList<News>){
        items.clear()
        items.addAll(updateNews)
        notifyDataSetChanged()
    }
}
class NewsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val titleView: TextView =itemView.findViewById<TextView>(R.id.title)
    val image: ImageView =itemView.findViewById<ImageView>(R.id.image)
    val media: TextView =itemView.findViewById<TextView>(R.id.name)
}
interface NewsItemClicked{




    fun onItemClicked(item:News)
}