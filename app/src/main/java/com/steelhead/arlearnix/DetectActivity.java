package com.steelhead.arlearnix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DetectActivity extends AppCompatActivity {

    ImageView preimg;
    private Uri imguri;
    Uri path ;
    CardView ImgPicker,resultcard;
    TextView result,cardname;

    private  String objpass ;
    Button knowinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);
        getPremission();
        resultcard = findViewById(R.id.resultcard);
        preimg =findViewById(R.id.preimg);
        ImgPicker = findViewById(R.id.ImgPicker);
        result=findViewById(R.id.result);
        knowinfo=findViewById(R.id.knowinfo);
        cardname=findViewById(R.id.cardname);
        knowinfo.setVisibility(View.INVISIBLE);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        cardname.setText("Select Image");
        result.setText("Click on above image");

        ImgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardname.setText("");

                ImagePicker.with(DetectActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(720)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });



        knowinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objpass != null) {
                    checkIfObjectExistsInFirebase(objpass);
                } else {
                    // Handle the case where objpass is null
                    // For example, show a message or take appropriate action
                }
            }

            private void checkIfObjectExistsInFirebase(String objpass) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference objectRef = database.getReference(objpass);

                objectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The object exists in the database
                            // Proceed to InfoActivity
                            finish();
                            Intent intent = new Intent(DetectActivity.this, InfoActivity.class);
                            intent.putExtra("passname", objpass);
                            startActivity(intent);
                        } else {
                            // The object does not exist in the database
                            // Handle the case where the object is not found
                            Toast.makeText(DetectActivity.this, "Object not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        // Handle the error
                        Toast.makeText(DetectActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imguri = data.getData();
        preimg.setImageURI(imguri);
        addToStorage(imguri);
    }
    private void addToStorage(Uri uri) {
        if (uri == null) {
            // Handle the case where uri is null (e.g., user canceled image selection)
            cardname.setText("To Select Image");
            result.setText("Click on above Card");
            Toast.makeText(this, "Image selection was canceled", Toast.LENGTH_SHORT).show();
            return;
        }

        cardname.setText("Finding Object....");
        result.setText("Wait for a while");

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("MyImage");
        final StorageReference imageName = reference.child("Image1234");

        imageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        path = uri;
                        cardname.setText("Image Uploaded");
                        callpython();
                    }
                });
            }
        });
    }

    private void callpython() {
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("PyScript");
        PyObject obj = pyobj.callAttr("main",path);
        cardname.setText("Object Detected As");
        objpass = obj.toString();
        result.setText(objpass.toUpperCase());
        knowinfo.setVisibility(View.VISIBLE);
    }

    private void getPremission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(DetectActivity.this,new String[] {Manifest.permission.CAMERA},11);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11)
        {
            if(grantResults.length>0)
            {
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    this.getPremission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void onBackPressed()
    {
        finish();
        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}