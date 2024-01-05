package com.example.learning_foreign_words_app.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = DbWordTranslationModel.class, version = 2)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase instance; // ми створюємо це для singleton для запобігання
                                            // створення більше ніж 1 екземпляра
    public abstract WordTranslationDAO wordDao();
    public static synchronized WordDatabase getInstance(Context context){  // sync запобігає доступу до БД із декількох потоків одночасно
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WordDatabase.class, "word_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // заповнення БД при створенні
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private WordTranslationDAO wordTranslationDAO;

        private PopulateDbAsyncTask(WordDatabase database){
            wordTranslationDAO = database.wordDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wordTranslationDAO.saveWord(new DbWordTranslationModel("привіт","hello","greeting in english",0,0));
            wordTranslationDAO.saveWord(new DbWordTranslationModel("стіл","table","an object of interior",0,0));
            wordTranslationDAO.saveWord(new DbWordTranslationModel("квітка","flower","an biological object",0,0));
            return null;
        }
    }

}
