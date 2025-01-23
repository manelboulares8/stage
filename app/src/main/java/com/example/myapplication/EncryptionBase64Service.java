package com.example.myapplication;


import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionBase64Service {
    private static final String SALT = "##-s!m@c19:87+##";
    private static final String IV = "0123456789123456";
    private static final String SECRET_KEY = SALT;
    private static final EncryptionBase64Service encryptionService = new EncryptionBase64Service();
    public static EncryptionBase64Service getInstance() {
        return encryptionService;
    }

    public String decryptPassword(String password) {
        if (password == null) {
            return null;
        }
        return decrypt(password, IV, SECRET_KEY);
    }

    private static Cipher initCipher(final int mode, final String initialVectorString, final String secretKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        final SecretKeySpec skeySpec = new SecretKeySpec((secretKey).getBytes(), "AES");
        final IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
        final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(mode, skeySpec, initialVector);
        return cipher;
    }

   /* static String decrypt(final String encryptedData, final String initialVector, final String secretKey) {
        String decryptedData = null;
        try {
            final Cipher cipher = initCipher(Cipher.DECRYPT_MODE, initialVector, secretKey);
            final byte[] encryptedByteArray = Base64.decode(encryptedData);
            final byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
            decryptedData = new String(decryptedByteArray, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }*/
   static String decrypt(final String encryptedData, final String initialVector, final String secretKey) {
       String decryptedData = null;
       try {
           // Initialize the cipher for decryption
           final Cipher cipher = initCipher(Cipher.DECRYPT_MODE, initialVector, secretKey);

           // Decode the Base64-encoded encrypted data
           final byte[] encryptedByteArray = Base64.decode(encryptedData, Base64.DEFAULT);

           // Decrypt the data
           final byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);

           // Convert the decrypted byte array to a string
           decryptedData = new String(decryptedByteArray, StandardCharsets.UTF_8);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return decryptedData;
   }


    static String extractData(String input, String startTag, String endTag) {
        int startIndex = input.indexOf(startTag);
        int endIndex = input.indexOf(endTag);
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return input.substring(startIndex + startTag.length(), endIndex);
        }
        return ""; // Retourne une chaîne vide si la balise n'est pas trouvée
    }




}
