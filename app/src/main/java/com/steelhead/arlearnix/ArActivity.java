package com.steelhead.arlearnix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class ArActivity extends AppCompatActivity {

    String Armodel=null, information=null,animal = null , bird = null , obj=null;
    Button back;
    private int placed =0;

    private int speck = 0;

    ImageView speckimg;

    private boolean isSpeaking = false;
    TextToSpeech tts;
    CardView speackbt;
    TextView aniname;

    private ArFragment arFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        aniname = findViewById(R.id.objtitle);
        speckimg = findViewById(R.id.speckimg);
        back =findViewById(R.id.back);
        speackbt = findViewById(R.id.speakbt);

        animal = getIntent().getStringExtra("animalname");
        bird = getIntent().getStringExtra("birdname");
        obj = getIntent().getStringExtra("objname");

        if(bird==null && obj==null)
        {
            Armodel=animal;
        }
        if(animal==null && obj==null)
        {
            Armodel = bird;
        }
        if(animal==null && bird==null)
        {
            Armodel = obj;
        }

        aniname.setText(Armodel.toUpperCase());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Armodel=null; information=null;animal = null ; bird = null ; obj=null;
                backpage();
            }
        });
        FirebaseApp.initializeApp(this);
        //download model form firebase call function
        downloadmodel();
        fetchdata();

        speackbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSpeaking && speck==0)
                {
                    speaktext();
                    speck++;
                    speckimg.setImageResource(R.drawable.nosound);
                }
                else {
                    speckimg.setImageResource(R.drawable.sound);
                    destorytts();
                    speck--;

                }

            }
        });

    }

    private void backpage() {

        destorytts();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    private void speaktext() {

        if(!isSpeaking  && information!=null)
        {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status  == TextToSpeech.SUCCESS)
                    {
                        tts.setLanguage(Locale.getDefault());
                        tts.setSpeechRate(0.8f);
                        tts.speak(information,TextToSpeech.QUEUE_ADD,null);
                    }
                    else {
                        // Handle initialization error
                    }


                }
            });
            // Set an OnUtteranceProgressListener to monitor the TTS engine state
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    isSpeaking = true;
                    speckimg.setImageResource(R.drawable.nosound);
                }
                @Override
                public void onDone(String utteranceId) {
                    isSpeaking = false;
                    speckimg.setImageResource(R.drawable.nosound);

                }
                @Override
                public void onError(String utteranceId) {
                    isSpeaking = false;
                    speckimg.setImageResource(R.drawable.nosound);
                }
            });
        }
    }

    private void fetchdata() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String modelinfo = Armodel+"info";
        DatabaseReference animalinformation = database.getReference(modelinfo);

        animalinformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                information = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private ModelRenderable renderable;
    private void downloadmodel() {
        String glbfile = Armodel+".glb";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(glbfile);

        try {
            File file = File.createTempFile(Armodel, "glb");

            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    buildModel(file);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void buildModel(File file)
    {

        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show();
                    renderable = modelRenderable;
                });
        startAr();
    }

    private void startAr() {
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
            andy.getScaleController().setMinScale(0.1f);

            // Set the initial local position (adjust these values as needed)
            andy.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));

            andy.setParent(anchorNode);
            andy.setRenderable(renderable);
            andy.select();
            if (placed>=1)
            {
                anchor.detach();
            }
            placed++;
        });
    }

    public void onBackPressed()
    {
        destorytts();

        finish();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private void destorytts() {
            // Stop the Text-to-Speech engine
        if (tts != null) {
            // Stop the Text-to-Speech engine
            tts.stop();
            tts.shutdown();
            tts = null;  // Set tts to null to release the reference
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            destorytts();
        }
    }
}