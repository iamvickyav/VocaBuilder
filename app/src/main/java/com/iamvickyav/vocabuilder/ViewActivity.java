package com.iamvickyav.vocabuilder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.iamvickyav.vocabuilder.util.VocaConstants.COLLECTION;

public class ViewActivity extends AppCompatActivity {

    Button showData;
    List<String> documentId;
    ListView listView;
    ProgressBar progressBar;
    ArrayAdapter<String> arrayAdapter;

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
                documentId = new ArrayList<>();
                new AsyncFetch().execute();
            }
        });
    }

    void displayListView() {
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listview_element, documentId);
        listView.setAdapter(arrayAdapter);
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
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    documentId.add(document.getId());
                                }
                                Log.d("AsyncFetch", "Fetch Success " + documentId.size());
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