package com.example.learning_foreign_words_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;

public class WordExtrasFromDbActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextWord, editTextTranslation, editTextDefinition;
    private TextView textViewGuessedPercentage, textViewWordId, textViewWasShowed;
    private Button deleteBtn, updateBtn;

    public static final String EXTRA_WORD_ID =
            "com.example.learning_foreign_words_app.activities.EXTRA_WORD_ID";
    public static final String EXTRA_WORD =
            "com.example.learning_foreign_words_app.activities.EXTRA_WORD";
    public static final String EXTRA_TRANSLATION =
            "com.example.learning_foreign_words_app.activities.EXTRA_TRANSLATION";
    public static final String EXTRA_DEFINITION =
            "com.example.learning_foreign_words_app.activities.EXTRA_DEFINITION";
    public static final String EXTRA_GUESSED =
            "com.example.learning_foreign_words_app.activities.EXTRA_GUESSED";
    public static final String EXTRA_WAS_SHOWED =
            "com.example.learning_foreign_words_app.activities.EXTRA_WAS_SHOWED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_word_data);
        doInitialisation();

    }

    private void doInitialisation(){

        textViewWordId = findViewById(R.id.text_view_word_id_extra);
        editTextWord = findViewById(R.id.text_view_extra_word);
        editTextTranslation = findViewById(R.id.text_view_extra_translate);
        editTextDefinition = findViewById(R.id.text_view_extra_definition);
        textViewGuessedPercentage = findViewById(R.id.text_view_extra_guessed_percentage);
        textViewWasShowed = findViewById(R.id.text_view_extra_was_showed);

        deleteBtn = findViewById(R.id.delete_word_from_db);
        updateBtn = findViewById(R.id.update_word_from_db_btn);

        // get data from intent
        getIntentFromMainActivity();

        deleteBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
    }

    private void getIntentFromMainActivity(){
        Intent intent = getIntent();

        int gottenWord_id = intent.getIntExtra(MainActivity.ID_WORD_FROM_DB_EXTRA, -1);
        String gottenWord = intent.getStringExtra(MainActivity.WORD_FROM_DB_EXTRA);
        String gottenTranslation = intent.getStringExtra(MainActivity.TRANSLATION_FROM_DB_EXTRA);
        String gottenDefinition = intent.getStringExtra(MainActivity.DEFINITION_FROM_DB_EXTRA);
        int guessed = intent.getIntExtra(MainActivity.GUESSED_FROM_DB_EXTRA,-1);
        int wasShowed = intent.getIntExtra(MainActivity.WAS_SHOWED_FROM_DB_EXTRA, -1);

        // set data from intent
        textViewWordId.setText(""+gottenWord_id);
        editTextWord.setText(gottenWord);
        editTextTranslation.setText(gottenTranslation);
        editTextDefinition.setText(gottenDefinition);
        textViewGuessedPercentage.setText(""+guessed);
        textViewWasShowed.setText("" + wasShowed);
    }


    @Override
    public void onClick(View view) {
        Intent data = new Intent();
        switch (view.getId()){
            case R.id.delete_word_from_db:


                data.putExtra(EXTRA_WORD_ID, textViewWordId.getText().toString());
                data.putExtra(EXTRA_WORD, editTextWord.getText().toString());
                data.putExtra(EXTRA_TRANSLATION, editTextTranslation.getText().toString());
                data.putExtra(EXTRA_DEFINITION, editTextDefinition.getText().toString());
                data.putExtra(EXTRA_GUESSED, textViewGuessedPercentage.getText().toString());
                data.putExtra(EXTRA_WAS_SHOWED, textViewWasShowed.getText().toString());

                setResult(MainActivity.RESULT_OK_FOR_DELETE_FROM_DB,data);
                finish();

            case R.id.update_word_from_db_btn:
                //Toast.makeText(this, "attempt to update", Toast.LENGTH_SHORT).show();

                if(editTextWord.getText().toString().trim().isEmpty()
                        || editTextTranslation.getText().toString().trim().isEmpty()
                        || editTextDefinition.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Fill the data please", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!editTextWord.getText().toString().matches("[a-zA-Z]+")){
                    Toast.makeText(this, "Remove inappropriate characters in word field", Toast.LENGTH_SHORT).show();
                    break;
                }

                data.putExtra(EXTRA_WORD_ID, textViewWordId.getText().toString());
                data.putExtra(EXTRA_WORD, editTextWord.getText().toString());
                data.putExtra(EXTRA_TRANSLATION, editTextTranslation.getText().toString());
                data.putExtra(EXTRA_DEFINITION, editTextDefinition.getText().toString());
                data.putExtra(EXTRA_GUESSED, textViewGuessedPercentage.getText().toString());
                data.putExtra(EXTRA_WAS_SHOWED, textViewWasShowed.getText().toString());

                setResult(MainActivity.RESULT_OK_FOR_UPDATE_DB, data);
                finish();
        }
        

        
    }
}
