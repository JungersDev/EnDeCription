import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*
Шифровка
*/

public class Solution {
    static Cipher cipher;
    private static SecretKeySpec key = new SecretKeySpec("Som12345Som12345".getBytes(), "AES");

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static SecretKeySpec getKey() {
        return key;
    }

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        switch (args[0]) {
            case "-e" :
                encryptFile(args[1], args[2]);
                break;
            case "-d" :
                decryptFile(args[1], args[2]);
                break;
        }

    }

    // * ENCRYPTION
    public static void encryptFile(String from, String to) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // ? Take data
        String resultFrom = null;
        try (BufferedReader buffer = new BufferedReader(new FileReader(from))) {
            StringBuilder sb = new StringBuilder();
            while (buffer.ready()) {
                sb.append(buffer.readLine()).append("\n");
            }

            resultFrom = sb.toString();
        } catch (IOException e) {
            System.out.println("IOException was caught");
        }

        // ? Put data
        // GET CIPHER
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] bytes = cipher.doFinal(resultFrom.getBytes());

        try (OutputStream outputStream = new FileOutputStream(to)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("IOException was caught");
        }
    }

    // * DECRYPTION
    public static void decryptFile(String from, String to) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // ? Take data
        List<Byte> array = new ArrayList();
        try (InputStream inputStream = new FileInputStream(from)) {
            while (inputStream.available() > 0) {
                array.add((byte) inputStream.read());
            }
        } catch (IOException e) {
            System.out.println("IOException was caught");
        }

        byte[] bytes = new byte[array.size()];
        for (int i = 0; i < array.size(); i++) {
            bytes[i] = array.get(i);
        }


        // ? Put data
        // --> GET CIPHER
        cipher.init(Cipher.DECRYPT_MODE, getKey());
        byte[] cbytes = cipher.doFinal(bytes);
        char[] chars = new char[cbytes.length];
        for (int i = 0; i < chars.length; i ++) {
            chars[i] = (char) cbytes[i];
        }

        try (Writer writer = new FileWriter(to)) {
            writer.write(chars);
        } catch (IOException e) {
            System.out.println("IOException was caught");
        }
    }
}
