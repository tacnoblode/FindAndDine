package com.howellkeatingfonseca.findanddine.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.howellkeatingfonseca.findanddine.data.model.RatedRestaurant
import com.howellkeatingfonseca.findanddine.data.model.Rating
import com.howellkeatingfonseca.findanddine.data.model.RatingSortOption
import com.howellkeatingfonseca.findanddine.data.model.Restaurant

class FindAndDineDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "find_and_dine.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE restaurants (
                id TEXT PRIMARY KEY,
                name TEXT,
                address TEXT,
                city TEXT,
                cuisine TEXT,
                latitude REAL,
                longitude REAL,
                hours TEXT,
                is_favourite INTEGER
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE ratings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id TEXT UNIQUE,
                stars INTEGER,
                visit_date INTEGER,
                note TEXT,
                personal_rank INTEGER
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS ratings")
        db.execSQL("DROP TABLE IF EXISTS restaurants")
        onCreate(db)
    }

    fun insertRestaurant(restaurant: Restaurant) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", restaurant.id)
            put("name", restaurant.name)
            put("address", restaurant.address)
            put("city", restaurant.city)
            put("cuisine", restaurant.cuisine)
            put("latitude", restaurant.latitude)
            put("longitude", restaurant.longitude)
            put("hours", restaurant.hours)
            put("is_favourite", if (restaurant.isFavourite) 1 else 0)
        }
        db.insertWithOnConflict("restaurants", null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
    }

    fun updateRestaurant(restaurant: Restaurant) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", restaurant.name)
            put("address", restaurant.address)
            put("city", restaurant.city)
            put("cuisine", restaurant.cuisine)
            put("latitude", restaurant.latitude)
            put("longitude", restaurant.longitude)
            put("hours", restaurant.hours)
            put("is_favourite", if (restaurant.isFavourite) 1 else 0)
        }
        db.update(
            "restaurants",
            values,
            "id = ?",
            arrayOf(restaurant.id)
        )
        db.close()
    }

    fun getRestaurant(id: String): Restaurant? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM restaurants WHERE id = ?", arrayOf(id))
        val restaurant = if (cursor.moveToFirst()) readRestaurant(cursor, 0) else null
        cursor.close()
        db.close()
        return restaurant
    }

    fun getAllRestaurants(): List<Restaurant> {
        val restaurants = mutableListOf<Restaurant>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM restaurants ORDER BY name COLLATE NOCASE", null)
        while (cursor.moveToNext()) {
            restaurants.add(readRestaurant(cursor, 0))
        }
        cursor.close()
        db.close()
        return restaurants
    }

    fun deleteRestaurant(id: String) {
        getRatingForRestaurant(id)?.let { deleteRating(it.id) }
        val db = writableDatabase
        db.delete("restaurants", "id = ?", arrayOf(id))
        db.close()
    }

    fun insertRating(restaurantId: String, stars: Int, visitDate: Long, note: String) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ratings", null)
        cursor.moveToFirst()
        val rank = cursor.getInt(0) + 1
        cursor.close()
        val values = ContentValues().apply {
            put("restaurant_id", restaurantId)
            put("stars", stars)
            put("visit_date", visitDate)
            put("note", note)
            put("personal_rank", rank)
        }
        db.insert("ratings", null, values)
        db.close()
    }

    fun updateRating(id: Int, stars: Int, visitDate: Long, note: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("stars", stars)
            put("visit_date", visitDate)
            put("note", note)
        }
        db.update(
            "ratings",
            values,
            "id = ?",
            arrayOf(id.toString())
        )
        db.close()
    }

    fun getRatingForRestaurant(restaurantId: String): Rating? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ratings WHERE restaurant_id = ?", arrayOf(restaurantId))
        val rating = if (cursor.moveToFirst()) readRating(cursor, 0) else null
        cursor.close()
        db.close()
        return rating
    }

    fun getRatedRestaurants(
        starsFilter: Int? = null,
        sortOption: RatingSortOption = RatingSortOption.PERSONAL_RANK
    ): List<RatedRestaurant> {
        val ratedRestaurants = mutableListOf<RatedRestaurant>()
        val db = readableDatabase

        val where = if (starsFilter != null) "WHERE ratings.stars = ?" else ""
        val orderBy = when (sortOption) {
            RatingSortOption.PERSONAL_RANK -> "ratings.personal_rank"
            RatingSortOption.STARS_HIGH_TO_LOW -> "ratings.stars DESC, ratings.personal_rank"
            RatingSortOption.STARS_LOW_TO_HIGH -> "ratings.stars, ratings.personal_rank"
            RatingSortOption.NEWEST_VISIT -> "ratings.visit_date DESC, ratings.personal_rank"
            RatingSortOption.OLDEST_VISIT -> "ratings.visit_date, ratings.personal_rank"
            RatingSortOption.NAME -> "restaurants.name COLLATE NOCASE, ratings.personal_rank"
        }

        val cursor = db.rawQuery(
            """
            SELECT restaurants.*, ratings.* FROM ratings
            INNER JOIN restaurants ON ratings.restaurant_id = restaurants.id
            $where
            ORDER BY $orderBy
            """.trimIndent(),
            starsFilter?.let { arrayOf(it.toString()) }
        )
        while (cursor.moveToNext()) {
            ratedRestaurants.add(
                RatedRestaurant(
                    readRestaurant(cursor, 0),
                    readRating(cursor, 9)
                )
            )
        }
        cursor.close()
        db.close()
        return ratedRestaurants
    }

    fun moveRating(id: Int, newRank: Int) {
        val db = writableDatabase
        val cursor = db.rawQuery(
            "SELECT personal_rank, (SELECT COUNT(*) FROM ratings) FROM ratings WHERE id = ?",
            arrayOf(id.toString())
        )
        if (cursor.moveToFirst()) {
            val oldRank = cursor.getInt(0)
            val targetRank = newRank.coerceIn(1, cursor.getInt(1))
            if (targetRank < oldRank) {
                db.execSQL(
                    "UPDATE ratings SET personal_rank = personal_rank + 1 WHERE personal_rank >= ? AND personal_rank < ?",
                    arrayOf(targetRank, oldRank)
                )
            } else if (targetRank > oldRank) {
                db.execSQL(
                    "UPDATE ratings SET personal_rank = personal_rank - 1 WHERE personal_rank > ? AND personal_rank <= ?",
                    arrayOf(oldRank, targetRank)
                )
            }
            db.execSQL("UPDATE ratings SET personal_rank = ? WHERE id = ?", arrayOf(targetRank, id))
        }
        cursor.close()
        db.close()
    }

    fun deleteRating(id: Int) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT personal_rank FROM ratings WHERE id = ?", arrayOf(id.toString()))
        if (cursor.moveToFirst()) {
            val rank = cursor.getInt(0)
            db.delete("ratings", "id = ?", arrayOf(id.toString()))
            db.execSQL(
                "UPDATE ratings SET personal_rank = personal_rank - 1 WHERE personal_rank > ?",
                arrayOf(rank)
            )
        }
        cursor.close()
        db.close()
    }

    private fun readRestaurant(cursor: Cursor, start: Int): Restaurant {
        return Restaurant(
            cursor.getString(start),
            cursor.getString(start + 1),
            cursor.getString(start + 2),
            cursor.getString(start + 3),
            cursor.getString(start + 4),
            cursor.getDouble(start + 5),
            cursor.getDouble(start + 6),
            cursor.getString(start + 7),
            cursor.getInt(start + 8) == 1
        )
    }

    private fun readRating(cursor: Cursor, start: Int): Rating {
        return Rating(
            cursor.getInt(start),
            cursor.getString(start + 1),
            cursor.getInt(start + 2),
            cursor.getLong(start + 3),
            cursor.getString(start + 4),
            cursor.getInt(start + 5)
        )
    }
}
