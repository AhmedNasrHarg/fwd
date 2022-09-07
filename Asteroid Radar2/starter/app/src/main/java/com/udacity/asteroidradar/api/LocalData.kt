package com.udacity.asteroidradar.api

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Database(entities = arrayOf(Asteroid::class, PictureOfDay::class), version = 1)	//add other entities to the array		[reusable]
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao			//add other Daos like this

    companion object{
        @Volatile private var INSTANCE: AsteroidDatabase?=null
        fun getInstance(context: Context) : AsteroidDatabase{
            synchronized(this){
                var instance = INSTANCE
                if( instance == null ){
                    instance = Room.databaseBuilder(
                        context,
                        AsteroidDatabase::class.java, "asteroid-db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: Asteroid)

    @Insert
    fun insertAnAsteroid(asteroid: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMutableAsteroids( asteroids: MutableList<Asteroid>)

    @Delete
    fun deleteOldAsteroids(vararg oldAsteroid: Asteroid)

    @Query("Select * from Asteroid where closeApproachDate>= :today order by closeApproachDate")
    fun getAllAsteroids(today:String): MutableList<Asteroid>

    //image of Day
    @Insert
    fun insertImageOfDay(imageOfDay: PictureOfDay)

    @Delete
    fun deleteOldImage(imageOfDay: PictureOfDay)

    @Query("select * from PictureOfDay")
    fun getImageOfDay(): PictureOfDay

    @Query("delete from PictureOfDay")
    fun deleteOldImage()
}
