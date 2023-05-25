package ensias.myteam.babytakingcare;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ensias.myteam.babytakingcare.Models.Parent;

public class  EditUserActivity extends AppCompatActivity {

    private TextView changeImage  , badgeLetter_edit;
    private ImageView userBadge , back_btn ;
    private EditText firstName , secondName , birthday , phoneNumber , location  ;
    private String firstNameValue , secondNameValue , birthdayValue , phoneNumberValue , locationValue  ;
    private CircularImageView userImage;
    //
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private Uri imageUri;
    //
    private FloatingActionButton submitUpdate ;
    //
    private ProgressDialog progressDialog ;
    //
    private FirebaseAuth auth ;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        initialisation();
    }

    private void initialisation()
    {
        cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //
        changeImage = findViewById(R.id.changeImage_edit);
        userImage  = findViewById(R.id.userImage_edit);
        firstName = findViewById(R.id.firstName_edit);
        secondName = findViewById(R.id.secondName_edit);
        birthday = findViewById(R.id.birthday_edit);
        phoneNumber = findViewById(R.id.phoneNumber_edit);
        location = findViewById(R.id.location_edit);
        submitUpdate = findViewById(R.id.saveUpdate);
        //
        userBadge = findViewById(R.id.userBadge_edit);
        badgeLetter_edit = findViewById(R.id.badgeLetter_edit);
        //
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //
        back_btn = findViewById(R.id.back_btn);
        //
        getInfoUserAuthenticated() ;

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoDialog();
            }
        });

        submitUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInformation();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getInfoUserAuthenticated()
    {
        String id  = user.getUid();
        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference("babiesDb");
        DatabaseReference contactRef = contactsRef.child(id);
        contactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the contact data as a Contact object
                Parent parent = dataSnapshot.getValue(Parent.class);
                firstName.setText(parent.getFirstName());
                secondName.setText(parent.getSecondName());
                birthday.setText(parent.getBirthday());
                phoneNumber.setText(parent.getPhoneNumber());
                location.setText(parent.getLocation());
                if (parent.getImage() == null || parent.getImage().equals("") || Objects.isNull(parent.getImage())) {
                    userImage.setImageDrawable(getResources().getDrawable(R.drawable.img_avatar));
                } else {
                    badgeLetter_edit.setVisibility(View.INVISIBLE);
                    userBadge.setVisibility(View.INVISIBLE);
                    changeImage.setText("Change Photo");
                    userImage.setImageURI(Uri.parse(parent.getImage()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void updateUserInformation()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("update informations ");
        progressDialog.setMessage("Please wait the process ...");
        progressDialog.show();

        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("babiesDb").child(userId);

        firstNameValue = firstName.getText().toString();
        secondNameValue = secondName.getText().toString();
        phoneNumberValue = phoneNumber.getText().toString() ;
        locationValue = location.getText().toString() ;
        birthdayValue = birthday.getText().toString();

        // Create a map with the attributes to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstNameValue);
        updates.put("secondName", secondNameValue);
        updates.put("phoneNumber" , phoneNumberValue);
        updates.put("location", locationValue);
        updates.put("birthday" , birthdayValue);

        if(imageUri != null && ! "".equals(imageUri.toString()+""))
            updates.put("image" , ""+imageUri);

        // Update the user in the database
        userRef.updateChildren(updates);
        progressDialog.dismiss();
        TastyToast.makeText(this, "successfully UPDATED", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    private void addPhotoDialog() {
        String[] options = {"Take Photo", "Choose Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (ContextCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                addImageFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (ContextCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

                    if (resultUri != null) {
                        imageUri = resultUri;
                        userImage.setImageURI(resultUri);
                        userBadge.setVisibility(View.INVISIBLE);
                        badgeLetter_edit.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        try {
                            saveToDirectory("" + imageUri.getPath(), "" + getDir("contacts_images", MODE_PRIVATE));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    TastyToast.makeText(this, "" + error, TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            }
        }
    }
}