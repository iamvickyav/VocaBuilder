package com.iamvickyav.vocabuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iamvickyav.vocabuilder.model.Lexicon;

import static com.iamvickyav.vocabuilder.util.VocaConstants.ADD_SUCCESSFULL;
import static com.iamvickyav.vocabuilder.util.VocaConstants.COLLECTION;
import static com.iamvickyav.vocabuilder.util.VocaConstants.CONNECTION_ISSUE;
import static com.iamvickyav.vocabuilder.util.VocaConstants.WORD_MANDATORY;

public class AddActivity extends AppCompatActivity {

    EditText word, meaning, tamil, example;
    Button addToDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        word = findViewById(R.id.word);
        meaning = findViewById(R.id.meaning);
        tamil = findViewById(R.id.tamil);
        example = findViewById(R.id.example);
        addToDB = findViewById(R.id.addToDB);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wordString = word.getText().toString();
                if(!wordString.isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Lexicon lexicon = prepareData();
                    Task task = db.collection(COLLECTION).document(wordString.toUpperCase()).set(lexicon);

                    task.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),ADD_SUCCESSFULL, Toast.LENGTH_SHORT).show();
                                clearInputBoxes();
                                word.requestFocus();
                            } else {
                                Toast.makeText(getApplicationContext(),CONNECTION_ISSUE, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),WORD_MANDATORY, Toast.LENGTH_SHORT).show();
                    word.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearInputBoxes() {
        word.setText("");
        meaning.setText("");
        tamil.setText("");
        example.setText("");
    }

    private Lexicon prepareData() {
        Lexicon lexicon = new Lexicon();
        lexicon.meaning = meaning.getText().toString();
        lexicon.tamil = tamil.getText().toString();
        lexicon.example = example.getText().toString();
        lexicon.word = word.getText().toString();
        return lexicon;
    }
}
