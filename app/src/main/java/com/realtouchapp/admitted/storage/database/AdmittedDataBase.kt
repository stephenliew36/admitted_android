package com.realtouchapp.admitted.storage.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.storage.database.dao.*
import com.realtouchapp.admitted.storage.database.entity.KeyValueData


/*
    作者: RobertChou
    時間: 2023/2/6 上午 01:05
    信箱: didi31139@gmail.com
    說明:
*/
//@Database(entities = [Honors::class,Test::class], version =2, autoMigrations = [AutoMigration (from = 1, to = 2)], exportSchema=true)
@Database(
    entities = [
        Honors::class,
        Test::class,
        Course::class,
        IntellectualCuriosity::class,
        CommunityImpact::class,
        KeyValueData::class,
        TargetSchool::class,
        FCMessage::class,
        Extracurricular::class
    ],
    // v4: targetschool 的 male/female/acceptanceRate 由 INTEGER 改為 REAL。
    // 欄位型別變更無法使用 AutoMigration，靠 fallbackToDestructiveMigration() 重建資料庫
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
//@TypeConverters(GenreConverters::class)
@TypeConverters(DataConverter::class)
abstract class AdmittedDataBase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "AdmittedDataBase.db"
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }

    abstract fun getHonorsDao(): HonorsDao
    abstract fun getTestDao(): TestDao
    abstract fun getCourseDao(): CourseDao
    abstract fun getIntellectualCuriosityDao(): IntellectualCuriosityDao
    abstract fun getCommunityImpactDao(): CommunityImpactDao
    abstract fun getKeyValueDao(): KeyValueDao
    abstract fun getTargetSchoolDao(): TargetSchoolDao
    abstract fun getFCMessageDao(): FCMessageDao
    abstract fun getExtracurricularDao(): ExtracurricularDao

}