package com.example.learning_foreign_words_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;

public class LearningModsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonMainMode, buttonToDictionary, buttonRedundantMode,
            buttonDefinitionMode, buttonRepetitionMode;

    public static final String MAIN_MODE_LEARNING_ACTIVITY =
            "learning_foreign_words_app.activities.LearningModsActivity.MAIN_MODE_LEARNING_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_mods);
        doInitialization();
    }

    private void doInitialization(){
        buttonMainMode = findViewById(R.id.button_main_mode);
        buttonToDictionary = findViewById(R.id.button_to_dictionary);
        buttonRedundantMode = findViewById(R.id.button_find_redundant_mode);
        buttonDefinitionMode = findViewById(R.id.button_definition_mode);
        buttonRepetitionMode = findViewById(R.id.button_repetition_mode);

        buttonMainMode.setOnClickListener(this);
        buttonToDictionary.setOnClickListener(this);
        buttonRedundantMode.setOnClickListener(this);
        buttonDefinitionMode.setOnClickListener(this);
        buttonRepetitionMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Button selectedButton = (Button) view;
        switch (selectedButton.getId()){
            case R.id.button_main_mode:
                //Toast.makeText(this, "Main mode selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(LearningModsActivity.this, MainModeLearningActivity.class);
                intent.putExtra(MAIN_MODE_LEARNING_ACTIVITY,1);
                startActivity(intent);
                break;
            case R.id.button_to_dictionary:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_find_redundant_mode:
                intent = new Intent(LearningModsActivity.this, AudioModeLearningActivity.class);
                //Toast.makeText(this, "Redundant mode selected", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_definition_mode:
                //Toast.makeText(this, "Definition mode selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(LearningModsActivity.this, DefinitionModeLearningActivity.class);
                startActivity(intent);
                break;
            case R.id.button_repetition_mode:
                //Toast.makeText(this, "Repetition mode selected", Toast.LENGTH_SHORT).show();
                intent = new Intent(LearningModsActivity.this, MainModeLearningActivity.class);
                intent.putExtra(MAIN_MODE_LEARNING_ACTIVITY,2);
                startActivity(intent);
                break;
        }
    }
}