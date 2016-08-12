package co.timecrypt.api.v2.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * A basic implementation of the AES cipher routine.
 */
public class SimpleAES {

    /**
     * Encrypts the given text using an symmetric encryption algorithm and the given encryption key. If encryption fails,
     * this simple implementation does not attempt again. It could still be encrypted in the database layer.
     *
     * @param content    A message text ready for encryption
     * @param passphrase An encryption key
     * @return The encrypted text
     */
    public static String encrypt(String content, String passphrase) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(passphrase));
            byte[] bytes = content.getBytes("UTF-8");
            byte[] encrypted = cipher.doFinal(bytes);
            return new BASE64Encoder().encode(encrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
            return content;
        }
    }

    /**
     * Decrypts the given text using an symmetric decryption algorithm and the given decryption key. If decryption fails,
     * this simple implementation does not attempt again. It could still have been decrypted in the database layer.
     *
     * @param content    A message text ready for decryption
     * @param passphrase A decryption key
     * @return The decrypted text
     */
    public static String decrypt(String content, String passphrase) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, generateKey(passphrase));
            byte[] bytes = new BASE64Decoder().decodeBuffer(content);
            byte[] decrypted = cipher.doFinal(bytes);
            return new String(decrypted);
        } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IOException |
                NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return content.replaceAll("=", "").replaceAll("/", "0"); // hide that it's base64
        }
    }

    /**
     * Creates a padded passphrase modification - basically... hashes the passphrase and then uses the first 128 bits.
     *
     * @param passphrase Which passphrase to use
     * @return The key spec object, containing the key info
     */
    private static SecretKeySpec generateKey(String passphrase) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] pwdBytes = passphrase.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        pwdBytes = sha.digest(pwdBytes);
        pwdBytes = Arrays.copyOf(pwdBytes, 16); // use only first 128 bits
        return new SecretKeySpec(pwdBytes, "AES");
    }

}
