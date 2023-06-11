package com.steelhead.arlearnix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ZooActivity extends AppCompatActivity {

    CardView cat , cow , deer , dog , fox , horse , lion , pandas , tiger , zebra ;

    String animal = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoo);
        cat = findViewById(R.id.cat);
        cow = findViewById(R.id.cow);
        deer = findViewById(R.id.deer);
        dog = findViewById(R.id.dog);
        fox = findViewById(R.id.fox);
        horse = findViewById(R.id.horse);
        lion = findViewById(R.id.lion);
        pandas = findViewById(R.id.panda);
        tiger = findViewById(R.id.tiger);
        zebra = findViewById(R.id.zebra);

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="cat";
                startanimalAr();
            }
        });
        cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="cow";
                startanimalAr();
            }
        });
        deer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="deer";
                startanimalAr();
            }
        });
        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="dog";
                startanimalAr();
            }
        });
        fox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="fox";
                startanimalAr();
            }
        });
        horse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="horse";
                startanimalAr();
            }
        });
        lion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="lion";
                startanimalAr();
            }
        });
        pandas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="pandas";
                startanimalAr();
            }
        });
        tiger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="tiger";
                startanimalAr();
            }
        });
        zebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal ="zebra";
                startanimalAr();
            }
        });
    }

    private void startanimalAr() {
        finish();
        String o = animal;
        Intent intent = new Intent(ZooActivity.this,ArActivity.class);
        intent.putExtra("animalname",o);
        startActivity(intent);
    }
    public void onBackPressed()
    {

            finish();
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

    }
}