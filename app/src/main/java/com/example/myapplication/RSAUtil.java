package com.example.myapplication;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {
    private static final String ALGORITHM = "RSA";


    public static String decrypt(String valueToDecrypt, PrivateKey privateKey) throws Exception {
        // Decode the Base64-encoded string
        byte[] valueToDecryptBArr = Base64.decode(valueToDecrypt, Base64.DEFAULT);

        // Initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(valueToDecryptBArr);

        // Convert the decrypted bytes to a string
        return new String(decryptedBytes);
    }


    public static boolean verify(String messageText, String signature, String publicKey) {
        try {
            // Decode the Base64-encoded public key
            byte[] publicKeyByteArr = Base64.decode(publicKey, Base64.DEFAULT);

            // Reconstruct the PublicKey object
            PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKeyByteArr));

            // Initialize the Signature object for verification
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(key);

            // Update the signature with the message text
            sig.update(messageText.getBytes("UTF-8"));

            // Decode the Base64-encoded signature and verify it
            byte[] signatureBytes = Base64.decode(signature, Base64.DEFAULT);
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return false;
        }
    }
}
