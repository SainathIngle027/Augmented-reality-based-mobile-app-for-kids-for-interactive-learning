package com.steelhead.arlearnix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SanctuaryActivity extends AppCompatActivity {

    String bird = null;
    CardView crow , eagle , hen , macaw , owl ,parrot , peacock , pigeon , sparrow , vulture ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanctuary);
        crow = findViewById(R.id.crow);
        eagle = findViewById(R.id.eagle);
        hen = findViewById(R.id.hen);
        macaw = findViewById(R.id.macaw);
        owl  = findViewById(R.id.owl);
        parrot = findViewById(R.id.parrot);
        peacock = findViewById(R.id.peacock);
        pigeon = findViewById(R.id.pigeon);
        sparrow = findViewById(R.id.sparrow);
        vulture=findViewById(R.id.vulture);

        crow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "crow";
                startbirdAr();
            }
        });
        eagle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "eagle";
                startbirdAr();
            }
        });
        hen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "hen";
                startbirdAr();
            }
        });
        macaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "macaw";
                startbirdAr();
            }
        });
        owl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "owl";
                startbirdAr();
            }
        });
        parrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "parrot";
                startbirdAr();
            }
        });
        peacock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "peacock";
                startbirdAr();
            }
        });
        pigeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "pigeon";
                startbirdAr();
            }
        });
        sparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "sparrow";
                startbirdAr();
            }
        });
        vulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bird = "vulture";
                startbirdAr();
            }
        });
    }

    private void startbirdAr() {
        finish();
        String o = bird;
        Intent intent = new Intent(SanctuaryActivity.this,ArActivity.class);
        intent.putExtra("birdname",o);
        startActivity(intent);
    }
    public void onBackPressed()
    {

            finish();
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
    }
}