# 🎧 Spotify Mood Visualizer

> A dynamic KDE Plasma 6 widget that visualizes the *mood* of your currently playing Spotify track — transforming energy and emotion into motion and color.  
> Developed with QML, Python, and the Spotify Web API.

---

## 🌈 Overview

**Spotify Mood Visualizer** brings your music to life!  
It connects to the Spotify Web API, reads the *valence* (happiness) and *energy* of your current song, and visualizes them through smooth animated waves and gradients that react to the music.

| Feature | Description |
|----------|--------------|
| 🎵 Real-time track info | Displays song title and artist |
| 🌊 Reactive animation | Waves move faster or slower depending on energy |
| 🎨 Mood-based colors | Warm tones for happy songs, cool tones for melancholic ones |
| 🔁 Manual refresh | Update instantly with one click |
| 🪄 Minimal QML design | Uses Plasma 6 components and soft transitions |

---

## 📱 Android ViewModel & Repository Παράδειγμα

Αν τα πεδία *energy* και *valence* επιστρέφουν συνεχώς `0.0`, το πρόβλημα συνήθως είναι ότι η κλήση API δεν ζητά τα **audio features** του κομματιού. 
Το ακόλουθο παράδειγμα Kotlin (συμβατό με Android Studio, Retrofit 2.11 και Kotlin Coroutines 1.8) δείχνει πώς μπορείς να αντλήσεις τις σωστές τιμές και να τις εμφανίσεις σε ένα `ViewModel`.

```kotlin
// build.gradle (module)
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
}
```

```kotlin
// Δημιούργησε το Retrofit service.
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.spotify.com/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

val api = retrofit.create(SpotifyApiService::class.java)

// Πέρασε το στο repository και στο ViewModel.
val repository = SpotifyMoodRepository(api)
val viewModel = MoodViewModel(repository)

// Όπου χρειάζεται, κάλεσε:
viewModel.refresh(accessToken = "BQD...")
```

Οι `data class` συνοδεύονται από `@JsonClass(generateAdapter = true)` ώστε ο Moshi να αναλύει με ακρίβεια τα πεδία `energy` και `valence`. Επιπλέον, το repository χρησιμοποιεί εφεδρική ανάγνωση από το `uri` του κομματιού όταν το `id` λείπει (π.χ. για local αρχεία), αποφεύγοντας να επιστρέφει 0.0.

Οι βασικές κλάσεις βρίσκονται στους φακέλους `app/src/main/java/com/example/spotifymood/`.
