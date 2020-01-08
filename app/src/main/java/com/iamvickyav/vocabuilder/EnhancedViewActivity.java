package com.iamvickyav.vocabuilder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iamvickyav.vocabuilder.model.Lexicon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.iamvickyav.vocabuilder.util.VocaConstants.COLLECTION;

public class EnhancedViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<String> listData = new ArrayList<>();
    HashMap<String, Lexicon> subListData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enhanced_list);

        recyclerView = findViewById(R.id.enhanced_list_view);
        progressBar = findViewById(R.id.progressBar_loading_words);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new AsyncFetch().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EnhancedViewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class AsyncFetch extends AsyncTask<Void, Void, Void> {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            db.collection(COLLECTION)
                    .orderBy("word", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Lexicon lexicon = document.toObject(Lexicon.class);
                                    listData.add(lexicon.word);
                                    subListData.put(lexicon.word, lexicon);
                                }
                                Log.d("AsyncFetch", "Fetch Success " + listData.size());
                                postFetch();
                            } else {
                                Log.e("AsyncFetch", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            return null;
        }

        private void postFetch(){
            progressBar.setVisibility(View.GONE);
            displayListView();
        }
    }

    void displayListView() {
        EnhancedViewListAdapter adapter = new EnhancedViewListAdapter(getApplicationContext(), listData, subListData);
        recyclerView.setAdapter(adapter);
    }
}
