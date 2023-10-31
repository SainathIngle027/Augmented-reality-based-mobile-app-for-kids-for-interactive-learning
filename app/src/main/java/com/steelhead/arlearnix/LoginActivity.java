package com.steelhead.arlearnix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    CardView loginbt;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        loginbt = findViewById(R.id.loginbt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }
    private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent , 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                HomePage();
            }
            catch (ApiException e)
            {
                Toast.makeText(this,"Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void HomePage() {
        finish();
        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Delete cache files here
        deleteCacheFiles();
    }
    private void deleteCacheFiles() {
        try {
            File cacheDir = getCacheDir();
            if (cacheDir.isDirectory()) {
                String[] files = cacheDir.list();
                for (String file : files) {
                    File cacheFile = new File(cacheDir, file);
                    if (cacheFile.delete()) {
                        Log.d("CacheDeletion", "Deleted cache file: " + file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}