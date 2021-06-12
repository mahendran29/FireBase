package com.example.firedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE="title";
    private static final String KEY_DESCRIPTION="description";

    EditText editTextTitle;
    EditText editTextDescription;
    TextView textViewData;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference noteBook = db.collection("NoteBook");
    DocumentReference noteRef = db.collection("NoteBook").document("Note1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle=findViewById(R.id.edit_text_title);
        editTextDescription=findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
    }

 //   @Override
//    protected void onStart() {
//        super.onStart();
//        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable  DocumentSnapshot documentSnapshot, @Nullable  FirebaseFirestoreException error) {
//                if(documentSnapshot.exists())
//                {
////                    String title = documentSnapshot.getString(KEY_TITLE);
////                    String description = documentSnapshot.getString(KEY_DESCRIPTION);
////                    textViewData.setText("Title:"+title+"\nDescription"+description);
//                    Note note = documentSnapshot.toObject(Note.class);
//                    String title =note.getTitle();
//                    String description=note.getDescription();
//                    textViewData.setText("Title:"+title+"\nDescription"+description);
//                }
//            }
//        });
//    }

    public void saveNote(View view)
    {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

//        Map<String,Object> note = new HashMap<>();
//        note.put(KEY_TITLE,title);
//        note.put(KEY_DESCRIPTION,description);

        Note note = new Note(title,description);


        noteRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,"ADDED",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(MainActivity.this,"FAILED",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDesc(View view)
    {
        String description = editTextDescription.getText().toString();
        Map<String,Object> note= new HashMap<>();
        note.put(KEY_DESCRIPTION,description);
        noteRef.update(note);
    }

    public void deleteDesc(View view)
    {
        Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION, FieldValue.delete());

        noteRef.update(note);
    }

    public void deleteNote(View view)
    {
        noteRef.delete();
    }

    public void getNote(View view)
    {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
//                    String title = documentSnapshot.getString(KEY_TITLE);
//                    String description = documentSnapshot.getString(KEY_DESCRIPTION);

                    Note note = documentSnapshot.toObject(Note.class);

                    String title =note.getTitle();
                    String description=note.getDescription();
                    textViewData.setText("Title:"+title+"\nDescription"+description);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Document not Available",Toast.LENGTH_SHORT).show();
                }
            }
           })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void addNotes(View view)
    {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();


        Note note = new Note(title,description);

        noteBook.add(note);
    }

    public void loadNotes(View view)
    {
       noteBook.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               String data="";
              for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
              {
                  Note note =documentSnapshot.toObject(Note.class);
                  String title=note.getTitle();
                  String description=note.getDescription();
                  data+="Title"+title+"\nDescription:"+description+"\n\n";

              }

              textViewData.setText(data);
           }
       });
    }


}