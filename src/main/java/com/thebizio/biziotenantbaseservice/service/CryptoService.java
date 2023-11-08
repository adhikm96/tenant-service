package com.thebizio.biziotenantbaseservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;

@Service
public class CryptoService {

    @Value(("${encryption-secret-key}"))
    private String encryptionSecretKey;

    @Value(("${secret-key-salt}"))
    private String secretKeySalt;

    private TextEncryptor textEncryptor = null;

    @PostConstruct
    private void init() {
       textEncryptor = Encryptors.text(encryptionSecretKey, secretKeySalt);
    }

    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String data) {
        return textEncryptor.decrypt(data);
    }
}
