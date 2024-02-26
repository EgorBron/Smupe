package net.blusutils.smupe.data.image_sources.models.sources

enum class AuthType {
    None,
    AuthHeader,
    AuthHeaderBearer,
    AuthHeaderToken,
    AuthHeaderOther, // see "defining custom auth in API-DEF"
    JSON
}
