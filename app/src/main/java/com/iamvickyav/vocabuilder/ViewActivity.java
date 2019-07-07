package com.iamvickyav.vocabuilder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    List<String> documentId = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listView = findViewById(R.id.list_view);
        showData = findViewById(R.id.showData);

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Toast.makeText(getApplicationContext(), "Loading..", Toast.LENGTH_SHORT).show();

                db.collection(COLLECTION)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        documentId.add(document.getId());
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listview_element, documentId);
                                    listView.setAdapter(arrayAdapter);
                                } else {
                                    Log.d("SEARCH", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }
}
