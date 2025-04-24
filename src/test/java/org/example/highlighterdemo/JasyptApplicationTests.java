package org.example.highlighterdemo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void jasypt() {
        String access_key = "jdbc:mariadb://mariadb:3306/lol";

        System.out.println("value ::: "+jasyptAEncoding(access_key));
    }

    private String jasyptAEncoding(String url) {
        String key = "50cf1e2b-8a1e-472a-ad24-1a978afdcbae";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(url);
    }
}
