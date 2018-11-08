package com.example.tornado.imagepicker;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int IMAGE_COUNT = 0;
    private final int REQUEST_CHOOSE_IMAGE_GALLERY = 35;
    private final int REQUEST_CHOOSE_IMAGE_CAMERA = 73;
    private final int REQUEST_CHOOSE_IMAGE_CROPP = 74;
    Uri uri;

    java.io.File File;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView)findViewById(R.id.info_register_imageView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Dialog dialog = new Dialog(MainActivity.this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("");
                    builder.setItems(new CharSequence[]{" انتخاب عکس"},
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    IMAGE_COUNT = 1;
                                    switch (which) {
                                        case 0:
                                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CHOOSE_IMAGE_GALLERY);
                                                    return;
                                                } else {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CHOOSE_IMAGE_GALLERY);
                                                    return;
                                                }
                                            } else {

                                            }
                                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CHOOSE_IMAGE_CAMERA);
                                                    return;
                                                } else {
                                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CHOOSE_IMAGE_CAMERA);
                                                    return;
                                                }
                                            } else {

                                            }
                                            ImagePicker.pickImage(MainActivity.this);


                                            break;

                                        default:
                                            break;
                                    }
                                }
                            });

                    builder.show();
                    dialog.dismiss();

                } catch (ActivityNotFoundException activityException) {
                    Toast.makeText(MainActivity.this, "خطا در انتخاب عکس", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (IMAGE_COUNT) {
                case 1:
                    try {

//                        if (data != null) {
                        if (requestCode == ImagePicker.REQUEST_PICK) {
                            ImagePicker.beginCrop(this, resultCode, data);
                        }
                        if (requestCode == ImagePicker.REQUEST_CROP) {
                            Bitmap bitmaps = ImagePicker.getImageCropped(this, resultCode, data,
                                    ImagePicker.ResizeType.FIXED_SIZE, 400);

                            img.setImageBitmap(bitmaps);

                            Random r = new Random();
                            int random = r.nextInt(99999 - 00001 + 1) + 00001;
                            java.io.File f = new File(getApplicationContext().getCacheDir(), "Image" + random + ".jpg");
                            f.createNewFile();
                            //Convert bitmap to byte array
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmaps.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] bitmapdata = bos.toByteArray();

                            //write the bytes in file
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                            File = f;
                        }

                    } catch (OutOfMemoryError e) {
                        String message = e.toString();
                        Toast.makeText(MainActivity.this, "خطا در آپلود عکس", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }



}
