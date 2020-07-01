package com.example.spongebobbeautycare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.spongebobbeautycare.data.VARS;
import com.example.spongebobbeautycare.data.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.telephony.mbms.FileInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class CreateActivity extends AppCompatActivity {
private ImageView imageCreate;
private FirebaseFirestore firebaseFirestore;
private FirebaseStorage firebaseStorage;
private Context context;
private EditText name,inggrediens,howtouse,description,suitablefor;
private Button buttonSave,buttonDelete;
private Activity activity;
private String category;
private boolean imageChanged = false;
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        imageCreate = (ImageView) findViewById(R.id.createImage);
        name = (EditText) findViewById(R.id.nameInput);
        inggrediens = (EditText) findViewById(R.id.inggredientsInput);
        description = (EditText) findViewById(R.id.descriptionInput);
        howtouse = (EditText) findViewById(R.id.howtouseInput);
        suitablefor = (EditText) findViewById(R.id.suitableforInput);
        buttonSave = (Button) findViewById(R.id.createSave);
        buttonDelete = (Button) findViewById(R.id.createDelete);
        context = this;
        product = new Product();
        activity = this;
        product.setCategory(this.getIntent().getStringExtra(VARS.CATEGORY));
        if (this.getIntent().getBooleanExtra(VARS.EDITSTATUS, false) == true) {
            //jika dalam mode edit
            firebaseFirestore.collection("products").document(this.getIntent().getStringExtra(VARS.PRODUCTID)).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                product.setImage(task.getResult().getString("image"));
                                product.setCategory(task.getResult().getString("category"));
                                name.setText(task.getResult().getString("name"));
                                description.setText(task.getResult().getString("description"));
                                howtouse.setText(task.getResult().getString("howtouse"));
                                suitablefor.setText(task.getResult().getString("suitablefor"));
                                inggrediens.setText(task.getResult().getString("inggredients"));
                                Glide.with(context).load(task.getResult().getString("image"))
                                        .centerCrop()
                                        .placeholder(R.drawable.makeup).into(imageCreate);

                            }
                        }
                    });


        }
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonEnable(false);
                Toasty.info(context,"Mulai mengunggah....",Toasty.LENGTH_SHORT).show();
                save();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
delete();
            }
        });
        imageCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }
    private void setButtonEnable(boolean status){
        buttonDelete.setEnabled(status);
        buttonSave.setEnabled(status);
    }
    private void delete(){
        setButtonEnable(false);
        if (!getIntent().getStringExtra(VARS.PRODUCTID).isEmpty()){
            firebaseFirestore.collection("products").document(getIntent().getStringExtra(VARS.PRODUCTID)).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                    Toasty.success(context,"Berhasil menghapus data ini.",Toasty.LENGTH_LONG).show();
                    setButtonEnable(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setButtonEnable(true);
                    Toasty.error(context,"Gagal menghapus data ini.",Toasty.LENGTH_LONG).show();
                }
            });
        }
    }
    private String filename;
    private Bitmap newimage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK) {
            imageChanged = true;
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            filename = new File(filePath).getName();
            newimage = BitmapFactory.decodeFile(filePath);
            imageCreate.setImageBitmap(newimage);
        }
    }StorageReference uploadReference;
private  Map<String, Object> data;
    private void save(){
data = new HashMap<>();
        data.put("name", name.getText().toString());
        data.put("howtouse",howtouse.getText().toString());
        data.put("description", description.getText().toString());
        data.put("inggredients",inggrediens.getText().toString());
        data.put("suitablefor",suitablefor.getText().toString());
        data.put("category",product.getCategory());
        if (this.imageChanged){
            StorageReference storageReference = firebaseStorage.getReference();
            uploadReference = firebaseStorage.getReference(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newimage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] newFileBytes = baos.toByteArray();

            UploadTask uploadTask = uploadReference.putBytes(newFileBytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toasty.error(context,exception.getMessage(),Toasty.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    uploadReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                           // data.put("image",filename);
                            data.put("image",task.getResult().toString());
                            Toasty.success(context,"Berhasil mengunggah foto.",Toasty.LENGTH_LONG).show();
                            saveToFireStore();
                        }
                    });

                }
            });
        }else{
            data.put("image",product.getImage());
        saveToFireStore();
        }

    }
    private void saveToFireStore(){
        if(getIntent().getBooleanExtra(VARS.EDITSTATUS,false)==true){
            //jika edit
            firebaseFirestore.collection("products").document(getIntent().getStringExtra(VARS.PRODUCTID)).set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(context,"Berhasil menyimpan data", Toasty.LENGTH_LONG)
                                    .show();
                            setButtonEnable(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(context,"Gagal menyimpan data", Toasty.LENGTH_LONG).show();
                }
            });
        }else{
            firebaseFirestore.collection("products").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toasty.success(context,"Berhasil menyimpan data", Toasty.LENGTH_LONG)
                            .show();
setButtonEnable(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(context,"Gagal menyimpan data", Toasty.LENGTH_LONG).show();

                }
            });
        }
    }
    private final int REQUEST_CODE_PICKER = 312;
private void chooseImage(){
    Intent intent = new Intent(this, ImageSelectActivity.class);
    intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
    intent.putExtra(ImageSelectActivity.FLAG_CAMERA, false);//default is true
    intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
    startActivityForResult(intent, REQUEST_CODE_PICKER);
}

}
