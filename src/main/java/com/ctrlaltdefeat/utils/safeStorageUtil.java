package com.ctrlaltdefeat.utils;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;

public class safeStorageUtil {
    private static CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName("MySystem", key)
        );
    }

    public static void store(String key, String value) {
        CredentialAttributes attributes = createCredentialAttributes(key);
        Credentials credentials = new Credentials(value, "");
        PasswordSafe.getInstance().set(attributes, credentials);
    }

    public static String get(String key) {
        CredentialAttributes attributes = createCredentialAttributes(key);
        PasswordSafe passwordSafe = PasswordSafe.getInstance();

        Credentials credentials = passwordSafe.get(attributes);
        if (credentials != null) {
            return credentials.getUserName();
        }

        return "";
    }
}
