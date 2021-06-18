package com.example.fairrepack;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.PKCS8EncodedKeySpec;
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
     *
     * @throws Exception Only need to handle IOException if the file doesn't exist
     *                   or NoSuchAlgorithmException if file is corrupted
     */
    public Wallet() throws Exception {
        byte[] file = Files.readAllBytes(Paths.get("private.key"));
        file = Base64.getDecoder().decode(file);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(file);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(keySpec);

        // Whole process to get public key from the private one
        RSAPrivateCrtKey privk = (RSAPrivateCrtKey) this.privateKey;
        RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
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
    public Wallet(String file_path) throws Exception {
        byte[] file = Files.readAllBytes(Paths.get(file_path));
        file = Base64.getDecoder().decode(file);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(file);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(keySpec);

        // Whole process to get public key from the private one
        RSAPrivateCrtKey privk = (RSAPrivateCrtKey) this.privateKey;
        RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(publicKeySpec);

        MessageDigest digest = MessageDigest.getInstance("SHA256");
        byte[] encodedHash = digest.digest(this.getPublicKey().getBytes(StandardCharsets.UTF_8));
        this.address = Wallet.toHexString(encodedHash);

        // Export private key to a file
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        String tmp = Base64.getEncoder().encodeToString(privateKeySpec.getEncoded());
        FileWriter fos = new FileWriter("private.key");
        fos.write(tmp);
        fos.close();
    }

    /**
     * Constructor to instantiate a wallet by generating a new one
     *
     * @param b Precise to generate a new key pair
     * @throws Exception Same as the precious constructor
     */
    public Wallet(boolean b) throws Exception {
        // Generate new pair of RSA keys
        KeyPair key_pair = buildKeyPair();
        this.publicKey = key_pair.getPublic();
        this.privateKey = key_pair.getPrivate();

        MessageDigest digest = MessageDigest.getInstance("SHA256");
        byte[] encodedHash = digest.digest(this.getPublicKey().getBytes(StandardCharsets.UTF_8));
        this.address = calculateAddress(getPublicKey());

        // Export private key to a file
        /*PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        String tmp = Base64.getEncoder().encodeToString(keySpec.getEncoded());
        FileWriter fos = new FileWriter("private.key");
        fos.write(tmp);
        fos.close();*/
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

    /**
     * Takes a string and return a signature by the private key, of it
     *
     * @param message String to sign
     * @return Return signature in base64 encoding
     * @throws Exception Can't happen
     */
    public String sign(String message) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(this.privateKey);
        privateSignature.update(message.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Getter for public key
     *
     * @return Public key in base64 encoding
     */
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
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
     * Getter for wallet address
     *
     * @return
     */
    public String getAddress() {
        return address;
    }
}