package com.example.spotifyanalyzer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.spotifyanalyzer.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private val sharedViewModel: SharedViewModel by viewModels()

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataManager = DataManager(this)
        loadSavedData()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, UserStatsFragment())
            .commit()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.nav_tracks -> TracksFragment()
                R.id.nav_artists -> ArtistsFragment()
                R.id.nav_albums -> AlbumsFragment()
                else -> UserStatsFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }

        findViewById<Button>(R.id.loadDataButton).setOnClickListener {
            openFilePicker()
        }

        findViewById<Button>(R.id.clearDataButton).setOnClickListener {
            clearData()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Выберите JSON-файл"), FILE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val jsonText = reader.use { it.readText() }
                    sharedViewModel.loadData(jsonText)
                    sharedViewModel.analyzeSpotifyHistory()

                    dataManager.saveHistoryData(sharedViewModel.historyData.value ?: emptyMap())

                    Toast.makeText(this, "Данные загружены успешно!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Ошибка при загрузке JSON: ${e.message}")
                    Toast.makeText(this, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadSavedData() {
        val savedHistory = dataManager.loadHistoryData()
        if (savedHistory.isNotEmpty()) {
            sharedViewModel.updateHistoryData(savedHistory)
            sharedViewModel.analyzeSpotifyHistory()
            Toast.makeText(this, "Данные загружены из памяти", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearData() {
        sharedViewModel.clearData()
        Toast.makeText(this, "Данные очищены!", Toast.LENGTH_SHORT).show()
        dataManager.clearHistoryData()
    }
}
