package cvut.fit.kot.data.repository

import cvut.fit.kot.data.remote.FileApi
import javax.inject.Inject

class FileRepository @Inject constructor (
    private val api: FileApi
) {
    suspend fun getAvatar(userId: Long): ByteArray? {
        val rsp = api.downloadAvatar(userId)
        return if (rsp.isSuccessful) rsp.body()?.bytes() else null
    }
}