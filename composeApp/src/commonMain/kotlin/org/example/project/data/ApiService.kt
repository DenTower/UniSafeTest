package org.example.project.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.example.project.data.dto.AllShopListsResponse
import org.example.project.data.dto.CreateShopListResponse
import org.example.project.data.dto.SuccessResponse
import org.example.project.data.dto.RegistrationResponse
import org.example.project.data.dto.GetShopListResponse
import org.example.project.util.NetworkError
import org.example.project.util.Result

class ApiService(
    private val httpClient: HttpClient
) {
    private suspend inline fun <reified T> safeCall(
        execute: () -> HttpResponse
    ): Result<T, NetworkError> {
        val response = try {
            execute()
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val body = response.body<T>()
                Result.Success(body)
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            409 -> Result.Error(NetworkError.CONFLICT)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun addToShopList(
        listId: Long,
        value: String,
        n: Int
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/AddToShoppingList") {
            parameter("id", listId)
            parameter("value", value)
            parameter("n", n)
        }
    }

    suspend fun updateItemShopList(
        itemId: Long,
        value: String,
        n: Int
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/UpdateShoppingList") {
            parameter("id", itemId)
            parameter("value", value)
            parameter("n", n)
        }
    }

    suspend fun removeFromList(
        itemId: Long
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/RemoveFromList") {
            parameter("item_id", itemId)
        }
    }

    suspend fun crossItemOff(
        itemId: Long
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/CrossItOff") {
            parameter("id", itemId)
        }
    }

    suspend fun moveItem(
        fromId: Long,
        toId: Long
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/MoveItem") {
            parameter("listId", fromId)
            parameter("toId", toId)
        }
    }

    suspend fun removeShopList(
        listId: Long
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/RemoveShoppingList") {
            parameter("list_id", listId)
        }
    }

    suspend fun createShopList(
        key: String,
        name: String
    ): Result<CreateShopListResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/CreateShoppingList") {
            parameter("key", key)
            parameter("name", name)
        }
    }

    suspend fun register(): Result<RegistrationResponse, NetworkError> = safeCall {
        httpClient.post("/shopping/v4/Registration")
    }

    suspend fun auth(
        key: String
    ): Result<SuccessResponse, NetworkError> = safeCall {
        httpClient.get("/shopping/v4/Authentication") {
            parameter("key", key)
        }
    }

    suspend fun getAllShopLists(
        key: String
    ): Result<AllShopListsResponse, NetworkError> = safeCall {
        httpClient.get("/shopping/v4/GetAllMyShopLists") {
            parameter("key", key)
        }
    }

    suspend fun getShopList(
        listId: Long
    ): Result<GetShopListResponse, NetworkError> = safeCall {
        httpClient.get("/shopping/v4/GetShoppingList") {
            parameter("list_id", listId)
        }
    }
}