package com.pirhotech.hammingchat.e2ee;
import android.content.Context;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.integration.android.AndroidKeysetManager;

import java.nio.charset.StandardCharsets;

public class CryptoManager {
    private static final String KEYSET_NAME = "master_keyset";
    private static final String PREF_FILE_NAME = "master_key_preference";
    private static final String MASTER_KEY_URI = "android-keystore://master_key";

    private Aead aead;

    public CryptoManager(Context context) throws Exception {
        AeadConfig.register();

        KeysetHandle keysetHandle = new AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .getKeysetHandle();

        aead = keysetHandle.getPrimitive(Aead.class);
    }

    public byte[] encrypt(String plaintext, byte[] associatedData) throws Exception {
        return aead.encrypt(plaintext.getBytes(StandardCharsets.UTF_8), associatedData);
    }

    public String decrypt(byte[] ciphertext, byte[] associatedData) throws Exception {
        byte[] decrypted = aead.decrypt(ciphertext, associatedData);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
