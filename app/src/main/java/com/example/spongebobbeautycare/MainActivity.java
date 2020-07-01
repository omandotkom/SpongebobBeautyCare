package com.example.spongebobbeautycare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;

import com.example.spongebobbeautycare.data.VARS;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
private CardView tonerCard,faciaWash,mosturizer,toner,sunscreen,serum,essence,mask;
private Button btnAbout,btnLogout;
private Context context;
private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        this.context = this;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        btnAbout = (Button) findViewById(R.id.aboutButton);
        btnLogout = (Button) findViewById(R.id.logoutButton);
        tonerCard = (CardView) findViewById(R.id.cardviewToner);
        mosturizer = (CardView) findViewById(R.id.cardViewMoisturizer);
        sunscreen = (CardView) findViewById(R.id.cardViewSunScreen);
        serum = (CardView) findViewById(R.id.cardViewSerum);
        essence = (CardView) findViewById(R.id.cardViewEssence);
        mask = (CardView) findViewById(R.id.cardViewMask);
        faciaWash = (CardView) findViewById(R.id.cardViewFacialWash);
        faciaWash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("facialwash");
            }
        });
        tonerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
toProduct("toner");
            }
        });
        mosturizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("moisturizer");
            }
        });
        sunscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("sunscreen");
            }
        });
        serum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("serum");
            }
        });
        essence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("essence");
            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toProduct("mask");
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAbout();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }
    private void toProduct(String product){
        Intent intent = new Intent(context, ProductsActivity.class);

        intent.putExtra(VARS.CATEGORY, product);

        startActivity(intent);

    }
private void toAbout(){
        startActivity(new Intent(this,AboutActivity.class));
}
private void logout(){
        final String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            Toasty.warning(context,"Sampai jumpa kembali, " + name, Toasty.LENGTH_SHORT).show();
            Intent intent = new Intent(context,Splash.class);
            startActivity(intent);
            finish();
        }
    });
}
}
