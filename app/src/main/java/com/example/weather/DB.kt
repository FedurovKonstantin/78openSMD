package com.example.weather

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = [Location::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}


@Dao
interface LocationDao {
    @Query("SELECT * FROM location WHERE name like :name")
    fun getLocationById(name: String): List<Location>

    @Insert
    fun insert(location: Location)
}

@Entity
data class Location(
    @PrimaryKey val id: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "name") val name: String,
)