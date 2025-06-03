package com.example.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.MacAlgorithm;

import javax.crypto.SecretKey;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        // Obtain the MacAlgorithm instance for HS256
        MacAlgorithm alg = Jwts.SIG.HS256;

        // Generate the SecretKey using the algorithm's key builder
        SecretKey key = alg.key().build();

        // Encode the key to Base64 for storage
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("Generated Base64-encoded secret key:");
        System.out.println(base64Key);
    }
}
