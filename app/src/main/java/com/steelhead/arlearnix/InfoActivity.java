package com.steelhead.arlearnix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    TextView objname , objinfo;
    private String passname = null , passinfo;

    ImageView objimg;

    Button arpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        objname= findViewById(R.id.objname);
        objimg = findViewById(R.id.objimg);
        objinfo =findViewById(R.id.objinfo);
        arpage = findViewById(R.id.arpage);

        passname = getIntent().getStringExtra("passname");
        objname.setText(passname.toUpperCase());

        passinfo = passname+"info";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(passname);
        DatabaseReference text = database.getReference(passinfo);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = null;
                if (dataSnapshot.exists()) {
                    // The object exists in the database
                    // Perform your logic here
                    value = dataSnapshot.getValue(String.class);

                } else {
                    // The object does not exist in the database
                    // Perform your logic here
                    value= "https://firebasestorage.googleapis.com/v0/b/ar-learnix.appspot.com/o/Info%20Images%2Fnotfound.png?alt=media&token=a20ef24f-8f33-4039-8e98-737b4148f4d4";

                }

                Picasso.get().load(value).into(objimg);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        text.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = null;
                if (dataSnapshot.exists())
                {
                    value = dataSnapshot.getValue(String.class);
                }
                else
                {
                    value = "No Information Found....!!!!";
                }
                objinfo.setText(value.toUpperCase());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        arpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(InfoActivity.this,ArActivity.class);
                intent.putExtra("objname",passname);
                startActivity(intent);
                Intent intent1 = new Intent(getApplicationContext(),InfoActivity.class);
                startActivity(intent1);
            }
        });

    }
    public void onBackPressed()
    {
            finish();
            Intent intent =new Intent(getApplicationContext(),DetectActivity.class);
            startActivity(intent);
    }
}




