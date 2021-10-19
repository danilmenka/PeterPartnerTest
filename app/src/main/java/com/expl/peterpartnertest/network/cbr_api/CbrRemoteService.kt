package com.expl.peterpartnertest.network.cbr_api

import com.expl.peterpartnertest.model.CbrResponse
import retrofit2.Response
import retrofit2.http.GET

interface CbrRemoteService {
    @GET("daily_json.js")
    suspend fun getRate(
    ): Response<CbrResponse>
}