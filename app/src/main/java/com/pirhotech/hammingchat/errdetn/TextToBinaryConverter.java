package com.pirhotech.hammingchat.errdetn;

public class TextToBinaryConverter {
    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char character : text.toCharArray()) {
            String binaryChar = Integer.toBinaryString(character);
            while (binaryChar.length() < 8) { // Ensure each char is 8 bits long
                binaryChar = "0" + binaryChar;
            }
            binary.append(binaryChar);
        }
        return binary.toString();
    }

    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteString = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteString, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }
}
