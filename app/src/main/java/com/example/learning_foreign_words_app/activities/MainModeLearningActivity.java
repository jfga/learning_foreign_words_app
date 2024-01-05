package com.example.learning_foreign_words_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.models.WordModelForMainMode;
import com.example.learning_foreign_words_app.viewmodels.WordTranslateDBViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainModeLearningActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonFirstAnswer, buttonSecondAnswer, buttonThirdAnswer,
            buttonFourthAnswer, buttonStart;
    private TextView modeNameTextView, mainWordTextView, scoreTextView;
    private WordTranslateDBViewModel dbViewModel;
    private List<DbWordTranslationModel> wordsList;
    private String[] randomTranslations;
    private HashSet<String> randomTranslationsHashSet = new HashSet<>();
    private int currentMainWord = 0;
    private int correctAnswerButtonId;
    private String correctTranslation;
    private int correctUserAnswers = 0;
    private int attempts = 0;
    private int selectedButtonId = -1;
    private int SELECTED_MODE = 0;
    private String selectedModeName = "Main mode";
    private boolean isMistake = false;
    private List<WordModelForMainMode> mistakes = new ArrayList<>();
    private DbWordTranslationModel currentDBWordTranslationModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mode_learning);
        doInitialisation();
        dbViewModel = new ViewModelProvider(this).get(WordTranslateDBViewModel.class);
        getWordsListFromDb();
