package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;

import javax.crypto.Cipher;

public class Scanner extends AppCompatActivity {

    private static final String SALT = "##-s!m@c19:87+##";
    private static final String IV = "0123456789123456";
    private static final String SECRET_KEY = SALT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner); // Charger le layout

        // Références aux vues
        ImageView logoImage = findViewById(R.id.logo_image);
        ImageView logonom = findViewById(R.id.logonom);
        ImageView userImage = findViewById(R.id.user);

        Button scanButton = findViewById(R.id.scan_button);
        TextView aboutus =findViewById(R.id.aboutus);

        scanButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                startQRScanner();
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de login lorsque l'image est cliquée
                Intent intent = new Intent(Scanner.this, login.class);
                startActivity(intent);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de login lorsque l'image est cliquée
                Intent intent = new Intent(Scanner.this, AboutUs.class);
                startActivity(intent);
            }
        });

    }
    /**
     * Starts the QR scanner using ZXing IntentIntegrator.
     */
    private void startQRScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(com.example.myapplication.Scanner.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan a QR Code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    // aafficher contenu du code QR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gérer le résultat du scan du code QR
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Le code QR a été scanné avec succès, on obtient sa valeur
                String scannedValue = result.getContents();

                // Afficher le résultat dans une TextView ou autre
                TextView textView = findViewById(R.id.textViewQRCodeResult); // Assurez-vous que cette TextView est présente dans votre layout XML
                textView.setText("Scanned QR Code Data: " + scannedValue); // Affiche le texte du QR code

                // Ou vous pouvez également afficher un Toast
                Toast.makeText(this, "Scanned Data: " + scannedValue, Toast.LENGTH_SHORT).show();
            } else {
                // Aucun code QR n'a été scanné
                Toast.makeText(this, "Scan failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }*/



    // Afficher texte et key et  hsh sans decodage !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gérer le résultat du scan du code QR
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Le code QR a été scanné avec succès, on obtient sa valeur
                String scannedValue = result.getContents();

                try {
                    // Supposons que le texte est dans un format similaire à :


                    String text = EncryptionBase64Service.extractData(scannedValue, "<TXT>", "</TXT>");
                    String hash = EncryptionBase64Service.extractData(scannedValue, "<HSH>", "</HSH>");
                    String key = EncryptionBase64Service.extractData(scannedValue, "<KEY>", "</KEY>");


                    String DecryptedText=EncryptionBase64Service.decrypt(text,IV,SECRET_KEY);
                    String DecryptedHash=EncryptionBase64Service.decrypt(hash,IV,SECRET_KEY);
                    String DecryptedKey=EncryptionBase64Service.decrypt(key,IV,SECRET_KEY);

                    if(RSAUtil.verify(DecryptedText,DecryptedHash,DecryptedKey)){
                        Intent intent = new Intent(Scanner.this, valide.class);
                        intent.putExtra("DECRYPTED_TEXT", DecryptedText); // Passer le texte déchiffré
                        startActivity(intent);
                        DBHelper dbHelper = new DBHelper(this);
                        String ipAddress = getLocalIpAddress();

                        // Vérifier la validité du QR code (exemple de validation)
                        boolean isValid = true;

                        // Obtenir la date du scan
                        String scanDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
                        dbHelper.insertScanRecord(ipAddress, scanDate, isValid, DecryptedText);


                    }
                    else{
                        Intent intent = new Intent(Scanner.this, invalide.class);
                        startActivity(intent);
                        DBHelper dbHelper = new DBHelper(this);
                        String ipAddress = getLocalIpAddress();
                        boolean isValid = false;
                        // Vérifier la validité du QR code (exemple de validation)

                        // Obtenir la date du scan
                        String scanDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
                        dbHelper.insertScanRecord(ipAddress, scanDate, isValid, DecryptedText);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error processing QR code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Scan failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
/*
    // Méthode pour extraire le contenu entre deux balises
    private String extractData(String input, String startTag, String endTag) {
        int startIndex = input.indexOf(startTag);
        int endIndex = input.indexOf(endTag);
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return input.substring(startIndex + startTag.length(), endIndex);
        }
        return ""; // Retourne une chaîne vide si la balise n'est pas trouvée
    }*/



    // Afficher texte et key et  hsh avec  decodage !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedValue = result.getContents();

                // Décoder les parties TXT, HSH, et KEY
                String decodedResult = QrCodeDecoder.decodeQRCodeParts(scannedValue);

                // Afficher les résultats dans une TextView
                TextView textView = findViewById(R.id.textViewQRCodeResult);
                textView.setText(decodedResult);

                // Récupérer l'adresse IP de l'appareil
                String ipAddress = getLocalIpAddress();

                // Vérifier la validité du QR code (exemple de validation)
                boolean isValid = (scannedValue != null && !scannedValue.isEmpty());

                // Obtenir la date du scan
                String scanDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

                // Enregistrer dans la base de données
                DBHelper dbHelper = new DBHelper(this);
                dbHelper.insertScanRecord(ipAddress, scanDate, isValid, scannedValue);
            } else {
                Toast.makeText(this, "Scan failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    public String getLocalIpAddress() {
        try {
            for (NetworkInterface netInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetAddress : Collections.list(netInterface.getInetAddresses())) {
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si aucune adresse IP valide n'est trouvée
    }



}





