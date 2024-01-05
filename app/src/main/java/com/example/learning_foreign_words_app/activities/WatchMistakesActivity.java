package com.example.learning_foreign_words_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.adapters.MistakesAdapter;
import com.example.learning_foreign_words_app.adapters.WordDBAdapter;
import com.example.learning_foreign_words_app.listeners.RecyclerTouchListener;
import com.example.learning_foreign_words_app.models.WordModelForMainMode;

import java.util.ArrayList;
import java.util.List;

public class WatchMistakesActivity extends AppCompatActivity {

    private Button buttonToMenu, buttonRestart;
    private List<WordModelForMainMode> words = new ArrayList<>();
    public static final String MAIN_MODE_LEARNING_ACTIVITY =
            "learning_foreign_words_app.activities.LearningModsActivity.MAIN_MODE_LEARNING_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_mistakes);
        doInitialisation();
    }

    private void doInitialisation(){
        Intent intent = getIntent();
        words = (List<WordModelForMainMode>) intent.getSerializableExtra("List");

        MistakesAdapter mistakesAdapter = new MistakesAdapter();
        mistakesAdapter.setWordsList(words);
        recyclerViewInit(mistakesAdapter);

        buttonRestart = findViewById(R.id.restart_mistakes_btn);
        buttonToMenu = findViewById(R.id.back_to_menu_mistakes_btn);
        clickOnButton();
    }

    private void recyclerViewInit(MistakesAdapter adapter){
        RecyclerView recyclerView = findViewById(R.id.mistakes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                //DbWordTranslationModel wordModel = adapter.getModelByPosition(position);
//
//                Intent intent = new Intent(MainActivity.this, WordExtrasFromDbActivity.class);
//
//                intent.putExtra(ID_WORD_FROM_DB_EXTRA, adapter.getModelByPosition(position).getId());
//                intent.putExtra(WORD_FROM_DB_EXTRA,adapter.getModelByPosition(position).getWord());
//                intent.putExtra(TRANSLATION_FROM_DB_EXTRA, adapter.getModelByPosition(position).getTranslation());
//                intent.putExtra(DEFINITION_FROM_DB_EXTRA, adapter.getModelByPosition(position).getDefinition());
//                intent.putExtra(GUESSED_FROM_DB_EXTRA, adapter.getModelByPosition(position).getGuessed());  // change later for guessed/wasShown
//                intent.putExtra(WAS_SHOWED_FROM_DB_EXTRA, adapter.getModelByPosition(position).getShown());
//
//                Toast.makeText(MainActivity.this, "Clicked on position " + adapter.getModelByPosition(position).getWord(), Toast.LENGTH_SHORT).show();
//
//                startActivityForResult(intent, DELETE_WORD_FROM_DB_REQUEST);  // starts words_extras_activity
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

    }

    private void clickOnButton(){
        buttonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchMistakesActivity.this, LearningModsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchMistakesActivity.this, MainModeLearningActivity.class);
                intent.putExtra(MAIN_MODE_LEARNING_ACTIVITY, 1);
                startActivity(intent);
                finish();
            }
        });
    }


}