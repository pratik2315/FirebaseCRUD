package com.example.firebasetorecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etAge, etRoll, etDept;
    private Button addData, uData;
    TextView sampleTv, sample2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("student");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etName = (EditText) findViewById(R.id.name);
        etAge = (EditText) findViewById(R.id.age);
        etRoll = (EditText) findViewById(R.id.rollID);
        etDept = (EditText) findViewById(R.id.dept);
        addData = (Button) findViewById(R.id.add);
        uData = (Button) findViewById(R.id.update);
        sampleTv = (TextView) findViewById(R.id.sample);
        sample2 = (TextView) findViewById(R.id.sample2);
        writeData();
        readData();
        updateData();
    }

    public void writeData(){

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String rollId = etRoll.getText().toString().trim();
                String dept = etDept.getText().toString().trim();

                Student student = new Student(name, age, rollId);

                myRef.child(dept).setValue(student);
                Toast.makeText(MainActivity.this, "Values inserted", Toast.LENGTH_SHORT).show();

                etName.setText("");
                etAge.setText("");
                etRoll.setText("");
                etDept.setText("");
            }
        });
    }

    private void readData(){
        DatabaseReference node = database.getReference("student").child("CO");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map map = (Map) snapshot.getValue();
                    String name = map.get("name").toString();
                    String age = map.get("age").toString();


                    sampleTv.setText(name);
                    sample2.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData(){
        uData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference node = database.getReference("student").child("dept");
                String updateName = etName.getText().toString();
                String updateAge = etAge.getText().toString();
                String updateRoll = etRoll.getText().toString();

                HashMap map = new HashMap();
                map.put("name", updateName);
                map.put("age", updateAge);
                map.put("rollNo", updateRoll);

                node.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(MainActivity.this, "Values updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}