package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class invalide extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invalide); // Charger le layout

    ImageView retourner = findViewById(R.id.retourner);
  retourner.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Lancer l'activité de scan lorsque l'image est cliquée
            Intent intent = new Intent(invalide.this, Scanner.class);
            startActivity(intent);
        }
    });
        ImageView userImage = findViewById(R.id.user);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de login lorsque l'image est cliquée
                Intent intent = new Intent(invalide.this, login.class);
                startActivity(intent);
            }
        });

}}