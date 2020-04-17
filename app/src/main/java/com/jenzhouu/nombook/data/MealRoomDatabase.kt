package com.jenzhouu.nombook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.utils.DateTypeConverter
import com.jenzhouu.nombook.utils.IngredientsConverter

@Database(
    entities = [Meal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class, IngredientsConverter::class)
abstract class MealRoomDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    //abstract fun favoritesDao(): FavoritesDao
    //abstract fun IngredientsDao(): IngredientsDao

    companion object {
        @Volatile
        private var instance: MealRoomDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: getDatabase(context).also { instance = it}
        }

        fun getDatabase(context: Context) = Room.databaseBuilder(context,
            MealRoomDatabase::class.java, "meal_database.db")
            .build()
    }
}
