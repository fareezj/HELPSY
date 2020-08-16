package com.fareez.helpsy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fareez.helpsy.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminPublishEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private String EventName, EventDate, EventPax, EventDesc, saveCurrentDate, saveCurrentTime;
    private EditText InputEventName, InputEventPax, InputEventDescription;
    private ImageView InputEventImage;
    private Button AddNewEventButton;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private ProgressDialog loadingBar;
    private String EventRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    public String eventID;

    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_publish_event);



        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Event Images");

        InputEventImage         = findViewById(R.id.select_event_image);
        AddNewEventButton       = findViewById(R.id.add_new_event_btn);
        InputEventName          = findViewById(R.id.input_event_name);
        dateText                = findViewById(R.id.date_text);
        InputEventPax           = findViewById(R.id.input_event_pax);
        InputEventDescription   = findViewById(R.id.input_event_description);
        loadingBar = new ProgressDialog(this);

        InputEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        AddNewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ValidateEventData();
            }
        });

        findViewById(R.id.event_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dateText.setText(currentDateString);
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
            InputEventImage.setImageURI(ImageUri);
        }

    }

    //EVENT DATA VALIDATION
    private void ValidateEventData(){
        EventName  = InputEventName.getText().toString();
        EventDate  = dateText.getText().toString();
        EventPax   = InputEventPax.getText().toString();
        EventDesc  = InputEventDescription.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Event Image is mandatory", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(EventName))
        {
            Toast.makeText(this, "Please write event name", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(EventDate))
        {
            Toast.makeText(this, "Please put event date", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(EventPax))
        {
            Toast.makeText(this, "Please write event pax", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(EventDesc))
        {
            Toast.makeText(this, "Please write event desc", Toast.LENGTH_SHORT ).show();
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

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + EventRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminPublishEventActivity.this, "Error: " + message, Toast.LENGTH_SHORT ).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminPublishEventActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT ).show();

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

                            Toast.makeText(AdminPublishEventActivity.this, "Getting Event Image Url Successfully", Toast.LENGTH_SHORT ).show();

                            SaveEventInfoToDatabase();


                        }
                    }
                });
            }
        });
    }


    private void SaveEventInfoToDatabase()
    {
        final DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("Event List")
                .child("Item")
                .child(EventRandomKey);

        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("eid", EventRandomKey);
        eventMap.put("publishDate", saveCurrentDate);
        eventMap.put("publishTime", saveCurrentTime);
        eventMap.put("eventImage", downloadImageUrl);
        eventMap.put("eventName", EventName);
        eventMap.put("eventDate", EventDate);
        eventMap.put("eventPax", EventPax);
        eventMap.put("eventDesc", EventDesc);

        EventRef.updateChildren(eventMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminPublishEventActivity.this, AdminHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminPublishEventActivity.this, "Event is added successfully", Toast.LENGTH_SHORT ).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminPublishEventActivity.this, "Error :" + message, Toast.LENGTH_SHORT ).show();

                        }
                    }
                });
    }



}
