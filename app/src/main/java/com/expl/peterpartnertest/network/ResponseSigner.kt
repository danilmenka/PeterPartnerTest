package com.expl.peterpartnertest.network

import androidx.lifecycle.MutableLiveData
import com.expl.peterpartnertest.utilits.APP_ACTIVITY
import org.json.JSONObject
import retrofit2.Response

object ResponseSigner{
    suspend fun <T:Any> startResponse(
        liveData: MutableLiveData<Event<T>>,
        request: suspend () -> Response<T>
    ) {
        liveData.postValue(Event.loading())
        try {
            val result : ResponseWrapper<T> = safeApiResult(request)
            val data : T?
            when(result) {
                is ResponseWrapper.Success -> {
                    data = result.data
                    liveData.postValue(Event.success(data))
                }
                is ResponseWrapper.Error -> {
                    liveData.postValue(Event.error(result.exception))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            liveData.postValue(Event.error(APP_ACTIVITY.getString(com.expl.peterpartnertest.R.string.internet_error_string)))
        }
    }

    private suspend fun <R: Any> safeApiResult(call: suspend ()-> Response<R>) : ResponseWrapper<R> {
        var errorMessage = APP_ACTIVITY.getString(com.expl.peterpartnertest.R.string.internet_error_string)
        val response = call.invoke()
        if (response.isSuccessful) return ResponseWrapper.Success(response.body()!!)
        else {
            try {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                errorMessage = jsonObj.getString("error")
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        return ResponseWrapper.Error(errorMessage)
    }

}
