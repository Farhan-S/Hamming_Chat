package com.pirhotech.hammingchat.errdetn;

public class HammingCode {
    public static String encode(String data) {
        if (data.length() != 4) {
            throw new IllegalArgumentException("Input data should be 4 bits long.");
        }

        int[] bits = new int[7];
        bits[2] = data.charAt(0) - '0';
        bits[4] = data.charAt(1) - '0';
        bits[5] = data.charAt(2) - '0';
        bits[6] = data.charAt(3) - '0';

        bits[0] = bits[2] ^ bits[4] ^ bits[6];
        bits[1] = bits[2] ^ bits[5] ^ bits[6];
        bits[3] = bits[4] ^ bits[5] ^ bits[6];

        StringBuilder encoded = new StringBuilder();
        for (int bit : bits) {
            encoded.append(bit);
        }
        return encoded.toString();
    }

    public static String decode(String data) {
        if (data.length() != 7) {
            throw new IllegalArgumentException("Encoded data should be 7 bits long.");
        }

        int[] bits = new int[7];
        for (int i = 0; i < 7; i++) {
            bits[i] = data.charAt(i) - '0';
        }

        int c1 = bits[0] ^ bits[2] ^ bits[4] ^ bits[6];
        int c2 = bits[1] ^ bits[2] ^ bits[5] ^ bits[6];
        int c4 = bits[3] ^ bits[4] ^ bits[5] ^ bits[6];

        int errorPosition = c1 + (c2 * 2) + (c4 * 4);
        if (errorPosition != 0) {
            bits[errorPosition - 1] ^= 1; // Fix the error
        }

        StringBuilder decoded = new StringBuilder();
        decoded.append(bits[2]).append(bits[4]).append(bits[5]).append(bits[6]);
        return decoded.toString();
    }

    public static String encodeLongString(String data) {
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < data.length(); i += 4) {
            String chunk = data.substring(i, Math.min(i + 4, data.length()));
            while (chunk.length() < 4) {
                chunk += "0"; // Padding with 0s to make it 4 bits
            }
            encoded.append(encode(chunk));
        }
        return encoded.toString();
    }

    public static String decodeLongString(String data) {
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < data.length(); i += 7) {
            String chunk = data.substring(i, Math.min(i + 7, data.length()));
            decoded.append(decode(chunk));
        }
        return decoded.toString();
    }
}
