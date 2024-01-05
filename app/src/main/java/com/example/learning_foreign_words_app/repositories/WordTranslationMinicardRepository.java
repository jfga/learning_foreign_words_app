package com.example.learning_foreign_words_app.repositories;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learning_foreign_words_app.activities.AddWordToDictionaryActivity;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.database.WordDatabase;
import com.example.learning_foreign_words_app.database.WordTranslationDAO;
import com.example.learning_foreign_words_app.models.AuthorizationToken;
import com.example.learning_foreign_words_app.models.WordMinicardResponse;
import com.example.learning_foreign_words_app.network.ApiClient;
import com.example.learning_foreign_words_app.network.TranslationApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordTranslationMinicardRepository {

    private TranslationApiService translationApiService;
    //toDelete// private ApiService tokenApiservice;
    private static final String apiKey = "Basic NGE3ZjM0ZDgtNDczMC00ZDZkLTlkYzktOGQ2ZjNlNjNjNThhOjMxNmVhODdjZWEzMTRjNDQ5ZDE3YjBlNjk1ODMzOWRm";
    private String FinalToken = "Bearer ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMk9ETTNNVGt3T1RZc0lrMXZaR1ZzSWpwN0lrTm9ZWEpoWTNSbGNuTlFaWEpFWVhraU9qVXdNREF3TENKVmMyVnlTV1FpT2pnMk56Y3NJbFZ1YVhGMVpVbGtJam9pTkdFM1pqTTBaRGd0TkRjek1DMDBaRFprTFRsa1l6a3RPR1EyWmpObE5qTmpOVGhoSW4xOS5WNmk0c3lfZGw5cjBHMGZ2NVI2cW5DM1JlVUk2RHhVY0NQOWl4emdRR3JZ";

    private WordTranslationDAO wordTranslationDAO;
    private LiveData<List<DbWordTranslationModel>> allWords;

    public WordTranslationMinicardRepository(Application application){ // для використання бд, потрібний context, application - subclass of context
        translationApiService = ApiClient.getRetrofit().create(TranslationApiService.class);

        //database work
        WordDatabase database = WordDatabase.getInstance(application);
        wordTranslationDAO = database.wordDao();
        allWords = wordTranslationDAO.getAllWords();
    }
    public WordTranslationMinicardRepository(){ // для використання бд, потрібний context, application - subclass of context
        translationApiService = ApiClient.getRetrofit().create(TranslationApiService.class);


    }

    public LiveData<WordMinicardResponse> getWordTranslationMinicard(String auth, String word, int scrLanguage, int destLanguage, Context context){

        MutableLiveData<WordMinicardResponse> data = new MutableLiveData<>();
        translationApiService.getWordTranslationMinicard(auth, word, scrLanguage, destLanguage).enqueue(new Callback<WordMinicardResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<WordMinicardResponse> call, @NonNull Response<WordMinicardResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
                else
                    Toast.makeText(context, "Check the spelling of the word\nor selected language", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<WordMinicardResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<String> getAuthorizationToken(String apiKey){
        MutableLiveData<String> authToken = new MutableLiveData<>();
        AuthorizationToken token = new AuthorizationToken();
        token.setAuthorizationToken("");
        translationApiService.getAuthorizationToken(apiKey, token).enqueue(new Callback<String>() { // change for apiService
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                authToken.setValue(response.body());
                FinalToken = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                authToken.setValue(null);
            }

        });

        return authToken;
    }// працюючий метод для отримання токена


    //methods for database operations

    //запити до БД виконуються тільки асинхронно
    public LiveData<List<DbWordTranslationModel>> getWordsListForMainMode(){
        return wordTranslationDAO.getWordsForMainMode();
    }
    public void insertWord(DbWordTranslationModel dbWordTranslationModel){
        new InsertWordAsyncTask(wordTranslationDAO).execute(dbWordTranslationModel);
    }
    public void delete(DbWordTranslationModel dbWordTranslationModel){
        new DeleteWordAsyncTask(wordTranslationDAO).execute(dbWordTranslationModel);
    }

    public void updateWord(DbWordTranslationModel dbWordTranslationModel){
        new UpdateWordWordAsyncTask(wordTranslationDAO).execute(dbWordTranslationModel);
    }

    public void updateWasShowed(DbWordTranslationModel dbWordTranslationModel){
        new UpdateWasShowedWordAsyncTask(wordTranslationDAO).execute(dbWordTranslationModel);
    }

    public void updateGuessed(DbWordTranslationModel dbWordTranslationModel){
        new UpdateGuessedWordAsyncTask(wordTranslationDAO).execute(dbWordTranslationModel);
    }


    public LiveData<List<DbWordTranslationModel>> getAllWords() {
        return allWords;
    }

    // асинхронність для запитів до БД
    private static class InsertWordAsyncTask extends AsyncTask<DbWordTranslationModel, Void, Void>{
        private WordTranslationDAO wordTranslationDAO; //викор для операцій в БД

        private InsertWordAsyncTask(WordTranslationDAO wordTranslationDAO){
            this.wordTranslationDAO = wordTranslationDAO;
        }

        @Override
        protected Void doInBackground(DbWordTranslationModel... dbWordTranslationModels) {
            wordTranslationDAO.saveWord(dbWordTranslationModels[0]); // заносимо слово у БД
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<DbWordTranslationModel, Void, Void>{
        private WordTranslationDAO wordTranslationDAO; //вик для операцій в БД

        private DeleteWordAsyncTask(WordTranslationDAO wordTranslationDAO){
            this.wordTranslationDAO = wordTranslationDAO;
        }

        @Override
        protected Void doInBackground(DbWordTranslationModel... dbWordTranslationModels) {
            wordTranslationDAO.deleteWord(dbWordTranslationModels[0]); // видаляємо слово із БД

            return null;
        }
    }

    private static class UpdateWordWordAsyncTask extends AsyncTask<DbWordTranslationModel, Void, Void>{
        private WordTranslationDAO wordTranslationDAO; //вик для операцій в БД

        private UpdateWordWordAsyncTask(WordTranslationDAO wordTranslationDAO){
            this.wordTranslationDAO = wordTranslationDAO;
        }

        @Override
        protected Void doInBackground(DbWordTranslationModel... dbWordTranslationModels) {
            wordTranslationDAO.updateWord(dbWordTranslationModels[0]); // збільшуємо значення відображень слова у БД
            return null;
        }
    }

    private static class UpdateWasShowedWordAsyncTask extends AsyncTask<DbWordTranslationModel, Void, Void>{
        private WordTranslationDAO wordTranslationDAO; //вик для операцій в БД

        private UpdateWasShowedWordAsyncTask(WordTranslationDAO wordTranslationDAO){
            this.wordTranslationDAO = wordTranslationDAO;
        }

        @Override
        protected Void doInBackground(DbWordTranslationModel... dbWordTranslationModels) {
            wordTranslationDAO.updateWordShownCount(dbWordTranslationModels[0].getId()); // збільшуємо значення відображень слова у БД
            return null;
        }
    }

    private static class UpdateGuessedWordAsyncTask extends AsyncTask<DbWordTranslationModel, Void, Void>{
        private WordTranslationDAO wordTranslationDAO; //вик для операцій в БД

        private UpdateGuessedWordAsyncTask(WordTranslationDAO wordTranslationDAO){
            this.wordTranslationDAO = wordTranslationDAO;
        }

        @Override
        protected Void doInBackground(DbWordTranslationModel... dbWordTranslationModels) {
            wordTranslationDAO.updateWordGuessed(dbWordTranslationModels[0].getId()); // збільшуємо значенння вгадувань у БД
            return null;
        }
    }

}
