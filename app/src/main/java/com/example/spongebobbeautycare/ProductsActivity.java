package com.example.spongebobbeautycare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.spongebobbeautycare.adapters.ProductsAdapter;
import com.example.spongebobbeautycare.data.VARS;
import com.example.spongebobbeautycare.data.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ProductsActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private ImageView addIcon;
private Context context;
private Product product;
    private ArrayList<Product> productsArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
context = this;
addIcon = (ImageView) findViewById(R.id.addButton);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view_product);
        //

addIcon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     if (VARS.ISADMIN){
        Intent intent = new Intent(context,CreateActivity.class);
        intent.putExtra(VARS.EDITSTATUS,false);
        intent.putExtra(VARS.CATEGORY,getIntent().getStringExtra(VARS.CATEGORY));
        startActivity(intent);}else{
         Toasty.error(context,"Anda harus login sebagai Admin untuk menggunakan fitur ini.",Toasty.LENGTH_LONG).show();
        }
    }
});

    }

    @Override
    protected void onResume() {
        super.onResume();
    addData();
    }

private     ProductsAdapter adapter;
    private void updateUI(){
        Log.d("LOOPSIZE",String.valueOf(productsArrayList.size()));

        for(int i =0;i<productsArrayList.size()-1;i++){
            Log.d("LOOPNAMA",productsArrayList.get(i).getName());
        }
        adapter = new ProductsAdapter(productsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductsActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
    private void addData(){

        productsArrayList = new ArrayList<>();
        db.collection("products").whereEqualTo("category",getIntent().getStringExtra(VARS.CATEGORY))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        product = new Product();
                        product.setName(document.getString("name"));
                        product.setId(document.getId().toString());
                        product.setImage(document.getString("image"));
                        productsArrayList.add(product);
                        /*
                        image.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                product.setImage(task.getResult().toString());
                                productsArrayList.add(product);
                                if (loop == hasil){
                                    Log.d("LOOP","kesino");
                                    updateUI();

                                }else{
                                    Log.d("LOOP",String.valueOf(loop));
                                    loop++;
                                }
                            }
                            }

                        });*/
                     }
updateUI();

                }else {
                    Log.d("ERROR_READ_DATA","error");
                }
            }
        });
    }

}
