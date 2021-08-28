package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;


import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;


public class MappingActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    String urlTrouser, urlShirt, urlShirt2,urlShirt3,urlShirt4,urlShirt5;


    ArrayList<String> ImgUrl= new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager Manager;
    Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);




        Intent intent = getIntent();
        String data = intent.getStringExtra("message");



        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference(data);

        this.recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        Manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(Manager);
        adapter = new Adapter(ImgUrl, this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                urlShirt=snapshot.child("product_image").child("url").getValue(String.class);
                urlShirt2=snapshot.child("product_image").child("url2").getValue(String.class);

                urlShirt3=snapshot.child("product_image").child("url3").getValue(String.class);
                urlShirt4=snapshot.child("product_image").child("url4").getValue(String.class);
                urlShirt5=snapshot.child("product_image").child("url5").getValue(String.class);

                urlTrouser=snapshot.child("product_name").getValue(String.class);


               // ImgUrl.add(urlTrouser);
                ImgUrl.add(urlShirt);
                ImgUrl.add(urlShirt2);
                ImgUrl.add(urlShirt3);
                ImgUrl.add(urlShirt4);
                ImgUrl.add(urlShirt5);

                recyclerView.setAdapter(adapter);

                // Image link from internet
                new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view))
                        .execute(urlTrouser);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MappingActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });




    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}