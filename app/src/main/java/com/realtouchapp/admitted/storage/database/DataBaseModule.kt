package com.realtouchapp.admitted.storage.database

import android.content.Context
import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.realtouchapp.admitted.encryption.AdmittedEncrption
import com.realtouchapp.admitted.network.entity.AreaOfInterest
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.storage.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    作者: RobertChou
    時間: 2023/2/5 下午 02:13
    信箱: didi31139@gmail.com
    說明:
*/
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AdmittedDataBase {
        return Room.databaseBuilder(
            appContext,
            AdmittedDataBase::class.java,
            AdmittedDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration() // 如果遷移失敗，重建資料庫
            .addTypeConverter(DataConverter())
//            .addMigrations(AdmittedDataBase.MIGRATION_1_2)
            .build()


    }

    @Provides
    fun provideHonorsDao(admittedDataBase: AdmittedDataBase): HonorsDao {

        return admittedDataBase.getHonorsDao()
    }

    @Provides
    fun provideTestDao(admittedDataBase: AdmittedDataBase): TestDao {

        return admittedDataBase.getTestDao()
    }
    @Provides
    fun provideCourseDao(admittedDataBase: AdmittedDataBase): CourseDao {

        return admittedDataBase.getCourseDao()
    }

    @Provides
    fun provideIntellectualCuriosityDao(admittedDataBase: AdmittedDataBase): IntellectualCuriosityDao {

        return admittedDataBase.getIntellectualCuriosityDao()
    }

    @Provides
    fun provideCommunityImpactDao(admittedDataBase: AdmittedDataBase): CommunityImpactDao {

        return admittedDataBase.getCommunityImpactDao()
    }

    @Provides
    fun provideKeyValueDao(admittedDataBase: AdmittedDataBase): KeyValueDao {

        return admittedDataBase.getKeyValueDao()
    }
    @Provides
    fun provideTargetSchoolDao(admittedDataBase: AdmittedDataBase): TargetSchoolDao {

        return admittedDataBase.getTargetSchoolDao()
    }

    @Provides
    fun provideFCMessageDao(admittedDataBase: AdmittedDataBase): FCMessageDao {

        return admittedDataBase.getFCMessageDao()
    }

    @Provides
    fun provideExtracurricularDao(admittedDataBase: AdmittedDataBase): ExtracurricularDao {

        return admittedDataBase.getExtracurricularDao()
    }
    
}
@ProvidedTypeConverter
class DataConverter(){
    @TypeConverter
    fun StringToAreaOfInterest(string: String?): AreaOfInterest? {
         var result= Gson().fromJson(
             string,
             AreaOfInterest::class.java)
        return  result
    }

    @TypeConverter
    fun AreaOfInterestToString(areaOfInterest: AreaOfInterest?): String? {
        var result= Gson().toJson(areaOfInterest)
        return  result
    }
    @TypeConverter
    fun StringToListString(string: String?): List<String>? {
        if (string.isNullOrEmpty()){ return  ArrayList<String>()}else{

        return string.split(",")
        }
    }

    @TypeConverter
    fun StringListToString(array: List<String>?): String? {
        if (array.isNullOrEmpty()){
            return ""
        }else{
            return array.joinToString(",")
        }
    }
}