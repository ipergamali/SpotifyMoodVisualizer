package com.example.spotifymood.data

import com.example.spotifymood.api.AudioFeaturesResponse
import com.example.spotifymood.api.SpotifyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpotifyMoodRepository(
    private val api: SpotifyApiService
) {
    suspend fun loadCurrentMood(accessToken: String): MoodSnapshot? = withContext(Dispatchers.IO) {
        val bearer = "Bearer $accessToken"
        val currentlyPlaying = api.getCurrentlyPlaying(bearer) ?: return@withContext null
        val trackId = currentlyPlaying.item?.id ?: return@withContext null

        val audioFeatures: AudioFeaturesResponse = api.getAudioFeatures(bearer, trackId)
        val energy = audioFeatures.energy ?: return@withContext null
        val valence = audioFeatures.valence ?: return@withContext null

        MoodSnapshot(
            trackId = trackId,
            trackName = currentlyPlaying.item.name.orEmpty(),
            artistName = currentlyPlaying.item.artists.firstOrNull()?.name.orEmpty(),
            energy = energy,
            valence = valence
        )
    }
}

data class MoodSnapshot(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val energy: Double,
    val valence: Double
)
