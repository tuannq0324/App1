package com.example.app1.database


//@Module
//@InstallIn(SingletonComponent::class)
//class AppModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            AppDatabase::class.java,
//            Constants.DB_NAME
//        ).fallbackToDestructiveMigration().build()
//    }
//}