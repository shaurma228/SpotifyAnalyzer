package com.example.spotifyanalyzer.viewmodel

import androidx.lifecycle.ViewModel
import org.json.JSONArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class TrackData(
    val name: String,
    val artist: String,
    var plays: Int = 0,
    var timePlayed: Long = 0
)

data class ArtistData(
    val name: String,
    var plays: Int = 0,
    var timePlayed: Long = 0
)

data class AlbumData(
    val name: String,
    val artist: String,
    var trackPlayed: Int = 0,
    var timePlayed: Long = 0
)

data class History(
    val ts: String,
    val msPlayed: Long,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val trackUrl: String
)

class SharedViewModel : ViewModel() {
    private val _trackData = MutableLiveData<Map<String, TrackData>>()
    val trackData: LiveData<Map<String, TrackData>> get() = _trackData

    private val _artistData = MutableLiveData<Map<String, ArtistData>>()
    val artistData: LiveData<Map<String, ArtistData>> get() = _artistData

    private val _albumData = MutableLiveData<Map<String, AlbumData>>()
    val albumData: LiveData<Map<String, AlbumData>> get() = _albumData

    private val _historyData = MutableLiveData<Map<String, History>>()
    val historyData: LiveData<Map<String, History>> get() = _historyData

    fun updateTracksData(newTracks: Map<String, TrackData>) {
        _trackData.value = newTracks
    }

    fun updateArtistsData(newArtists: Map<String, ArtistData>) {
        _artistData.value = newArtists
    }

    fun updateAlbumsData(newAlbums: Map<String, AlbumData>) {
        _albumData.value = newAlbums
    }

    fun updateHistoryData(newHistory: Map<String, History>) {
        _historyData.value = newHistory
    }

    private val _timeCounter = MutableLiveData<Long>()
    val timeCounter: LiveData<Long> get() = _timeCounter

    private val _listensCounter = MutableLiveData<Int>()
    val listensCounter: LiveData<Int> get() = _listensCounter

    init {
        _timeCounter.value = 0L
        _listensCounter.value = 0
    }

    fun updateTimeCounter(newTime: Long) {
        _timeCounter.value = newTime
    }

    fun updateListensCounter(newCount: Int) {
        _listensCounter.value = newCount
    }

    fun loadData(jsonText: String) {
        val jsonArray = JSONArray(jsonText)
        val newHistory = _historyData.value.orEmpty().toMutableMap()

        for (i in 0 until jsonArray.length()) {
            val entry = jsonArray.getJSONObject(i)
            val ts = entry.optString("ts")

            if (newHistory.containsKey(ts)) continue

            val track = entry.optString("master_metadata_track_name")
            val artist = entry.optString("master_metadata_album_artist_name")
            val album = entry.optString("master_metadata_album_album_name")
            val msPlayed = entry.optLong("ms_played", 0)
            val trackUrl = entry.optString("spotify_track_uri")

            if (track.isNotEmpty() && artist.isNotEmpty() && album.isNotEmpty()) {
                newHistory[ts] = History(ts, msPlayed, track, artist, album, trackUrl)
            }
        }

        _historyData.value = newHistory
    }

    fun analyzeSpotifyHistory() {
        val trackData = mutableMapOf<String, TrackData>()
        val artistData = mutableMapOf<String, ArtistData>()
        val albumData = mutableMapOf<String, AlbumData>()

        var timeCounter = 0L
        var listensCounter = 0

        _historyData.value?.forEach { (_, history) ->
            val msPlayed = history.msPlayed
            val trackName = history.trackName
            val artist = history.artistName
            val album = history.albumName

            if (trackName.isNotEmpty() && artist.isNotEmpty() && album.isNotEmpty()) {
                val trackKey = "$trackName | $artist"
                trackData.getOrPut(trackKey) { TrackData(trackName, artist) }.apply {
                    plays++
                    timePlayed += msPlayed
                }

                artistData.getOrPut(artist) { ArtistData(artist) }.apply {
                    plays++
                    timePlayed += msPlayed
                }

                val albumKey = "$album | $artist"
                albumData.getOrPut(albumKey) { AlbumData(album, artist) }.apply {
                    trackPlayed++
                    timePlayed += msPlayed
                }

                timeCounter += msPlayed
                listensCounter++
            }
        }

        _trackData.value = trackData
        _artistData.value = artistData
        _albumData.value = albumData

        updateTimeCounter(timeCounter)
        updateListensCounter(listensCounter)
    }


    fun clearData() {
        _historyData.value = emptyMap()
        _trackData.value = emptyMap()
        _artistData.value = emptyMap()
        _albumData.value = emptyMap()

        _timeCounter.value = 0L
        _listensCounter.value = 0
    }
}