package com.minbash.getsecure;

import org.apache.commons.codec.binary.Base64;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for generating secure links with expiration timestamps.
 */
public final class SecureLink {
    /**
     * Default validity period in minutes.
     */
    private static final int DEFAULT_PERIOD = 30;

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SecureLink() {
        // Utility class should not be instantiated
    }

    /**
     * Generates a secure link with an expiration timestamp and MD5 hash.
     *
     * @param baselink Base URL for signing
     * @param secret Secret string shared only with web server
     * @param period Optional period in days
     * @return Signed link as String
     */
    public static String secureLink(final String baselink, final String secret,
            final int period) {
        try {
            URI url = new URI(baselink);
            long expires = Instant.now()
                    .plus(period, ChronoUnit.DAYS)
                    .getEpochSecond();
            String hashString = expires + url.getPath() + " " + secret;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(
                    hashString.getBytes(StandardCharsets.UTF_8));
            String protectionString = Base64.encodeBase64URLSafeString(hashBytes);
            
            return String.format("%s?md5=%s&expires=%d",
                    baselink, protectionString, expires);
        } catch (Exception e) {
            throw new RuntimeException("Error generating secure link", e);
        }
    }

    /**
     * Main method for command-line usage.
     *
     * @param args Command line arguments: baselink secret [period]
     */
    public static void main(final String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar getsecure.jar"
                    + " <baselink> <secret> [period]");
            System.exit(1);
        }

        String baselink = args[0];
        String secret = args[1];
        int period = args.length > 2
                ? Integer.parseInt(args[2])
                : DEFAULT_PERIOD;

        try {
            String link = secureLink(baselink, secret, period);
            System.out.println(link);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
