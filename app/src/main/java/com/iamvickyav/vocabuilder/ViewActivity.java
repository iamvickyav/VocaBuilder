package com.iamvickyav.vocabuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ViewActivity extends AppCompatActivity {

    Button showData;
    ExpandableListView listView;
    ExpandableListAdapter listAdapter;
    ProgressBar progressBar;
    List<String> listData;
    HashMap<String, List<String>> subListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listView = findViewById(R.id.list_view);
        showData = findViewById(R.id.showData);
        progressBar = findViewById(R.id.progressBar);

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData = new ArrayList<>();
                subListData = new HashMap<>();
                new AsyncFetch().execute();
            }
        });
    }

    void displayListView() {
        listAdapter = new ExpandableListAdapter(getApplicationContext(), listData, subListData);
        listView.setAdapter(listAdapter);
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
                                    List<String> valueList = new ArrayList<>();
                                    valueList.add("Tamil    :   " + lexicon.tamil);
                                    valueList.add("Meaning  :   " + lexicon.meaning);
                                    valueList.add("Example  :   " + lexicon.example);
                                    subListData.put(lexicon.word, valueList);
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
}