//        getRandomTranslations();

    }


    private void doInitialisation() {
        modeNameTextView = findViewById(R.id.mode_name_text);
        mainWordTextView = findViewById(R.id.audio_mode_play_again_text);
        scoreTextView = findViewById(R.id.main_mode_score);
        buttonStart = findViewById(R.id.button_main_mode_start);

        Intent intent = getIntent();
        SELECTED_MODE = intent.getIntExtra(LearningModsActivity.MAIN_MODE_LEARNING_ACTIVITY, -1);
        if (SELECTED_MODE == 2) {
            modeNameTextView.setText("Endless mode");
            selectedModeName = "Endless mode";
        } else {
            modeNameTextView.setText("Main mode");
        }


        buttonFirstAnswer = findViewById(R.id.button_main_mode_first_answer);
        buttonFirstAnswer.setText("to");
        buttonSecondAnswer = findViewById(R.id.button_main_mode_second_answer);
        buttonSecondAnswer.setText("start");
        buttonThirdAnswer = findViewById(R.id.button_main_mode_third_answer);
        buttonThirdAnswer.setText("click");
        buttonFourthAnswer = findViewById(R.id.button_main_mode_fourth_answer);
        buttonFourthAnswer.setText("on");

        buttonFirstAnswer.setOnClickListener(this);
        buttonSecondAnswer.setOnClickListener(this);
        buttonThirdAnswer.setOnClickListener(this);
        buttonFourthAnswer.setOnClickListener(this);


        buttonStart.setOnClickListener(this);
        buttonStart.setText("start");
    }

    @Override
    public void onClick(View view) {

        buttonFirstAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonSecondAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonThirdAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonFourthAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));


        Button clickedButton = (Button) view;
        switch (clickedButton.getId()) {
            case R.id.button_main_mode_first_answer:
                buttonFirstAnswer.setBackgroundColor(Color.GRAY);
                selectedButtonId = buttonFirstAnswer.getId();
                break;

            case R.id.button_main_mode_second_answer:
                buttonSecondAnswer.setBackgroundColor(Color.GRAY);
                selectedButtonId = buttonSecondAnswer.getId();
                break;

            case R.id.button_main_mode_third_answer:
                buttonThirdAnswer.setBackgroundColor(Color.GRAY);
                selectedButtonId = buttonThirdAnswer.getId();
                break;

            case R.id.button_main_mode_fourth_answer:
                buttonFourthAnswer.setBackgroundColor(Color.GRAY);
                selectedButtonId = buttonFourthAnswer.getId();
                break;

            case R.id.button_main_mode_start:
                if (clickedButton.getText().equals("start")) {
                    loadNextWord();
                    new AlertDialog.Builder(this, R.style.DialogTheme)
                            .setTitle(selectedModeName)
                            .setMessage("In this game you should select correct translation of shown word. \n Good luck!")
                            .setPositiveButton("Ok", (dialogInterface, i) -> loadButtonText())
                            .setCancelable(false)
                            .show();
                    clickedButton.setText("submit");

                } else {
                    if(selectedButtonId == -1){
                        break;
                    }
                    attempts++;
                    int correctButtonId = getButtonId(correctAnswerButtonId);
                   // dbViewModel.updateShown(currentDBWordTranslationModel);

                    Log.i("algorithmWork", "selected btn id " + selectedButtonId + "\n correct answer btn id: " + correctButtonId);
                    if (getButtonId(correctAnswerButtonId) == selectedButtonId) {  // якщо обрана кнопка є правильним варіантом
                        correctUserAnswers++;
                   //     dbViewModel.updateGuessed(currentDBWordTranslationModel);
                    } else {                                                             // якщо обрано не правильний варіант
                        isMistake = true;
                        Button wrongButton = findViewById(selectedButtonId);
                        Button correctButton = findViewById(correctButtonId);
                        wrongButton.setBackgroundColor(getResources().getColor(R.color.black_red));
                        correctButton.setBackgroundColor(getResources().getColor(R.color.pastel_green));
                        correctButton.setTextColor(getResources().getColor(R.color.black));
                        // додавання слова в помилки
                        WordModelForMainMode currentMistakeWord = new WordModelForMainMode(wordsList.get(currentMainWord).getWord(),
                                wordsList.get(currentMainWord).getTranslation());
                        mistakes.add(currentMistakeWord);
                        wrongButton = null;
                        correctButton = null;
                    }
                    currentMainWord++;    // беремо індекс наступного слова для загадування
                    scoreTextView.setText("score: " + correctUserAnswers + "/" + attempts);
                    if (SELECTED_MODE == 1 && attempts == 10) {

                        String resutltText = " Good work! \n Your result of guessing is "
                                + correctUserAnswers * 100 / attempts + "% ";
                        resutltText += "\n Do you want to try again?";
                        String mistakesButtonName = "Watch Mistakes";
                        if(correctUserAnswers == 10){
                            mistakesButtonName = "No mistakes";
                        }

                        new AlertDialog.Builder(this, R.style.DialogTheme)
                                .setTitle("Result")
                                .setMessage(resutltText)
                                .setPositiveButton("Again", (dialogInterface, i) -> {
                                    attempts = 0;
                                    correctUserAnswers = 0;
                                    scoreTextView.setText("score: " + correctUserAnswers + "/" + attempts);
                                    loadNextWord();
                                    loadButtonText();
                                })
                                .setNeutralButton(mistakesButtonName,(dialogInterface, i) -> {
                                    if(correctUserAnswers == 10){
                                        finish();}
                                    else{
                                        Intent intent = new Intent(this, WatchMistakesActivity.class);
                                        intent.putExtra("List", (Serializable) mistakes);
                                        startActivity(intent);
                                        finish();
                                    }
                                    //Toast.makeText(this, "Open mistakes Activity", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("Finish", (dialogInterface, i) -> finish())
                                .setCancelable(false)
                                .show();
                        break;
                    }if (isMistake) {        // зупинка на 2 секунди
                        disableAllButtons(true);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> {
                            changeButtonColorToNormal();
                            disableAllButtons(false);
                            isMistake = false;
                            loadNextWord();
                            loadButtonText();
                        }, 2000);
                    } else {
                        loadNextWord();
                        loadButtonText();
                    }
                }
                break;
        }
    }

    private void getWordsListFromDb() {
        if (SELECTED_MODE == 1){
            dbViewModel.getWordsListForMainMode().observe(this, new Observer<List<DbWordTranslationModel>>() {
                @Override
                public void onChanged(List<DbWordTranslationModel> dbWordTranslationModels) {
                    wordsList = dbWordTranslationModels;

                }
            });
        } else if (SELECTED_MODE == 2) {
            dbViewModel.getAllWords().observe(this, new Observer<List<DbWordTranslationModel>>() {
                @Override
                public void onChanged(List<DbWordTranslationModel> dbWordTranslationModels) {
                    wordsList = dbWordTranslationModels;
                }
            });
        }
        else
            Toast.makeText(this, "Exception in getting words. \nLearning mode not selected", Toast.LENGTH_SHORT).show();

    }

    private void loadNextWord() {
        if (currentMainWord == wordsList.size()) {
            currentMainWord = 0;
        }
        correctTranslation = wordsList.get(currentMainWord).getTranslation();
        currentDBWordTranslationModel = wordsList.get(currentMainWord);
        getRandomTranslations();
    }

    private void loadButtonText() {
        Log.i("algorithmWork", "--- start of loadButtonText  ---");
        mainWordTextView.setText(wordsList.get(currentMainWord).getWord());
        int[] randomButtonsNums = generateRandomArray(4);
        for (int num = 0; num < randomButtonsNums.length; num++) { // кнопці із випадкового масиву призн. значення із випадкових перекладів

            Log.i("algorithmWork", "random translation in loadBtnText " + randomTranslations[num]);
            Log.i("algorithmWork", "current button in loadBtnText " + randomButtonsNums[num]);

            if (randomTranslations[num].equals(correctTranslation)) {
                correctAnswerButtonId = randomButtonsNums[num];
                Log.i("algorithmWork", "correctAnswerButtonId in loadBtnText: " + randomButtonsNums[0]);
            }
            setButtonText(randomButtonsNums[num], randomTranslations[num]);
        }
        Log.i("algorithmWork", "--- end of loadButtonText  ---");
        getRandomTranslations();
    }


    private void setButtonText(int buttonNum, String currentTranslation) {
        switch (buttonNum) {
            case 0:

                buttonFirstAnswer.setText(currentTranslation);
                Log.i("algorithmWork", "button 1 set " + currentTranslation);
                break;
            case 1:

                buttonSecondAnswer.setText(currentTranslation);
                Log.i("algorithmWork", "button 2 set: " + currentTranslation);
                break;
            case 2:

                buttonThirdAnswer.setText(currentTranslation);
                Log.i("algorithmWork", "button 3 set " + currentTranslation);
                break;
            case 3:

                buttonFourthAnswer.setText(currentTranslation);
                Log.i("algorithmWork", "button 4 set " + currentTranslation);
                break;
        }
    }


    private void getRandomTranslations() {  // повертає першим правильний + 3 випадкових переклада
        randomTranslationsHashSet.clear();
        randomTranslationsHashSet.add(wordsList.get(currentMainWord).getTranslation());
        dbViewModel.getAllWords().observe(this, new Observer<List<DbWordTranslationModel>>() {
            @Override
            public void onChanged(List<DbWordTranslationModel> dbWordTranslationModels) {
                int listSize = dbWordTranslationModels.size();
                int num = 0;

                while (num < 3) {
                    Random random = new Random();
                    int randomNum = random.nextInt(listSize);  // обираємо випадкові переклади із БД
                    String currentTranslation = dbWordTranslationModels.get(randomNum).getTranslation().toString();
                    if (randomTranslationsHashSet.contains(currentTranslation)) {
                        Log.i("arraysWork", "random translation duplicate is: " + currentTranslation);
                    } else {
                        randomTranslationsHashSet.add(currentTranslation);
                        num++;
                        Log.i("arraysWork", "random translation is: " + currentTranslation);
                    }
                }
                randomTranslations = randomTranslationsHashSet.toArray(new String[4]);

            }
        });
    }

    private int getButtonId(int buttonNum) {
        int btnId = -4;
        Log.i("algorithmWork", "getButtonId btn id input: " + buttonNum);
        switch (buttonNum) {
            case 0:
                btnId = buttonFirstAnswer.getId();
                break;
            case 1:
                btnId = buttonSecondAnswer.getId();
                break;
            case 2:
                btnId = buttonThirdAnswer.getId();
                break;
            case 3:
                btnId = buttonFourthAnswer.getId();
                break;
        }
        Log.i("algorithmWork", "getButtonId btn id output: " + btnId);
        return btnId;
    }

    private void changeButtonColorToNormal() {
        buttonFirstAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonFirstAnswer.setTextColor(getResources().getColor(R.color.davys_gray));

        buttonSecondAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonSecondAnswer.setTextColor(getResources().getColor(R.color.davys_gray));

        buttonThirdAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonThirdAnswer.setTextColor(getResources().getColor(R.color.davys_gray));

        buttonFourthAnswer.setBackgroundColor(getResources().getColor(R.color.light_bone));
        buttonFourthAnswer.setTextColor(getResources().getColor(R.color.davys_gray));
    }

    private void disableAllButtons(boolean toDisable){
        if(toDisable){
            buttonFirstAnswer.setEnabled(false);
            buttonSecondAnswer.setEnabled(false);;
            buttonThirdAnswer.setEnabled(false);;
            buttonFourthAnswer.setEnabled(false);;
            buttonStart.setEnabled(false);
        }
        else {
            buttonFirstAnswer.setEnabled(true);
            buttonSecondAnswer.setEnabled(true);;
            buttonThirdAnswer.setEnabled(true);;
            buttonFourthAnswer.setEnabled(true);;
            buttonStart.setEnabled(true);
        }
    }

    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        Random random = new Random();
        for (int i = size - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(array, i, j);
        }

        return array;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}