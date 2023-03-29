package ensias.myteam.babytakingcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ensias.myteam.babytakingcare.Models.Baby;


public class AddBabyActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    private TextView deviceId , babyName , babyBirthday ;
    private String deviceIdValue , babyNameValue , babyBirthdayValue ;
    private CircularImageView addImage_iv;

    private AppCompatButton submitButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);
        initialisation() ;
    }

    private void  initialisation()
    {
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        deviceId = findViewById(R.id.device_id_register);
        babyName = findViewById(R.id.baby_name_register);
        babyBirthday = findViewById(R.id.baby_birthday_register);
        addImage_iv = findViewById(R.id.addImage_iv);
        submitButton = findViewById(R.id.submit_add_baby);

        addImage_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoDialog();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData()
    {
        deviceIdValue = deviceId.getText().toString().trim();
        babyNameValue = babyName.getText().toString().trim();
        babyBirthdayValue = babyBirthday.getText().toString().trim();

        if (TextUtils.isEmpty(deviceIdValue)) {
            TastyToast.makeText(this, "Please enter device id !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        }

        if (TextUtils.isEmpty(babyNameValue)) {
            TastyToast.makeText(this, "Please enter your baby name!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        }

        if (TextUtils.isEmpty(babyBirthdayValue)) {
            TastyToast.makeText(this, "Please enter your birthday baby !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            return;
        }

        saveNewBaby();

    }

    private void addPhotoDialog() {
        String[] options = {"Take Photo", "Choose Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (ContextCompat.checkSelfPermission(AddBabyActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(AddBabyActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                addImageFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(AddBabyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                addImageFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }

    private void addImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void addImageFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                addImageFromCamera();
            } else {
                TastyToast.makeText(this, "Please grant the camera permission!", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
            }
        }

        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addImageFromGallery();
            } else {
                TastyToast.makeText(this, "Please grant the storage permission!", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
            }
        }
    }


    private void saveToDirectory(String srcDir, String desDir) throws IOException {
        File src = new File(srcDir);
        File des = new File(desDir, src.getName());

        FileChannel source = null;
        FileChannel destination = null;

        try {
            if (!des.getParentFile().exists()) {
                des.mkdirs();
            }
            if (!des.exists()) {
                des.createNewFile();
            }

            source = new FileInputStream(src).getChannel();
            destination = new FileOutputStream(des).getChannel();
            destination.transferFrom(source, 0, source.size());

            imageUri = Uri.parse(des.getPath());

        } catch (Exception e) {
            TastyToast.makeText(this, "" + e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();

        } finally {
            if (source != null) {
                source.close();

            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    addImage_iv.setImageURI(resultUri);

                    try {
                        saveToDirectory("" + imageUri.getPath(), "" + getDir("babies_images", MODE_PRIVATE));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    TastyToast.makeText(this, "" + error, TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }
        }
    }


    private void saveNewBaby()
    {
        DatabaseReference babyRef =
                FirebaseDatabase
                        .getInstance()
                        .getReference("babiesDb")
                        .child(deviceIdValue);
                ;

        babyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    Baby baby = new Baby();
                    baby.setName(babyNameValue);
                    baby.setBirthday(babyBirthdayValue);
                    baby.setImage(""+imageUri);
                    baby.setCreated_on(timestamp);
                    baby.setLast_updated_on(timestamp);
                    baby.setTemperature(""+0);
                    baby.setWeight(""+0);
                    babyRef.setValue(baby);
                } else {
                    TastyToast.makeText(AddBabyActivity.this, "Wrong Device Id !", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });


    }


}