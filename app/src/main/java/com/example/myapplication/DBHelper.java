package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aigleDB";
    private static final int DATABASE_VERSION = 5; // Incrémentation de la version de la base de données
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_HISTORIQUE = "historique";
    public static final String COLUMN_IP_ADDRESS = "ip_address";
    public static final String COLUMN_SCAN_DATE = "scan_date";
    public static final String COLUMN_IS_VALID = "is_valid";
    public static final String COLUMN_ENCRYPTED_DATA = "encrypted_data";
    public static final String COLUMN_REPORT = "report";

    // SQL statement to create users table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT);";

    // SQL statement to create historique table
    // SQL statement to create historique table
    private static final String CREATE_TABLE_HISTORIQUE = "CREATE TABLE " + TABLE_HISTORIQUE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_IP_ADDRESS + " TEXT, "
            + COLUMN_SCAN_DATE + " TEXT, "
            + COLUMN_IS_VALID + " INTEGER, "
            + COLUMN_ENCRYPTED_DATA + " TEXT, "
            + COLUMN_REPORT + " INTEGER DEFAULT 0);"; // Ajout de la colonne `report`

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création des tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_HISTORIQUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Ajouter la colonne `report` à la table `historique`
            db.execSQL("ALTER TABLE " + TABLE_HISTORIQUE + " ADD COLUMN " + COLUMN_REPORT + " INTEGER DEFAULT 0;");
        }
    }

    public void addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        // Insertion d'un utilisateur
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    // Méthode pour insérer un enregistrement dans la table historique
    public void insertScanRecord(String ipAddress, String scanDate, boolean isValid, String encryptedData) {

        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("DELETE FROM " + TABLE_HISTORIQUE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_IP_ADDRESS, ipAddress);
        values.put(COLUMN_SCAN_DATE, scanDate);
        values.put(COLUMN_IS_VALID, isValid ? 1 : 0);
        values.put(COLUMN_ENCRYPTED_DATA, encryptedData);
        values.put(COLUMN_REPORT, 0);
        // Insérer l'enregistrement dans la table historique
        db.insert(TABLE_HISTORIQUE, null, values);
        db.close();
    }

    public Cursor getAllHistorique() {

        SQLiteDatabase db = this.getReadableDatabase();
        // Select all rows from the historique table
        return db.query(TABLE_HISTORIQUE, null, null, null, null, null, null);
    }

    public void deleteHistorique(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORIQUE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void reportHistorique(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPORT, 1); // Mettre le champ `report` à true (1)

        db.update(TABLE_HISTORIQUE, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getReportedHistorique() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HISTORIQUE, null, COLUMN_REPORT + " = ?", new String[]{"1"}, null, null, null);
    }
}