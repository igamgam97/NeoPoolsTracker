package org.neopool.project.poolreward.data.repository

import com.codingfeline.buildkonfigsample.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import org.neopool.project.poolreward.data.model.PoolResponse

interface PoolRepository {
    suspend fun getPoolData(): Result<PoolResponse>
}

class PoolRepositoryImpl(
    private val client: HttpClient,
) : PoolRepository {
    companion object {
        private const val BASE_URL = "https://api.neopool.com/xapi/v1"
    }

    override suspend fun getPoolData(): Result<PoolResponse> = try {
        val response = client.get("$BASE_URL/pool/hashrate") {
            headers {
                append("Authorization", BuildKonfig.POOL_REWARD_AUTH_TOKEN)
            }
        }
        val responseBody = response.body<PoolResponse>()
        if (responseBody.success) {
            Result.success(responseBody)
        } else {
            Result.failure(IllegalStateException("Error: ${responseBody.error}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}