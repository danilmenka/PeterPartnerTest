package com.expl.peterpartnertest.network

import androidx.lifecycle.MutableLiveData
import com.expl.peterpartnertest.model.CbrResponse
import com.expl.peterpartnertest.model.Users
import com.expl.peterpartnertest.network.cbr_api.CbrRemoteService
import com.expl.peterpartnertest.network.users_api.UsersRemoteService
import javax.inject.Inject

class NetworkRepository @Inject constructor (private val userRemoteService: UsersRemoteService,
                                             private val cbrRemoteService: CbrRemoteService) {

    suspend fun getCategories(categoriesLiveData: MutableLiveData<Event<Users>>){
        ResponseSigner.startResponse(categoriesLiveData){
            userRemoteService.getUsers()
        }
    }

    suspend fun getCbrResponse(categoriesLiveData: MutableLiveData<Event<CbrResponse>>){
        ResponseSigner.startResponse(categoriesLiveData){
            cbrRemoteService.getRate()
        }
    }

}