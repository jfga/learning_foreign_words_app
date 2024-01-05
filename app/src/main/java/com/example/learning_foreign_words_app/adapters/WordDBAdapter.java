package com.example.learning_foreign_words_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;

import java.util.ArrayList;
import java.util.List;

public class WordDBAdapter extends RecyclerView.Adapter<WordDBAdapter.WordTranslationHolder> {
    private List<DbWordTranslationModel> wordsTranslation = new ArrayList<>();

    @NonNull
    @Override
    public WordTranslationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_dictionary, parent, false);
        return new WordTranslationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordTranslationHolder holder, int position) {
        DbWordTranslationModel currentWordTranslation = wordsTranslation.get(position);
        holder.textViewWord.setText(currentWordTranslation.getWord());
        holder.textViewTranslation.setText(currentWordTranslation.getTranslation());
        holder.textViewDefinition.setText(currentWordTranslation.getDefinition());

    }

    @Override
    public int getItemCount() {
        return wordsTranslation.size();
    }

    public DbWordTranslationModel getModelByPosition(int position){
        return wordsTranslation.get(position);
    }

    public void setWordsTranslation(List<DbWordTranslationModel> wordsTranslation){
        this.wordsTranslation = wordsTranslation;
        notifyDataSetChanged();  // later needed to remove
    }

    class WordTranslationHolder extends RecyclerView.ViewHolder{
        private TextView textViewWord;
        private TextView textViewTranslation;
        private TextView textViewDefinition;

        public WordTranslationHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.watch_mistakes_text_view_word_card_view);
            textViewTranslation = itemView.findViewById(R.id.watch_mistakes_text_view_translation_card_view);
            textViewDefinition = itemView.findViewById(R.id.text_view_definition);
        }
    }
}
