package com.minbash.getsecure;

import org.apache.commons.codec.binary.Base64;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SecureLink {
    /**
     * Generates a secure link with an expiration timestamp and MD5 hash
     *
     * @param baselink Base URL for signing
     * @param secret   Secret string shared only with web server
     * @param period   Optional period in days
     * @return Signed link as String
     */
    public static String secureLink(String baselink, String secret, int period) {
        try {
            URI url = new URI(baselink);
            
            // Calculate expiration timestamp
            long expires = Instant.now().plus(period, ChronoUnit.DAYS).getEpochSecond();
            
            // Create hash string
            String hashString = expires + url.getPath() + " " + secret;
            
            // Calculate MD5 hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(hashString.getBytes(StandardCharsets.UTF_8));
            
            // Encode hash using base64url encoding (base64 with -_ instead of +/)
            String protectionString = Base64.encodeBase64URLSafeString(hashBytes);
            
            // Build the protected link
            return String.format("%s?md5=%s&expires=%d", baselink, protectionString, expires);
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating secure link", e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar getsecure.jar <baselink> <secret> [period]");
            System.exit(1);
        }

        String baselink = args[0];
        String secret = args[1];
        int period = args.length > 2 ? Integer.parseInt(args[2]) : 30;

        try {
            String link = secureLink(baselink, secret, period);
            System.out.println(link);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
} 