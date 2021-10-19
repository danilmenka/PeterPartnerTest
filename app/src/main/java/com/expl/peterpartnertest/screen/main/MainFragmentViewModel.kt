package com.expl.peterpartnertest.screen.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expl.peterpartnertest.model.CbrResponse
import com.expl.peterpartnertest.model.TransactionHistory
import com.expl.peterpartnertest.model.User
import com.expl.peterpartnertest.model.Users
import com.expl.peterpartnertest.network.Event
import com.expl.peterpartnertest.network.NetworkRepository
import com.expl.peterpartnertest.utilits.VALUTE_EUR
import com.expl.peterpartnertest.utilits.VALUTE_GBP
import com.expl.peterpartnertest.utilits.VALUTE_RUB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor
    (private val networkRepository: NetworkRepository) : ViewModel(){

    val usersLiveData = MutableLiveData<Event<Users>>()

    val cbrResponseLiveData = MutableLiveData<Event<CbrResponse>>()

    var currentUserView = MutableLiveData<User>()

    fun getUsers(){
        this.viewModelScope.launch(Dispatchers.IO) {
            networkRepository.getCategories(usersLiveData)
        }
    }

    fun setCurrentUser(index: Int){
        currentUserView.value = usersLiveData.value?.data?.users?.get(index)
    }

    fun getCbr(){
        this.viewModelScope.launch(Dispatchers.IO) {
            networkRepository.getCbrResponse(cbrResponseLiveData)
        }
    }

    fun chooseCurrency(currency:String){
       when (currency){
            VALUTE_GBP -> currentUserView.value = recalculateCurrency(cbrResponseLiveData.value!!.data!!.valute.gbp.value)
            VALUTE_EUR -> currentUserView.value = recalculateCurrency(cbrResponseLiveData.value!!.data!!.valute.eur.value)
            VALUTE_RUB -> currentUserView.value = recalculateCurrency(1.0)
        }
    }

    private fun recalculateCurrency(multiplier:Double):User{
        val usdRub = cbrResponseLiveData.value!!.data!!.valute.usd.value
        val user: User = currentUserView.value!!
        val transactionHistory = mutableListOf<TransactionHistory>()
        user.transaction_history.forEach {
            val currentAmount = it.amount*usdRub/multiplier
          transactionHistory.add(TransactionHistory(
              it.title,
              it.icon_url,
              it.date,
              it.amount,
              currentAmount.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
          ))
        }
        val userBalance = user.balance*usdRub/multiplier
        return User(user.card_number,
            user.type,
            user.cardholder_name,
            user.valid,
            user.balance,
            userBalance.toBigDecimal().setScale(2, RoundingMode.UP).toDouble(),
            transactionHistory
            )
    }

    }