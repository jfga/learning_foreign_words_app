package com.example.learning_foreign_words_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordTranslationDAO {

    @Query("Select * from saved_word_translation_table")
    LiveData<List<DbWordTranslationModel>> getAllWords();
    @Query("Select * from saved_word_translation_table where (100 * guessed) / shown <= 50 or shown is 0 order by shown")
    LiveData<List<DbWordTranslationModel>> getWordsForMainMode();
    @Insert
    void saveWord(DbWordTranslationModel dbWordTranslationModel);

    @Delete
    void deleteWord(DbWordTranslationModel dbWordTranslationModel);

    @Update
    void updateWord(DbWordTranslationModel dbWordTranslationModel);

    @Query("Update saved_word_translation_table set shown = shown + 1 where id = :wordId ")
    void updateWordShownCount (int wordId);   // збільшити кількість wasShown на 1

    @Query("Update saved_word_translation_table "+
    "set guessed = guessed + 1 where id = :wordId ")
    void updateWordGuessed(int wordId);

}
