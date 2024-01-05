package com.example.learning_foreign_words_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.models.AuthorizationToken;
import com.example.learning_foreign_words_app.models.WordDictionaryDataResponse;
import com.example.learning_foreign_words_app.models.WordMinicardResponse;
import com.example.learning_foreign_words_app.viewmodels.DictionaryWordViewModel;
import com.example.learning_foreign_words_app.viewmodels.TranslatedWordViewModel;

import java.io.IOException;

public class AddWordToDictionaryActivity extends AppCompatActivity {
    private EditText editTextWord, editTextDefinition, editTextTranslation;
    private Button buttonGetTranslation, buttonGetDefinition, buttonEnglishLanguage, buttonUkrainianLanguage, buttonPlayPronunciation;
    private NumberPicker numberPicker;
    private TranslatedWordViewModel translateWordViewModel;
    private DictionaryWordViewModel dictionaryWordViewModel;
    private AuthorizationToken authToken = new AuthorizationToken();
    private WordMinicardResponse wordTranslationResponse = new WordMinicardResponse();
    private String[] partsOfSpeachArray = new String[]{"noun", "pronoun", "verb", "adjective", "adverb", "preposition", "conjunction", "interjection"};
    public static final String EXTRA_WORD =
            "com.example.learning_foreign_words_app.activities.EXTRA_WORD";
    public static final String EXTRA_TRANSLATION =
            "com.example.learning_foreign_words_app.activities.EXTRA_TRANSLATION";
    public static final String EXTRA_DEFINITION =
            "com.example.learning_foreign_words_app.activities.EXTRA_DEFINITION";
    private static final int englishLanguage = 1033;
    private static final int ukrainianLanguage = 1058;
    private int sourceLanguage = englishLanguage;
    private int destinationLanguage = ukrainianLanguage;
    private String wordAudioUrl = null;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_to_dictionary);

        initialisation();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add word to dictionary");

        //for api
        translateWordViewModel = new ViewModelProvider(this).get(TranslatedWordViewModel.class);
        dictionaryWordViewModel = new ViewModelProvider(this).get(DictionaryWordViewModel.class);
        getAuthToken();
        setOnClick();

    }

    private void initialisation() {
        editTextDefinition = findViewById(R.id.edit_text_definition);
        editTextTranslation = findViewById(R.id.edit_text_translation);
        editTextWord = findViewById(R.id.edit_text_word);
        buttonGetTranslation = findViewById(R.id.button_get_translation);
        buttonGetDefinition = findViewById(R.id.button_get_definition);

        buttonEnglishLanguage = findViewById(R.id.language_english_button);
        buttonEnglishLanguage.setBackground(getDrawable(R.drawable.custom_visible_button));
        buttonGetDefinition.setEnabled(true);

        buttonUkrainianLanguage = findViewById(R.id.language_ukrainian_button);
        buttonPlayPronunciation = findViewById(R.id.audio_mode_play_pronunciation_button);
        buttonPlayPronunciation.setEnabled(false);

        numberPicker = findViewById(R.id.number_piker_part_of_speach);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(partsOfSpeachArray.length - 1);
        //numberPicker.setMaxValue();
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return partsOfSpeachArray[i];
            }
        });
    }

    private void getDefinition(String word, String partOfSpeech) {

        dictionaryWordViewModel.getDictionaryWordData(word, this).observe(this, wordDictionaryDataResponse -> {
            boolean setPartOfSpeech = false;
            if(wordDictionaryDataResponse != null){
                for (WordDictionaryDataResponse.Meaning meaning : wordDictionaryDataResponse.getMeanings()
                ) {
                    if (meaning.getPartOfSpeech().equals(partOfSpeech)) {
                        editTextDefinition.setText(meaning.getDefinitions().get(0).getDefinition());
                        setPartOfSpeech = true;
                    }
                }
                if (!setPartOfSpeech) {
                    editTextDefinition.setText(wordDictionaryDataResponse.getMeanings().get(0).getDefinitions().get(0).getDefinition());
                }
                for (WordDictionaryDataResponse.Phonetic phonetic:
                     wordDictionaryDataResponse.getPhonetics()) {
                    if(phonetic.getAudio()!= null && !phonetic.getAudio().trim().isEmpty()){
                        wordAudioUrl = phonetic.getAudio();
                        //Toast.makeText(this, "audio link: " + wordAudioUrl, Toast.LENGTH_SHORT).show();
                        buttonPlayPronunciation.setEnabled(true);
                        break;
                    }
                }
            }else {
                Toast.makeText(this, "Check word input and selected language", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getTranslation(String token, String word) {

        translateWordViewModel.getWordTranslationMinicard(token, word, sourceLanguage, destinationLanguage, this).observe(this, wordMinicardResponse -> {
            //Toast.makeText(AddWordToDictionaryActivity.this, "Translation: "+ wordMinicardResponse.getWordTranslation().getTranslation(), Toast.LENGTH_SHORT).show();
            if(wordMinicardResponse != null){
            editTextTranslation.setText(wordMinicardResponse.getWordTranslation().getTranslation());
            }
            else
                Toast.makeText(this, "Check word input and selected language", Toast.LENGTH_SHORT).show();
        });


//        editTextDefinition.setText("Need to make definition too");
    }

    private void getAuthToken() {
        String TranslationApiKey = "Basic NGE3ZjM0ZDgtNDczMC00ZDZkLTlkYzktOGQ2ZjNlNjNjNThhOjMxNmVhODdjZWEzMTRjNDQ5ZDE3YjBlNjk1ODMzOWRm";

        translateWordViewModel.getAuthorizationToken(TranslationApiKey).observe(this, authorizationToken -> {
            if (authorizationToken != null) {
                authToken.setAuthorizationToken(authorizationToken);
//                Toast.makeText(getApplicationContext(),
//                        "token from instance: " + authToken.getToken(),
//                        Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(getApplicationContext(),
                        "Error in getAuth ",
                        Toast.LENGTH_SHORT).show();
        });
    }

    private void saveWord() {
        String word = editTextWord.getText().toString().toLowerCase();
        String translation = editTextTranslation.getText().toString().toLowerCase();
        String definition = editTextDefinition.getText().toString().toLowerCase();

        if (word.trim().isEmpty()
                || translation.trim().isEmpty()
                || definition.trim().isEmpty()) {
            Toast.makeText(this, "Please insert values ", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        if(sourceLanguage == ukrainianLanguage){        // для збереження із укр міняємо місцями слово та переклад
            data.putExtra(EXTRA_WORD, translation);
            data.putExtra(EXTRA_TRANSLATION, word);
            data.putExtra(EXTRA_DEFINITION, definition);
        }
        else {
            data.putExtra(EXTRA_WORD, word);
            data.putExtra(EXTRA_TRANSLATION, translation);
            data.putExtra(EXTRA_DEFINITION, definition);
        }
        setResult(RESULT_OK, data);
        finish();  // close this activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_word_menu, menu); //дозволяє вик. add_word_menu в якості меню в даній активності
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_word:
                saveWord();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setOnClick() {
        buttonGetTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = editTextWord.getText().toString();

                if (!word.trim().isEmpty() && sourceLanguage != -1) {
                    if (sourceLanguage == englishLanguage && word.matches("[a-zA-Z]+")){
                        // make api call
                        getTranslation("Bearer " + authToken.getToken(), word);
                    } else if (sourceLanguage == ukrainianLanguage) {
                        getTranslation("Bearer " + authToken.getToken(), word);
                    } else
                        Toast.makeText(AddWordToDictionaryActivity.this, "Remove inappropriate characters", Toast.LENGTH_SHORT).show();
                } else if (sourceLanguage == -1) {
                    Toast.makeText(AddWordToDictionaryActivity.this, "Please select input language first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddWordToDictionaryActivity.this, "Please enter the word first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonGetDefinition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partOfSpeech = partsOfSpeachArray[numberPicker.getValue()];
                String word = editTextWord.getText().toString();

                if (!word.trim().isEmpty() && sourceLanguage != -1) {
                    if (word.matches("[a-zA-Z]+"))
                        getDefinition(word, partOfSpeech);
                    else
                        Toast.makeText(AddWordToDictionaryActivity.this, "Remove inappropriate characters", Toast.LENGTH_SHORT).show();
                } else if (sourceLanguage == -1) {
                    Toast.makeText(AddWordToDictionaryActivity.this, "Please select input language first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddWordToDictionaryActivity.this, "Please enter the word first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonEnglishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddWordToDictionaryActivity.this, "english", Toast.LENGTH_SHORT).show();
                if (sourceLanguage != englishLanguage){
                    sourceLanguage = englishLanguage;
                    destinationLanguage = ukrainianLanguage;
                    buttonUkrainianLanguage.setBackground(getDrawable(R.drawable.custom_invisible_button));
                    buttonEnglishLanguage.setBackground(getDrawable(R.drawable.custom_visible_button));
                    buttonGetDefinition.setEnabled(true);
                }

            }
        });

        buttonUkrainianLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddWordToDictionaryActivity.this, "ukrainian", Toast.LENGTH_SHORT).show();
                if(sourceLanguage != ukrainianLanguage){
                    sourceLanguage = ukrainianLanguage;
                    destinationLanguage = englishLanguage;
                    buttonEnglishLanguage.setBackground(getDrawable(R.drawable.custom_invisible_button));
                    buttonUkrainianLanguage.setBackground(getDrawable(R.drawable.custom_visible_button));
                    buttonGetDefinition.setEnabled(false);
                }

            }
        });

        buttonPlayPronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wordAudioUrl != null){
                    playAudio();
                }
                else {
                    Toast.makeText(AddWordToDictionaryActivity.this, "No audio", Toast.LENGTH_SHORT).show();
                }
                // розібратися із повторним програшем треку
            }
        });
    }

    private void playAudio() {
        if(player == null){
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                player.setDataSource(wordAudioUrl);
                player.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(player != null){
                        player.release();
                        player = null;
                        //Toast.makeText(AddWordToDictionaryActivity.this, "Player release", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        player.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player != null){
            player.release();
            player = null;
        }

    }
}