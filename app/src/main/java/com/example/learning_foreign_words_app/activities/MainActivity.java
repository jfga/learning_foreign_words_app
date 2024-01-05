package com.example.learning_foreign_words_app.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.adapters.WordDBAdapter;
import com.example.learning_foreign_words_app.listeners.RecyclerTouchListener;
import com.example.learning_foreign_words_app.models.AuthorizationToken;
import com.example.learning_foreign_words_app.models.DictionaryWordResponse;
import com.example.learning_foreign_words_app.viewmodels.DictionaryWordViewModel;
import com.example.learning_foreign_words_app.viewmodels.TranslatedWordViewModel;
import com.example.learning_foreign_words_app.viewmodels.WordTranslateDBViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TranslatedWordViewModel viewModel;
    private DictionaryWordViewModel dictionaryWordViewModel;
    //private AuthorizationToken authToken = new AuthorizationToken();
    private FloatingActionButton buttonAddWord, buttonStartLearning;
    // db
    private WordTranslateDBViewModel wordTranslateDBViewModel;
    public static final int ADD_WORD_TO_DICTIONARY_REQUEST = 1;
    public static final int DELETE_WORD_FROM_DB_REQUEST = 2;
    public static final String ID_WORD_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.ID_WORD_FROM_DB_EXTRA";
    public static final String WORD_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.WORD_FROM_DB_EXTRA";
    public static final String TRANSLATION_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.TRANSLATION_FROM_DB_EXTRA";
    public static final String DEFINITION_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.DEFINITION_FROM_DB_EXTRA";
    public static final String GUESSED_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.GUESSED_FROM_DB_EXTRA";
    public static final String WAS_SHOWED_FROM_DB_EXTRA =
            "com.example.learning_foreign_words_app.activities.WAS_SHOWED_FROM_DB_EXTRA";

    public static final int RESULT_OK_FOR_UPDATE_DB = 101;
    public static final int RESULT_OK_FOR_DELETE_FROM_DB = 102;
    private DictionaryWordResponse wordResponse = new DictionaryWordResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doInitialization();
        viewModel = new ViewModelProvider(this).get(TranslatedWordViewModel.class);
        startLearningActivity();
        dictionaryWordViewModel = new ViewModelProvider(this).get(DictionaryWordViewModel.class);
        // DB
        databaseWork();

    }

    private void doInitialization(){

        buttonAddWord = findViewById(R.id.button_add_word);

        buttonStartLearning = findViewById(R.id.button_to_learning_activity);
    }

    private void recyclerViewInit(WordDBAdapter adapter){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //DbWordTranslationModel wordModel = adapter.getModelByPosition(position);

                Intent intent = new Intent(MainActivity.this, WordExtrasFromDbActivity.class);

                intent.putExtra(ID_WORD_FROM_DB_EXTRA, adapter.getModelByPosition(position).getId());
                intent.putExtra(WORD_FROM_DB_EXTRA,adapter.getModelByPosition(position).getWord());
                intent.putExtra(TRANSLATION_FROM_DB_EXTRA, adapter.getModelByPosition(position).getTranslation());
                intent.putExtra(DEFINITION_FROM_DB_EXTRA, adapter.getModelByPosition(position).getDefinition());
                intent.putExtra(GUESSED_FROM_DB_EXTRA, adapter.getModelByPosition(position).getGuessed());  // change later for guessed/wasShown
                intent.putExtra(WAS_SHOWED_FROM_DB_EXTRA, adapter.getModelByPosition(position).getShown());

                //Toast.makeText(MainActivity.this, "Clicked on position " + adapter.getModelByPosition(position).getWord(), Toast.LENGTH_SHORT).show();

                startActivityForResult(intent, DELETE_WORD_FROM_DB_REQUEST);  // starts words_extras_activity
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

//    private void getAuthToken(){
//        String TranslationApiKey = "Basic NGE3ZjM0ZDgtNDczMC00ZDZkLTlkYzktOGQ2ZjNlNjNjNThhOjMxNmVhODdjZWEzMTRjNDQ5ZDE3YjBlNjk1ODMzOWRm";
//
//        viewModel.getAuthorizationToken(TranslationApiKey).observe(this, authorizationToken ->{
//            if(authorizationToken != null){
//                authToken.setAuthorizationToken(authorizationToken);
//
//                Toast.makeText(getApplicationContext(),
//                        "token from instance: "+ authToken.getToken(),
//                        Toast.LENGTH_SHORT).show();
//
//               // viewModel.setAuthToApi(authorizationToken);
//                //tokenText.setText("Yes: " + viewModel.getAuthToApi());
//                tokenText.setText(viewModel.getAuthToApi());
//            }
//            else
//                Toast.makeText(getApplicationContext(),
//                        "Error in getAuth ",
//                        Toast.LENGTH_SHORT).show();
//                });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_TO_DICTIONARY_REQUEST && resultCode == RESULT_OK) {
            String word = data.getStringExtra(AddWordToDictionaryActivity.EXTRA_WORD);
            String translation = data.getStringExtra(AddWordToDictionaryActivity.EXTRA_TRANSLATION);
            String definition = data.getStringExtra(AddWordToDictionaryActivity.EXTRA_DEFINITION);

            DbWordTranslationModel wodrTranslation = new DbWordTranslationModel(word, translation, definition, 0, 0);
            wordTranslateDBViewModel.saveWord(wodrTranslation);

            Toast.makeText(this, "Word saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == DELETE_WORD_FROM_DB_REQUEST && resultCode == RESULT_OK_FOR_DELETE_FROM_DB) {

            String word_id = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WORD_ID);
            String word = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WORD);
            String translation = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_TRANSLATION);
            String definition = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_DEFINITION);
            String guessed = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_GUESSED);
            String wasShowed = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WAS_SHOWED);

            DbWordTranslationModel deleteWord = new DbWordTranslationModel(
                    word, translation, definition, Integer.parseInt(wasShowed), Integer.parseInt(guessed));

            deleteWord.setId(Integer.parseInt(word_id));

            Toast.makeText(this, "Deleted: " + deleteWord, Toast.LENGTH_SHORT).show();
            wordTranslateDBViewModel.deleteWord(deleteWord);  // спробувати перевірити роботу асинхронних методів для видалення

        } else if (requestCode == DELETE_WORD_FROM_DB_REQUEST && resultCode == RESULT_OK_FOR_UPDATE_DB) {

            String word_id = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WORD_ID);
            String word = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WORD);
            String translation = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_TRANSLATION);
            String definition = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_DEFINITION);
            String guessed = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_GUESSED);
            String wasShowed = data.getStringExtra(WordExtrasFromDbActivity.EXTRA_WAS_SHOWED);

            DbWordTranslationModel updateWord = new DbWordTranslationModel(
                    word, translation, definition, Integer.parseInt(wasShowed), Integer.parseInt(guessed));

            updateWord.setId(Integer.parseInt(word_id));
            wordTranslateDBViewModel.updateWord(updateWord);     // add update word method to db
            //Toast.makeText(this, "RESULT CODE: " + resultCode, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Changes not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void databaseWork(){

        buttonAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "You clicked at me :|", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddWordToDictionaryActivity.class);
                startActivityForResult(intent, ADD_WORD_TO_DICTIONARY_REQUEST); // замінити на: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
            }
        });


        WordDBAdapter adapter = new WordDBAdapter();
        recyclerViewInit(adapter);

        wordTranslateDBViewModel = new ViewModelProvider(this).get(WordTranslateDBViewModel.class);
        wordTranslateDBViewModel.getAllWords().observe(this, new Observer<List<DbWordTranslationModel>>() {
            @Override
            public void onChanged(List<DbWordTranslationModel> wordTranslations) {
                adapter.setWordsTranslation(wordTranslations);
            }
        });
    }

    private void startLearningActivity(){
        buttonStartLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Let`s learning", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LearningModsActivity.class );
                startActivity(intent);
                finish();
            }
        });
    }
}