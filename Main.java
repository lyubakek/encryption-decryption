package encryptdecrypt;

import java.io.*;
import java.util.Scanner;

class CipherAlgorithm {
    private CryptStrategy text;

    public CipherAlgorithm(CryptStrategy text) {
        this.text = text;
    }

    public String encrypt(String data, int key) {
        return text.encrypt(data, key);
    }

    public String decrypt(String data, int key) {
        return text.decrypt(data, key);
    }
}

interface CryptStrategy {
    String encrypt(String data, int key);
    String decrypt(String data, int key);
}

class CaesarCryptStrategy implements CryptStrategy {

    @Override
    public String encrypt(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (char character : data.toCharArray()) {
            if (character >= 'A' && character <= 'Z') {
                int currentIndex = (character - 'A');
                int newIndex = (currentIndex + key) % 26;
                char newChar = (char) (newIndex + 'A');
                result.append(newChar);
            } else if (character >= 'a' && character <= 'z') {
                int currentIndex = (character - 'a');
                int newIndex = (currentIndex + key) % 26;
                char newChar = (char) (newIndex + 'a');
                result.append(newChar);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String data, int key) {
        key = 26 - key % 26;
        String result = encrypt(data, key);
   return result;
    }
}

class UnicodeCryptStrategy implements CryptStrategy {
    final String alphabet = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    @Override
    public String encrypt(String data, int key) {
        int tmp = 0;
        int index = 0;
        String result = "";
        for (int i = 0; i < data.length(); i++) {
            index = -1;
            for (int j = 0; j < alphabet.length(); j++) {
                if (data.charAt(i) == alphabet.charAt(j)) {
                    tmp = j + key;
                    index = tmp % alphabet.length();
                    break;
                }
            }
            if (index == -1) {
                result += data.charAt(i);
            } else {
                result += alphabet.charAt(index);
            }
        }
        return result;
    }

    @Override
    public String decrypt(String data, int key) {
        int tmp = 0;
        int index = 0;
        String result = "";
        for (int i = 0; i < data.length(); i++) {
            index = -1;
            for (int j = 0; j < alphabet.length(); j++) {
                if (data.charAt(i) == alphabet.charAt(j)) {
                    tmp = j - key;
                    index = tmp % alphabet.length();
                    break;
                }
            }
            if (index == -1) {
                result += data.charAt(i);
            } else {
                result += alphabet.charAt(index);
            }
        }
        return result;
    }
}

public class Main {

    public static void main(String[] args) {

        String result = "";

        String encOrDec = "";
        int key = 0;
        String data = "";
        String nameIn = "";
        String nameOut = "";
        String algorithm = "";

        int k = 0;

        while (k < (args.length - 1)) {

            switch (args[k]) {
                case "-mode":
                    encOrDec = args[k + 1];
                    k++;
                    break;
                case "-key":
                    key = Integer.parseInt(args[k + 1]);
                    k++;
                    break;
                case "-data":
                    data = args[k + 1];
                    k++;
                    break;
                case "-in":
                    nameIn = args[k + 1];
                    k++;
                    break;
                case "-out":
                    nameOut = args[k + 1];
                    k++;
                    break;
                case "-alg":
                    algorithm = args[k + 1];
                    k++;
                    break;
            }
                 k++;
            }

        File fileIn = new File(nameIn);
        try (Scanner scanner = new Scanner(fileIn)) {
            data = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + fileIn);
        }

        CipherAlgorithm encryption = null;
        CipherAlgorithm decryption = null;

        if (encOrDec.equals("enc")) {
            if (algorithm.equals("shift")) {
                encryption = new CipherAlgorithm(new CaesarCryptStrategy());
                result = encryption.encrypt(data, key);
            } if (algorithm.equals("unicode")) {
                encryption = new CipherAlgorithm(new UnicodeCryptStrategy());
                result = encryption.encrypt(data, key);
            }
        } else if (encOrDec.equals("dec")) {
            if (algorithm.equals("shift")) {
                decryption = new CipherAlgorithm(new CaesarCryptStrategy());
                result = decryption.decrypt(data, key);
            }
            if (algorithm.equals("unicode")) {
                decryption = new CipherAlgorithm(new UnicodeCryptStrategy());
                result = decryption.decrypt(data, key);
            }
        }
        System.out.println(result);

        File fileOut = new File(nameOut);
        try (PrintWriter printWriter = new PrintWriter(fileOut)) {
            printWriter.print(result);
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }
}