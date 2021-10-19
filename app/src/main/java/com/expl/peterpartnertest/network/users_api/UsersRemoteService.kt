package com.expl.peterpartnertest.network.users_api

import com.expl.peterpartnertest.model.Users
import retrofit2.Response
import retrofit2.http.GET

interface UsersRemoteService {
    @GET("users.json")
    suspend fun getUsers(
    ): Response<Users>
}