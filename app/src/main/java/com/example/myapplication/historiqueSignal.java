package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class historiqueSignal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_signal);

        // Charger les historiques reportés
        loadReportedHistorique();
        ImageView retourner =findViewById(R.id.retourner);
        retourner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(historiqueSignal.this, historique.class);
                startActivity(intent);
            }
        });

        // Logout button handling
        ImageView logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmation();
            }
        });
    }
    private void showLogoutConfirmation() {
        // Afficher un message de confirmation pour le logout
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    // Action de déconnexion
                    Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité
                    Intent intent = new Intent(historiqueSignal.this, Scanner.class);
                    startActivity(intent);
                })
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void loadReportedHistorique() {
        // Conteneur pour afficher les lignes dynamiques
        LinearLayout container = findViewById(R.id.holder);
        container.removeAllViews(); // Nettoyer les vues existantes

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getReportedHistorique(); // Récupérer les historiques reportés

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Récupérer les données depuis le curseur
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                @SuppressLint("Range") String ipAddress = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IP_ADDRESS));
                @SuppressLint("Range") String datee = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SCAN_DATE));
                @SuppressLint("Range") int validitee = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_IS_VALID));
                @SuppressLint("Range") String encryptedData = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ENCRYPTED_DATA));
                @SuppressLint("Range") int reporting = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_REPORT));

                // Créer une nouvelle vue pour chaque ligne
                View rowView = LayoutInflater.from(this).inflate(R.layout.holder, container, false);

                // Référencer les éléments de la vue
                TextView validiteTextView = rowView.findViewById(R.id.validite);
                TextView dateTextView = rowView.findViewById(R.id.date);

                // Définir les valeurs
                validiteTextView.setText(validitee == 1 ? "Valide" : "Invalide");
                dateTextView.setText(datee);
                rowView.setOnClickListener(v -> showCustomPopup(id, ipAddress, encryptedData, validitee));

                // Ajouter la vue au conteneur
                container.addView(rowView);
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Aucun historique reporté trouvé.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showCustomPopup(int id, String ipAddress, String encryptedData, int validitee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_popup_reported, null);
        builder.setView(dialogView);

        // Référencer les vues du layout
        TextView ipAddressTextView = dialogView.findViewById(R.id.ipAddressTextView);
        TextView encryptedDataTextView = dialogView.findViewById(R.id.encryptedDataTextView);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button reportButton = dialogView.findViewById(R.id.reportButton);

        // Afficher l'adresse IP
        ipAddressTextView.setText("Adresse IP de l'appareil : " + ipAddress);

        // Vérifier si les données sont valides
        if (validitee == 1) {
            encryptedDataTextView.setText("Données Cryptées : " + encryptedData);
        } else {
            encryptedDataTextView.setText("Données Cryptées : Non valides");
        }

        // Créer et afficher la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();

        // Gérer le clic sur le bouton Supprimer
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cet élément ?")
                    .setPositiveButton("Oui", (dialogInterface, which) -> {
                        // Supprimer l'élément de la base de données
                        DBHelper dbHelper = new DBHelper(this);
                        dbHelper.deleteHistorique(id);

                        // Recharger l'historique après la suppression
                        loadReportedHistorique();

                        // Fermer la boîte de dialogue
                        dialog.dismiss();

                        Toast.makeText(this, "Supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Non", (dialogInterface, which) -> dialogInterface.dismiss())
                    .show();
        });


    }

}