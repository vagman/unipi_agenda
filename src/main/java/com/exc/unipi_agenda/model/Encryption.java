package com.exc.unipi_agenda.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {
    // Variable Declaration
    private String password_unhashed;
    private String password_hashed;
    private String salt;

    // Getters
    public String getPassword_hashed(){return this.password_hashed;}
    public String getSalt(){return this.salt;}

    // Tools
    byte[] saltArray;
    String toHash;
    String userInput_hashed;


    /**
     * Encrypt a password. With the object you can read hash_pass and salt
     * @param password_unhashed Unhashed password
     */
    public void encrypt(String password_unhashed){
        this.password_unhashed = password_unhashed;

        // Create salt
        SecureRandom random = new SecureRandom();
        saltArray = new byte[20];
        random.nextBytes(saltArray);
        this.salt = saltArray.toString();

        // Hashing
        toHash = salt + password_unhashed + salt;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5"); //SHA512
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            this.password_hashed = "";
        }
        if(messageDigest != null){
            messageDigest.update(toHash.getBytes(), 0, toHash.length());
            this.password_hashed = new BigInteger(1, messageDigest.digest()).toString(16);
            if (this.password_hashed.length() < 32) {
                this.password_hashed = "0" + password_hashed;
            }
        }
    }

    /**
     * Returns boolean if the password is correct
     * @param password_hashed
     * @param salt Str
     * @param userInput String
     * @return boolean
     */
    public boolean passwordMach(String password_hashed, String salt, String userInput){
        this.password_hashed = password_hashed;
        this.password_unhashed = "";
        this.salt = salt;
        // Hashing userInput with the saved hash
        toHash = salt + userInput + salt;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5"); //SHA512
        } catch (NoSuchAlgorithmException ex) {
            return false;
        }
        messageDigest.update(toHash.getBytes(), 0, toHash.length());
        userInput_hashed = new BigInteger(1, messageDigest.digest()).toString(16);
        if (userInput_hashed.length() < 32) {
            userInput_hashed = "0" + userInput_hashed;
        }

        return userInput_hashed.equals(this.password_hashed);
    }
}
