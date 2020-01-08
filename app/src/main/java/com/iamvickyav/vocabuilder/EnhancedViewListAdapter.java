package com.iamvickyav.vocabuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iamvickyav.vocabuilder.model.Lexicon;

import java.util.List;
import java.util.Map;

public class EnhancedViewListAdapter extends RecyclerView.Adapter<EnhancedViewListAdapter.WordViewHolder>  {

    private Context context;
    private List<String> wordList;
    private Map<String, Lexicon> wordDefinitionMap;

    EnhancedViewListAdapter(Context context, List<String> wordList, Map<String, Lexicon> wordDefinitionMap) {
        this.context = context;
        this.wordList = wordList;
        this.wordDefinitionMap = wordDefinitionMap;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_enhanced_list_item, parent, false);
        WordViewHolder wordViewHolder = new WordViewHolder(view);
        return wordViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder wordViewHolder, int position) {
        Lexicon lexicon = wordDefinitionMap.get(wordList.get(position));
        wordViewHolder.cardWord.setText(lexicon.word);
        wordViewHolder.cardWordMeaning.setText(lexicon.meaning);
        wordViewHolder.cardWordTamil .setText(lexicon.tamil);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView cardWord;
        TextView cardWordMeaning;
        TextView cardWordTamil;

        WordViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.enhanced_card_view);
            cardWord = itemView.findViewById(R.id.card_word);
            cardWordMeaning = itemView.findViewById(R.id.card_word_meaning);
            cardWordTamil = itemView.findViewById(R.id.card_word_tamil);
        }
    }
}
