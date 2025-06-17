package cvut.fit.kot.util

object JwtUtils {

    private fun decodeBase64Url(data: String): ByteArray =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            java.util.Base64.getUrlDecoder().decode(data)
        } else {
            android.util.Base64.decode(
                /* str  = */ data,
                /* flags= */ android.util.Base64.URL_SAFE or
                        android.util.Base64.NO_WRAP or
                        android.util.Base64.NO_PADDING
            )
        }

    fun userId(token: String): Long? = runCatching {
        val payload = token.split('.')[1]
        val json     = String(decodeBase64Url(payload), Charsets.UTF_8)
        org.json.JSONObject(json).getString("sub").toLong()
    }.getOrNull()
}