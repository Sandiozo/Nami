package com.example.mob3000.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        User::class,
        DefaultAff::class,
        CustomAff::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun affirmationDao(): AffirmationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mob3000_db"
                )
                    .fallbackToDestructiveMigration(false)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am steady even when life feels uncertain')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I breathe with intention and focus')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I respond to stress with clarity')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I trust myself to navigate challenges')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am grounded in this moment')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I release what I cannot control')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am becoming more resilient every day')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I deserve calm and balance')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I can choose a slower pace')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am learning to be kinder to myself')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I let go of tension that does not serve me')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am patient with my own progress')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I can make space for peace')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am moving toward a healthier mindset')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I give myself permission to feel and heal')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I trust the process of growth')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I face discomfort with courage')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I can anchor myself through breathing')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I acknowledge my progress, even if it’s small')")
                            db.execSQL("INSERT INTO default_aff (text) VALUES ('I am capable of creating inner stability')")

                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
