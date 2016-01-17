package com.handgold.pjdc.util;


import com.handgold.pjdc.base.Constant;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {

    private String encryptKey;
    static byte[] keybt = new byte[8];

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public DES(String str) {
        this.encryptKey = str;

        byte[] arrBTmp = str.getBytes();
        for (int i = 0; (i < arrBTmp.length) && (i < keybt.length); i++)
            keybt[i] = arrBTmp[i];
    }

    public DES() {
        this(Constant.TYY_APP_SECRET);
    }

    public String encryptDES(String encryptString) {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(keybt, "DES");
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptString;
    }

    public String decryptDES(String decryptString) {
        try {
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);

            SecretKeySpec key = new SecretKeySpec(keybt, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);

            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptString;
    }
}
