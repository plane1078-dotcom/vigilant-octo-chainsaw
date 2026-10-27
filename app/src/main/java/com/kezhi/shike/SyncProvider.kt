package com.kezhi.shike

/**
 * Future cloud-sync seam. No network requests are made by this app today.
 * Implement this interface only after choosing a trusted backend and adding
 * authentication, encryption in transit, retry logic and conflict handling.
 */
interface SyncProvider {
    suspend fun upload(records: List<CheckInRecord>): SyncResult
    suspend fun download(): SyncDownloadResult
}

data class CheckInRecord(
    val id: String,
    val timestampEpochMillis: Long,
    val deviceId: String? = null
)

sealed class SyncResult {
    data object Success : SyncResult()
    data class Failure(val message: String) : SyncResult()
}

sealed class SyncDownloadResult {
    data class Success(val records: List<CheckInRecord>) : SyncDownloadResult()
    data class Failure(val message: String) : SyncDownloadResult()
}

/** Safe default: sync is deliberately disabled until a backend is configured. */
class NoOpSyncProvider : SyncProvider {
    override suspend fun upload(records: List<CheckInRecord>) =
        SyncResult.Failure("尚未配置云同步服务")

    override suspend fun download() =
        SyncDownloadResult.Failure("尚未配置云同步服务")
}
