package com.manikandareas.devent.data.datasources.remote

import android.content.Context
import com.manikandareas.devent.R
import com.manikandareas.devent.domain.models.EventResponse
import com.manikandareas.devent.utils.ApiException
import com.manikandareas.devent.utils.NoInternetException
import com.manikandareas.devent.utils.RemoteHelper
import com.manikandareas.devent.utils.UnknownException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RemoteDatasource(
    private val apiService: ApiService,
    private val context : Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchEvent(active: Int, keyword: String? = null): EventResponse = withContext(
        ioDispatcher
    ) {
        try {
            if (!RemoteHelper.hasInternetConnection(context)) {
                throw NoInternetException(context.getString(R.string.no_internet))
            }
            val response = apiService.getEvent(active, keyword)
            if (response.error == true) {
                throw ApiException(response.message ?: context.getString(R.string.error_code_default))
            }
            response
        } catch (e: HttpException) {
            val code = e.code()
            val errorMessage = RemoteHelper.remoteErrorMessage(context, code)
            throw ApiException(errorMessage)
        } catch (e: IOException) {
            throw NoInternetException(context.getString(R.string.no_internet))
        } catch (e: Exception) {
            throw UnknownException(e.message ?: context.getString(R.string.error_code_default))
        }
    }
}