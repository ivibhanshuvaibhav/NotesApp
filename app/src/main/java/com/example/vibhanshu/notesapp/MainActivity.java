package com.example.vibhanshu.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    EditText title, note;
    Button addImage, addNote;
    ImageView imageView;
    private static final int requestCodeGallery = 0;
    private static final int requestCodeCamera = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText) findViewById(R.id.title);

        note = (EditText) findViewById(R.id.note);

        imageView = (ImageView) findViewById(R.id.image_view);

        addImage = (Button) findViewById(R.id.image_button);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Choose a source...");
                alertDialogBuilder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(gallery, requestCodeGallery);
                                break;
                            case 1:
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                                File output=new File(dir, "CameraTemp.jpeg");
//                                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
//                                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
////                                imagesFolder.mkdirs(); // <----
//                                File image = new File(imagesFolder, "image_001.jpg");
//                                Uri uriSavedImage = Uri.fromFile(image);
//                                camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                                saveImage(camera);
                                startActivityForResult(camera, requestCodeCamera);
                                break;
                        }
                    }

                });
                alertDialogBuilder.show();
            }
        });

        addNote = (Button) findViewById(R.id.note_button);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Note added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveImage(Intent camera){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd|hh:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String name = "CameraTemp".concat(sdf.format(timestamp)).concat(".jpeg");
        File output = new File(dir, name);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
          case 0:
              if (resultCode == RESULT_OK) {
                  Uri selectedImage = imageReturnedIntent.getData();
                  imageView.setImageURI(selectedImage);
              }
              break;
          case 1:
              if (resultCode == RESULT_OK) {
                  Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                  imageView.setImageBitmap(photo);
              }
              break;
        }
    }
}
