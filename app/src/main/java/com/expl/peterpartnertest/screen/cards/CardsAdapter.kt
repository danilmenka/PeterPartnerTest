package com.expl.peterpartnertest.screen.cards

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.expl.peterpartnertest.R
import com.expl.peterpartnertest.model.User
import java.lang.Exception


class CardsAdapter(val clickChoice: (index:String) -> Unit) : RecyclerView.Adapter<CardsAdapter.MainHolder>() {

    private var userList = emptyList<User>()
    private var imgList = emptyList<Int>()
    private var mCurrentIndex = 0

    class MainHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleCard: TextView = view.findViewById(R.id.card_title)
        val titleImg: ImageView = view.findViewById(R.id.card_title_img)
        val isCurrentCard: ImageView = view.findViewById(R.id.card_img_is_current)
    }

    override fun onViewAttachedToWindow(holder: MainHolder) {
        holder.itemView.setOnClickListener {
            clickChoice(userList[holder.layoutPosition].card_number)
        }
    }

    override fun onViewDetachedFromWindow(holder: MainHolder) {
        holder.itemView.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return MainHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.titleCard.text = userList[position].card_number
        try {
            holder.titleImg.setImageResource(imgList[position])
        }
        catch (e:Exception){}
        if (position==mCurrentIndex) holder.isCurrentCard.visibility = View.VISIBLE
            else holder.isCurrentCard.visibility = View.INVISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<User>, listImg: List<Int>, currentIndex:Int) {
        userList = list
        imgList = listImg
        notifyDataSetChanged()
        mCurrentIndex = currentIndex
    }
}