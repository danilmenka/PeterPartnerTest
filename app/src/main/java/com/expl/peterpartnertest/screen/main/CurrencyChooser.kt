package com.expl.peterpartnertest.screen.main

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.expl.peterpartnertest.R
import com.expl.peterpartnertest.utilits.APP_ACTIVITY

object CurrencyChooser {
    private var firstStart = true
    private var currentIndex: Int? = null
    fun choseCurrency(currencySlotList: List<CurrencySlot>,getChooseStr:(String,String)->Unit){
                currencySlotList.forEachIndexed { index,it ->
            it.cardView.setOnClickListener {
                firstStart = false
                currentIndex = index
                changeView(currencySlotList,index)
                getChooseStr(currencySlotList[index].textViewSymbol.text.toString(),
                    currencySlotList[index].textViewValute.text.toString())
            }
        }
        if (firstStart){
            firstStart = false
            currentIndex = 0
        }
            currentIndex?.let { changeView(currencySlotList, it)}
                getChooseStr(currencySlotList[currentIndex!!].textViewSymbol.text.toString(),
                    currencySlotList[currentIndex!!].textViewValute.text.toString())
    }
    private fun changeView(currencySlotList: List<CurrencySlot>,chooseIndex:Int){
        currencySlotList.forEachIndexed { mIndex, mIt ->
            if (mIndex==chooseIndex){
                mIt.cardView.setCardBackgroundColor(ContextCompat.getColor(APP_ACTIVITY, R.color.figma_blue))
                mIt.textViewSymbol.setTextColor(ContextCompat.getColor(APP_ACTIVITY, R.color.figma_white))
                mIt.textViewValute.setTextColor(ContextCompat.getColor(APP_ACTIVITY, R.color.white))
            } else {
                mIt.cardView.setCardBackgroundColor(ContextCompat.getColor(APP_ACTIVITY, R.color.white))
                mIt.textViewSymbol.setTextColor(ContextCompat.getColor(APP_ACTIVITY, R.color.figma_text_grey))
                mIt.textViewValute.setTextColor(ContextCompat.getColor(APP_ACTIVITY, R.color.figma_text_grey))
            }
        }
    }
}

data class CurrencySlot(val cardView: CardView,val textViewSymbol: TextView,val textViewValute:TextView)