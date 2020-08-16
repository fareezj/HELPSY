package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminPublishNewsActivity extends AppCompatActivity {

    private String NewsTitle, NewsAuthor, NewsContent, saveCurrentDate, saveCurrentTime;
    private EditText InputNewsTitle, InputNewsAuthor, InputNewsContent;
    private ImageView InputNewsImage;
    private Button PublishNewsButton;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private ProgressDialog loadingBar;
    private String EventRandomKey, downloadImageUrl;
    private StorageReference NewsImagesRef;
    public String eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_publish_news);

        NewsImagesRef = FirebaseStorage.getInstance().getReference().child("News Images");

        InputNewsImage   =    findViewById(R.id.select_news_image);
        InputNewsTitle   =    findViewById(R.id.input_news_title);
        InputNewsAuthor   =    findViewById(R.id.input_news_author);
        InputNewsContent   =    findViewById(R.id.input_news_content);
        PublishNewsButton   =    findViewById(R.id.publish_news_btn);
        loadingBar = new ProgressDialog(this);

        InputNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        PublishNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateEventData();
            }
        });

    }


    //OPEN GALLERY AND SAVE EVENT IMAGE
    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode ==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            InputNewsImage.setImageURI(ImageUri);
        }

    }


    //EVENT DATA VALIDATION
    private void ValidateEventData(){
        NewsTitle  = InputNewsTitle.getText().toString();
        NewsAuthor  = InputNewsAuthor.getText().toString();
        NewsContent   = InputNewsContent.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Event Image is mandatory", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(NewsTitle))
        {
            Toast.makeText(this, "Please write event name", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(NewsAuthor))
        {
            Toast.makeText(this, "Please put event date", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(NewsContent))
        {
            Toast.makeText(this, "Please write event pax", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            StoreEventInfo();
        }
    }

    //STORE EVENT INFO
    private void StoreEventInfo(){

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait while we are adding the new product...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        EventRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = NewsImagesRef.child(ImageUri.getLastPathSegment() + EventRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminPublishNewsActivity.this, "Error: " + message, Toast.LENGTH_SHORT ).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminPublishNewsActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT ).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminPublishNewsActivity.this, "Getting Event Image Url Successfully", Toast.LENGTH_SHORT ).show();

                            SaveEventInfoToDatabase();


                        }
                    }
                });
            }
        });
    }

    private void SaveEventInfoToDatabase()
    {
        final DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("News List")
                .child("Item")
                .child(EventRandomKey);

        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("nid", EventRandomKey);
        eventMap.put("publishDate", saveCurrentDate);
        eventMap.put("publishTime", saveCurrentTime);
        eventMap.put("NewsImage", downloadImageUrl);
        eventMap.put("NewsTitle", NewsTitle);
        eventMap.put("NewsAuthor", NewsAuthor);
        eventMap.put("NewsContent", NewsContent);

        EventRef.updateChildren(eventMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminPublishNewsActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminPublishNewsActivity.this, "Event is added successfully", Toast.LENGTH_SHORT ).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminPublishNewsActivity.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                        }
                    }
                });
    }





}
