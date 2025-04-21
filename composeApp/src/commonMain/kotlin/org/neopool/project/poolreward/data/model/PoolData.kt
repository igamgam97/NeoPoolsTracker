package org.neopool.project.poolreward.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PoolResponse(
    @SerialName("data")
    val data: PoolData,
    @SerialName("error")
    val error: String?,
    @SerialName("success")
    val success: Boolean,
    @SerialName("updated")
    val updated: Long
)

@Serializable
data class PoolData(
    @SerialName("hashrate1d")
    val hashrate1d: Long,
    @SerialName("blockReward")
    val blockReward: Double,
    @SerialName("height")
    val height: Long,
    @SerialName("feetype")
    val feeType: String,
    @SerialName("name")
    val name: String,
    @SerialName("tag")
    val tag: String
) 