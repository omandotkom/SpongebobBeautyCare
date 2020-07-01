package com.example.spongebobbeautycare;

import android.graphics.Typeface;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.spongebobbeautycare.data.VARS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetilActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ImageView productImage;
    private TextView productName,detilproductHowToUse,productDescription,detilproductInggredients,detilproductSuitable;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        productImage = (ImageView) findViewById(R.id.detilproductimage);
        productName = (TextView) findViewById(R.id.detilproductname);
        detilproductInggredients = (TextView) findViewById(R.id.detilproductInggredients);
        productDescription = (TextView) findViewById(R.id.detilproductDesctiption);
        detilproductHowToUse = (TextView) findViewById(R.id.detilproductHowToUse);
        detilproductSuitable = (TextView) findViewById(R.id.detilproductSuitable);
        productName.setTypeface(Typeface.createFromAsset(getAssets(), VARS.MPLUS_FONT_EXTRABOLD));
        productDescription.setTypeface(Typeface.createFromAsset(getAssets(),VARS.MPLUS_FONT_REGULA));
        detilproductInggredients.setTypeface(Typeface.createFromAsset(getAssets(),VARS.MPLUS_FONT_REGULA));
        detilproductSuitable.setTypeface(Typeface.createFromAsset(getAssets(),VARS.MPLUS_FONT_REGULA));
        detilproductHowToUse.setTypeface(Typeface.createFromAsset(getAssets(),VARS.MPLUS_FONT_REGULA));
        firebaseFirestore.collection("products").document(getIntent().getStringExtra(VARS.PRODUCTID)).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d("Hasil",task.getResult().toString());
                            Glide.with(getApplicationContext()).load(getIntent().getStringExtra(VARS.PRODUCTIMAGEURL))
                                    .centerCrop()
                                    .placeholder(R.drawable.makeup).into(productImage);
                            productName.setText(task.getResult().getString("name"));
                            productDescription.setText(task.getResult().getString("description"));
                            detilproductInggredients.setText(task.getResult().getString("inggredients"));
                            detilproductSuitable.setText(task.getResult().getString("suitablefor"));
                            detilproductHowToUse.setText(task.getResult().getString("howtouse"));
                        }
                    }
                });
    }
}
