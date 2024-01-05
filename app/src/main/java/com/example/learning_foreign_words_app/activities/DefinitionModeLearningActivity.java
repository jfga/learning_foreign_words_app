package com.example.learning_foreign_words_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.viewmodels.WordTranslateDBViewModel;

import java.util.List;
import java.util.Random;

public class DefinitionModeLearningActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCheck, buttonTurn, buttonToDefinition, buttonToTranslation, buttonSuggestion;
    private TextView definitionTextView;
    private EditText inputEditText;
    private WordTranslateDBViewModel viewModel;
    private List<DbWordTranslationModel> dbWordList;
    private DbWordTranslationModel currentDbWordModel;
    private final static  int TRANSLATION = 1001;
    private final static int DEFINITION = 1002;
    private int selectedWordOutputData = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition_mode_learning);
        initialisation();
        viewModel = new ViewModelProvider(this).get(WordTranslateDBViewModel.class);
        getWordsList();

        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Writing mode")
                .setMessage("In this mode, you have to spell the word correctly according to the translation or definition. \n Good luck!")
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        start();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void initialisation(){
        buttonCheck = findViewById(R.id.definition_mode_check_button);
        buttonTurn = findViewById(R.id.definition_turn_button);
        buttonCheck.setOnClickListener(this);
        buttonTurn.setOnClickListener(this);
        buttonToDefinition = findViewById(R.id.definition_definition_button);
        buttonToTranslation = findViewById(R.id.definition_translation_button);
        buttonToDefinition.setOnClickListener(this);
        buttonToTranslation.setOnClickListener(this);
        buttonSuggestion = findViewById(R.id.definition_suggest_button);
        buttonSuggestion.setOnClickListener(this);
        inputEditText = findViewById(R.id.definition_mode_input_word_edit_text);
        definitionTextView = findViewById(R.id.definition_definition_mode_text_view);

        buttonToDefinition.setBackground(getDrawable(R.drawable.custom_invisible_button));
        buttonToTranslation.setBackground(getDrawable(R.drawable.custom_visible_button));

    }


    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;

        switch(clickedButton.getId()) {
            case R.id.definition_mode_check_button:
                if (inputEditText.getText() != null &&
                        !inputEditText.getText().toString().trim().isEmpty()) {
                    if(checkSpelling()){
                        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                        setRandomWordModel();
                        loadDefinitionText();
                        inputEditText.setText("");
                    }
                }
                else
                    Toast.makeText(this, "Please, input word first", Toast.LENGTH_SHORT).show();
                break;
            case R.id.definition_turn_button:
                setRandomWordModel();
                loadDefinitionText();
                break;
            case R.id.definition_definition_button:
                buttonToTranslation.setBackground(getDrawable(R.drawable.custom_invisible_button));
                buttonToDefinition.setBackground(getDrawable(R.drawable.custom_visible_button));
                selectedWordOutputData = DEFINITION;
                loadDefinitionText();
                break;
            case R.id.definition_translation_button:
                buttonToDefinition.setBackground(getDrawable(R.drawable.custom_invisible_button));
                buttonToTranslation.setBackground(getDrawable(R.drawable.custom_visible_button));
                selectedWordOutputData = TRANSLATION;
                loadDefinitionText();
                break;
            case R.id.definition_suggest_button:
                openLatter();
                break;
        }
    }

    private void getWordsList(){
        viewModel.getAllWords().observe(this, new Observer<List<DbWordTranslationModel>>() {
            @Override
            public void onChanged(List<DbWordTranslationModel> dbWordTranslationModels) {
                if(dbWordTranslationModels != null){
                    dbWordList = dbWordTranslationModels;
                }
            }
        });
    }

    private void loadDefinitionText(){
        if(currentDbWordModel != null){
            if(selectedWordOutputData == DEFINITION){
                definitionTextView.setText(currentDbWordModel.getDefinition());
            }
            else if(selectedWordOutputData == TRANSLATION){
                definitionTextView.setText(currentDbWordModel.getTranslation());
            }
        }
        else
            Toast.makeText(this, "Can`t set definition", Toast.LENGTH_SHORT).show();
    }

    private void setRandomWordModel(){
        Random random = new Random();
        if(dbWordList != null){
            DbWordTranslationModel newModel = dbWordList.get(random.nextInt(dbWordList.size()));
            if(currentDbWordModel!= null){
                if(!currentDbWordModel.getWord().equals(newModel.getWord())){
                    currentDbWordModel = newModel;
                }
                else{
                    currentDbWordModel = dbWordList.get(random.nextInt(dbWordList.size()));
                }
            }else{
                currentDbWordModel = dbWordList.get(random.nextInt(dbWordList.size()));
            }
        }
        else
            Toast.makeText(this, "Can`t get wordsList", Toast.LENGTH_SHORT).show();
    }

    private boolean checkSpelling(){
        String currentInputVariant = inputEditText.getText().toString();
        Log.i("definitionAlg"," \n  ------------------------------ \n");
        if(currentInputVariant.matches("[a-zA-Z]+")){

            char[] inputArray = currentInputVariant.toLowerCase().toCharArray();
            Log.i("definitionAlg"," current input: " + currentInputVariant.toLowerCase());

            char[] word = currentDbWordModel.getWord().toLowerCase().toCharArray();
            Log.i("definitionAlg"," current word: " + currentDbWordModel.getWord().toLowerCase());

            int minLength = inputArray.length;
            int maxLength = word.length;
            boolean isCorrect = true;
            if(inputArray.length >= word.length ){
                minLength = word.length;
                maxLength = inputArray.length;
                Log.i("definitionAlg"," less length in Word: " + minLength);
            }
            else
                Log.i("definitionAlg"," less length in Input: " + minLength);

            char[] resultArray = new char[maxLength];
            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] = '-';
            }

            int wordIndex = 0;
            try{
                for (int i = 0; i < minLength; i++) {
                    if(word.length != inputArray.length)
                        isCorrect = false;
//                    if(i+1 == minLength && word.length > inputArray.length && resultArray[resultArray.length-1] == 0){
//                        resultArray[resultArray.length-1] = '-';
//                        isCorrect = false;
//                    }
                    Log.i("definitionAlg"," Next Step");
                    Log.i("definitionAlg","current WordIndex: " + wordIndex + " current Input index: " + i);
                    if(word[wordIndex] != inputArray[i]){ // якщо символ не співпав
                        Log.i("definitionAlg","     current WordIndex: " + wordIndex + " current Input index: " + i);
                        Log.i("definitionAlg","     Word char: "+ word[wordIndex] + " != Input char: " + inputArray[i]);
                        isCorrect = false;

                        Log.i("definitionAlg","     input Array char: " + inputArray[i] + " changed for '-' ");
                        if( i!= 0 && i< minLength &&
                                inputArray[i] == word[wordIndex+1] && inputArray[i-1] == word[wordIndex-1]){  // якщо наст. та попер. однакові (пропустив літеру)
                            Log.i("definitionAlg","         next Input char: "+ inputArray[i]
                                    + " next Word char: "+ word[wordIndex+1] +
                                    " \n       and previous Input char: "+ inputArray[i-1]
                                    + "previous Word char: "+ word[wordIndex-1]
                                    +" equals");
                            resultArray[wordIndex] = '-';
                            resultArray[wordIndex+1] = inputArray[i];

                            wordIndex = wordIndex + 2;  // зміщуємо індекс на 1 вперед
                            Log.i("definitionAlg"," raise Word index for 2, wordIndex: " + wordIndex );
                        } else if (i == 0 && inputArray[i] == word[wordIndex+1]) {
                            Log.i("definitionAlg","__________________________\n"
                                    +"     first element miss "
                                    +"         next Input char: "+ inputArray[i]
                                    + " next Word char: "+ word[wordIndex+1] +" equals");
                            resultArray[wordIndex] = '-';
                            resultArray[wordIndex+1] = inputArray[i];
                            wordIndex = wordIndex + 2;
                        } else{
                            inputArray[i] = '-';
                            resultArray[wordIndex] = inputArray[i];
                            wordIndex++;
                        }
                    }else{  // якщо співпав
                        resultArray[wordIndex] = inputArray[i];
                        Log.i("definitionAlg"," Input char "+ inputArray[i] +" = " + word[wordIndex]+ " Word char, wordIndex: " + wordIndex );
                        wordIndex++;
                    }
                }
            }catch (IndexOutOfBoundsException e){
                Toast.makeText(this, "Exception out of bound", Toast.LENGTH_SHORT).show();
                //inputEditText.setText(new String(resultArray));
                Log.i("definitionAlg"," Exception out of bound");
                e.printStackTrace();
            }
            Log.i("definitionAlg"," Final array = " + new String(resultArray));
            inputEditText.setText(new String(resultArray));
            return isCorrect;
        }
        else {
            Toast.makeText(this, "Input contains numbers or inappropriate symbols", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void openLatter(){

        if(currentDbWordModel == null)
            return;
        char[] inputText = inputEditText.getText().toString().toCharArray();
        char[] word = currentDbWordModel.getWord().toCharArray();
        int minLenth = word.length;
        if( inputText.length < word.length)
            minLenth = inputText.length;

        if(inputText.length == 0){
            char[] result = new char[1];
            result[0] = word[0];
            inputEditText.setText(new String(result));
        }
        else {
            for (int i = 0; i < minLenth; i++) {
                if(inputText[i] != word[i]){
                    inputText[i] = word[i];
                    i = word.length;
                }
                else if(i+1 == minLenth && minLenth != word.length && word[minLenth] != 0){
                    char[] result = new char[inputText.length + 1];
                    for (int j = 0; j < result.length; j++) {
                        if(j<inputText.length){
                            result[j] = inputText[j];
                        }
                        else{
                            result[j] = word[j];
                            inputEditText.setText(new String(result));
                            return;
                        }
                    }
                }
            }
            inputEditText.setText(new String(inputText));
        }
    }
    private void start(){
        setRandomWordModel();
        loadDefinitionText();
    }

}