package com.example.spotifymood.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

private const val MARKET_DEFAULT = "from_token"

interface SpotifyApiService {
    @GET("v1/me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") bearerToken: String,
        @Query("market") market: String = MARKET_DEFAULT
    ): CurrentlyPlayingResponse?

    @GET("v1/audio-features/{id}")
    suspend fun getAudioFeatures(
        @Header("Authorization") bearerToken: String,
        @Path("id") trackId: String
    ): AudioFeaturesResponse
}

@JsonClass(generateAdapter = true)
data class CurrentlyPlayingResponse(
    @Json(name = "item") val item: TrackItem?,
)

@JsonClass(generateAdapter = true)
data class TrackItem(
    @Json(name = "id") val id: String?,
    @Json(name = "uri") val uri: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "artists") val artists: List<ArtistItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ArtistItem(
    @Json(name = "name") val name: String?
)

@JsonClass(generateAdapter = true)
data class AudioFeaturesResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "energy") val energy: Double?,
    @Json(name = "valence") val valence: Double?
)
