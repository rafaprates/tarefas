package com.rafaelcardoso.tarefas.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class RsaKeyProperties {

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    public RsaKeyProperties(@Value("${rsa.public-key}") RSAPublicKey publicKey,
                            @Value("${rsa.private-key}") RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}
