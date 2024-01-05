package com.example.learning_foreign_words_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learning_foreign_words_app.R;
import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.models.WordModelForMainMode;

import java.util.ArrayList;
import java.util.List;

public class MistakesAdapter extends RecyclerView.Adapter<MistakesAdapter.MistakeWordHolder> {

    private List<WordModelForMainMode> wordsList = new ArrayList<>();

    @NonNull
    @Override
    public MistakeWordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_mistakes_word_translation, parent, false);
        return new MistakesAdapter.MistakeWordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MistakeWordHolder holder, int position) {

        WordModelForMainMode currentWordTranslation = wordsList.get(position);
        holder.textViewMistakeWord.setText(currentWordTranslation.getWord());
        holder.textViewMistakeTranslation.setText(currentWordTranslation.getTranslation());

    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    public void setWordsList(List<WordModelForMainMode> wordsList){
        this.wordsList = wordsList;
        notifyDataSetChanged();
    }

    class MistakeWordHolder extends RecyclerView.ViewHolder{
        private TextView textViewMistakeWord;
        private TextView textViewMistakeTranslation;
        private TextView textViewMistakeDefinition;

        public MistakeWordHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewMistakeWord = itemView.findViewById(R.id.watch_mistakes_text_view_word_card_view);
            this.textViewMistakeTranslation = itemView.findViewById(R.id.watch_mistakes_text_view_translation_card_view);

        }
    }
}
