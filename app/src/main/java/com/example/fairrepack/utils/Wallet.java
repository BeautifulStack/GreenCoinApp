package com.example.fairrepack.utils;

import android.content.Context;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class used to interact with RSA keys and signature algorithm
 */
public class Wallet {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final String address;

    /**
     * Constructor to instantiate a wallet with an existing private key file
     * <p>
     * NO UTILITY FOR NOW
     *
     * @throws Exception Only need to handle IOException if the file doesn't exist
     *                   or NoSuchAlgorithmException if file is corrupted
     */
    private Wallet(Context context) throws Exception {
        byte[] file = Files.readAllBytes(Paths.get("private.key"));

        file = Base64.getDecoder().decode(file);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(file);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(keySpec);

        // Whole process to get public key from the private one
        RSAPrivateCrtKey privk = (RSAPrivateCrtKey) this.privateKey;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(publicKeySpec);

        MessageDigest digest = MessageDigest.getInstance("SHA256");
        byte[] encodedHash = digest.digest(this.getPublicKey().getBytes(StandardCharsets.UTF_8));
        this.address = Wallet.toHexString(encodedHash);
    }

    /**
     * Constructor to instantiate a wallet with an existing private key file imported by the user
     *
     * @throws Exception Only need to handle IOException if the file doesn't exist
     *                   or NoSuchAlgorithmException if file is corrupted
     */
    private Wallet(String path, Context context) throws Exception {
        // PRIVATE
        byte[] file = Files.readAllBytes(Paths.get(path + "/private.key"));
        file = Base64.getDecoder().decode(file);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(file);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(keySpec);

        // PUBLIC
        /*byte[] file2 = Files.readAllBytes(Paths.get(path+"/public.key"));
        file2 = Base64.getDecoder().decode(file2);
        X509EncodedKeySpec keySpec2 = new X509EncodedKeySpec(file2);
        KeyFactory kf2 = KeyFactory.getInstance("RSA");
        this.publicKey = kf2.generatePublic(keySpec2);*/

        // Whole process to get public key from the private one
        //RSAPrivateCrtKey privk = (RSAPrivateCrtKey) this.privateKey; //This line doesn't work in Android and it f**king piss me off !!
        RSAPrivateKeySpec privateKeySpec = kf.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKeySpec.getModulus(), BigInteger.valueOf(65537)); // Un peu doozy doozy
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(publicKeySpec);

        // ADDRESS
        this.address = calculateAddress(getPublicKey());
    }

    /**
     * Constructor to instantiate a wallet by generating a new one
     *
     * @param b Precise to generate a new key pair
     * @throws Exception Same as the precious constructor
     */
    private Wallet(boolean b, Context context) throws Exception {
        // Generate new pair of RSA keys
        KeyPair key_pair = buildKeyPair();
        this.publicKey = key_pair.getPublic();
        this.privateKey = key_pair.getPrivate();

        this.address = calculateAddress(getPublicKey());

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        String privkey_export = Base64.getEncoder().encodeToString(privateKeySpec.getEncoded());

        //X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(this.publicKey.getEncoded());
        //String pubkey_export = Base64.getEncoder().encodeToString(publicKeySpec.getEncoded());

        File file1 = new File(context.getFilesDir(), "private.key");
        try (FileOutputStream stream = new FileOutputStream(file1)) {
            stream.write(privkey_export.getBytes(StandardCharsets.UTF_8));
        }

        /*File file2 = new File(context.getFilesDir(), "public.key");
        try (FileOutputStream stream = new FileOutputStream(file2)) {
            stream.write(pubkey_export.getBytes(StandardCharsets.UTF_8));
        }*/

    }

    public static Wallet get_wallet(String path, Context context) {
        if (path == null) {
            try {
                return new Wallet(true, context);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return new Wallet(path, context);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Method use to generate the key pair, used by the constructor
     *
     * @return Return the key pair
     * @throws NoSuchAlgorithmException Can't happen
     */
    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 1024;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    private static String calculateAddress(String public_key) throws NoSuchAlgorithmException {
        byte[] ph = new byte[20];

        byte[] sha256 = MessageDigest.getInstance("SHA256").digest(public_key.getBytes(UTF_8));
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256, 0, sha256.length);
        digest.doFinal(ph, 0);
        return toHexString(ph);
    }

    private static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    /**
     * Takes a string and return a signature by the private key, of it
     *
     * @param message String to sign
     * @return Return signature in base64 encoding
     */
    public String sign(String message){
        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(this.privateKey);
            privateSignature.update(message.getBytes(UTF_8));

            byte[] signature = privateSignature.sign();

            return Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Getter for private key
     *
     * @return Public key in base64 encoding
     */
    public String getPrivateKey() {
        // Export private key to a file
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        return Base64.getEncoder().encodeToString(keySpec.getEncoded());
    }

    /**
     * Getter for public key
     *
     * @return Public key in base64 encoding
     */
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }

    /**
     * Getter for wallet address
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    public static boolean delete_wallet(Context context) {
        File file = new File(context.getFilesDir(), "private.key");
        return file.delete();
    }
}