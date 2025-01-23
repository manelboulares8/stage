package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ImageView retourner = findViewById(R.id.retourner);

        // Initialisation des vues
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);

        retourner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Scanner.class);
                startActivity(intent);
            }
        });

        dbHelper = new DBHelper(this);
       dbHelper.addUser("test@gmail.com","1234");

        // Événement de clic sur le bouton de connexion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les données des champs
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Vérifier les champs non vides
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier si l'utilisateur existe dans la base de données
                    if (dbHelper.checkUser(email, password)) {
                        // Si l'utilisateur est trouvé, aller vers l'écran principal
                        Intent intent = new Intent(login.this,historique.class);
                        startActivity(intent);
                        finish();  // Pour ne pas revenir à cette activité avec le bouton retour
                    } else {
                        // Afficher un pop-up d'erreur si l'utilisateur n'existe pas
                        showErrorDialog();
                    }
                }
            }
        });
    }

    // Méthode pour afficher un pop-up avec un message d'erreur
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur de connexion")
                .setMessage("Identifiants incorrects. Veuillez vérifier votre e-mail et votre mot de passe.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false) // Empêcher de fermer le pop-up en dehors
                .setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Réinitialiser les champs pour une nouvelle tentative
                        emailInput.setText("");
                        passwordInput.setText("");
                    }
                });

        // Afficher le pop-up
        builder.create().show();
    }
}
