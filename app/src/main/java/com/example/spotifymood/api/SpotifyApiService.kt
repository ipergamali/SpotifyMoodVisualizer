package com.example.spotifymood.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

private const val MARKET_DEFAULT = "from_token"

interface SpotifyApiService {
    @GET("/v1/me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") bearerToken: String,
        @Query("market") market: String = MARKET_DEFAULT
    ): CurrentlyPlayingResponse?

    @GET("/v1/audio-features/{id}")
    suspend fun getAudioFeatures(
        @Header("Authorization") bearerToken: String,
        @Path("id") trackId: String
    ): AudioFeaturesResponse
}

data class CurrentlyPlayingResponse(
    val item: TrackItem?
)

data class TrackItem(
    val id: String?,
    val name: String?,
    val artists: List<ArtistItem> = emptyList()
)

data class ArtistItem(
    val name: String?
)

data class AudioFeaturesResponse(
    val id: String?,
    val energy: Double?,
    val valence: Double?
)
