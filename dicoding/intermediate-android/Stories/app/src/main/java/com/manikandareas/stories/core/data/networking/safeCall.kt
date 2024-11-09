package com.manikandareas.stories.core.data.networking

import com.manikandareas.stories.core.domain.util.NetworkError
import com.manikandareas.stories.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        println(e.printStackTrace())
        return Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        println(e.printStackTrace())
        return Result.Error(NetworkError.SERIALIZATION)
    }  catch (e: Exception) {
        println(e.printStackTrace())
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}