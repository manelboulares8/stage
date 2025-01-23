package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class valide extends AppCompatActivity {
    String html;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valide); // Charger le layout
        TextView textViewQRCodeResult = findViewById(R.id.info);

        // Récupérer le texte passé via l'Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("DECRYPTED_TEXT")) {
            String decryptedText = intent.getStringExtra("DECRYPTED_TEXT");
            textViewQRCodeResult.setText(extractTextFromHtml(decryptedText));
        }
        ImageView retourner = findViewById(R.id.retourner);
        retourner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de scan lorsque l'image est cliquée
                Intent intent = new Intent(valide.this, Scanner.class);
                startActivity(intent);
            }
        });
        ImageView userImage = findViewById(R.id.user);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de login lorsque l'image est cliquée
                Intent intent = new Intent(valide.this, login.class);
                startActivity(intent);
            }
        });
    }

        public String extractTextFromHtml(String html) {
            // Utiliser Jsoup pour parser le HTML
            Document document = Jsoup.parse(html);
            StringBuilder cleanText = new StringBuilder();

            // Sélectionner tous les éléments <p> et extraire leur texte
            Elements paragraphs = document.select("p");
            for (Element paragraph : paragraphs) {
                cleanText.append(paragraph.text()).append("\n");
            }

            return  cleanText.toString().trim(); // Retourner le texte nettoyé
        }

    }
