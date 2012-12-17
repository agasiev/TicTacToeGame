package com.game.xo;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class AESCoder {
    static final String AES_KEY = "TicTacToeAESKey+";

    public static String decryptAES(String source) {
        try {
            System.out.println(source);
            Cipher aes = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            aes.init(Cipher.DECRYPT_MODE, key);
            return new String(aes.doFinal(source.getBytes()));
        }
        catch (Exception e) {
            System.out.println("[Decrypt Error]: " + e.getMessage());
        }
        return "";
    }

    public static String encryptAES(String source) {
        try {
            Cipher aes = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            aes.init(Cipher.ENCRYPT_MODE, key);
            return new String(aes.doFinal(source.getBytes()));
        }
        catch (Exception e) {
            System.out.println("[Encrypt Error]: " + e.getMessage());
        }
        return "";
    }
}
