package com.briviaclub.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.briviaclub.app.data.local.dao.ActivityDao
import com.briviaclub.app.data.local.dao.FilterDao
import com.briviaclub.app.data.local.dao.MatchDao
import com.briviaclub.app.data.local.dao.MessageDao
import com.briviaclub.app.data.local.dao.PaymentDao
import com.briviaclub.app.data.local.dao.ReportBlockDao
import com.briviaclub.app.data.local.dao.SubscriptionDao
import com.briviaclub.app.data.local.dao.SwipeDao
import com.briviaclub.app.data.local.dao.UserDao
import com.briviaclub.app.data.local.entity.ActivityEntity
import com.briviaclub.app.data.local.entity.MatchEntity
import com.briviaclub.app.data.local.entity.MessageEntity
import com.briviaclub.app.data.local.entity.PaymentEntity
import com.briviaclub.app.data.local.entity.ReportBlockEntity
import com.briviaclub.app.data.local.entity.SubscriptionEntity
import com.briviaclub.app.data.local.entity.SwipeEntity
import com.briviaclub.app.data.local.entity.UserEntity
import com.briviaclub.app.data.local.entity.UserFilterEntity

@Database(
    entities = [
        UserEntity::class,
        SwipeEntity::class,
        MatchEntity::class,
        MessageEntity::class,
        UserFilterEntity::class,
        SubscriptionEntity::class,
        PaymentEntity::class,
        ReportBlockEntity::class,
        ActivityEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun swipeDao(): SwipeDao
    abstract fun matchDao(): MatchDao
    abstract fun messageDao(): MessageDao
    abstract fun filterDao(): FilterDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun paymentDao(): PaymentDao
    abstract fun reportBlockDao(): ReportBlockDao
    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "brivia_club_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
