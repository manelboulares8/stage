package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;


public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        ImageView retourner = findViewById(R.id.retourner);
        ImageView logo =findViewById(R.id.logo_image);
        TextView text =findViewById(R.id.thanks);
        TextView textt =findViewById(R.id.bienvenue);
        TextView texttt =findViewById(R.id.about_us_title);
       // TextView textttt =findViewById(R.id.about_us_content);



        retourner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de scan lorsque l'image est cliquée
                Intent intent = new Intent(AboutUs.this, Scanner.class);
                startActivity(intent);
            }
        });

    }
}