package com.sethholloway.toopherdemo;

import com.toopher.AuthenticationStatus;
import com.toopher.PairingStatus;
import com.toopher.ToopherAPI;
import com.toopher.RequestError;

public class Main {
    private static final long TIMEOUT_MILLIS = 60000;
    private static final String USERNAME = "me@java";
    private static final String TERMINAL_NAME = "my computer";
    private static final String PAIRING_PHRASE = "rural colony";

    public static void main(String[] args) {
        ToopherAPI api = getToopherApi();

        try {
            PairingStatus pairing = pair(api);
            AuthenticationStatus auth = authenticate(api, pairing);
            if (auth != null && auth.granted) {
                System.out.println("Authentication allowed.");
            } else {
                System.out.println("Authentication denied.");
            }
        } catch (Exception e) {
            System.out.println("We seem to have hit a snag. Please check your credentials and pairing phrase.");
        }
    }

    private static ToopherAPI getToopherApi() {
        // Create an API object using your credentials
        final String key = System.getenv("TOOPHER_CONSUMER_KEY");
        final String secret = System.getenv("TOOPHER_CONSUMER_SECRET");
        if (key == null || secret == null) {
            System.out.println("Please set your key and secret.");
            throw new IllegalStateException("set TOOPHER_CONSUMER_KEY and TOOPHER_CONSUMER_SECRET");
        }
        ToopherAPI api = new ToopherAPI(key, secret);

        return api;
    }

    private static PairingStatus pair(ToopherAPI api) {
        System.out.println("Pairing. Please check the Toopher app on your mobile device.");

        PairingStatus pairing = null;
        try {
            long startTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Step 1 - Pair with their phone's Toopher app
            pairing = api.pair(PAIRING_PHRASE, USERNAME);

            // Wait for the user to respond
            while (!pairing.enabled && elapsedTime < TIMEOUT_MILLIS) {
                pairing = api.getPairingStatus(pairing.id);
                waitToPoll();
                elapsedTime = System.currentTimeMillis() - startTime;
            }
        } catch (RequestError e) {
            System.out.println("Trouble pairing. Please check your PAIRING_PHRASE.");
            e.printStackTrace();
        }
        return pairing;
    }

    private static AuthenticationStatus authenticate(ToopherAPI api, PairingStatus pairing) {
        System.out.println("Authenticating. Please check the Toopher app on your mobile device.");

        AuthenticationStatus auth = null;
        try {
            long startTime = System.currentTimeMillis();
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Step 2 - Authenticate a log in
            auth = api.authenticate(pairing.id, TERMINAL_NAME);

            // Wait for the user to respond
            while (auth.pending && elapsedTime < TIMEOUT_MILLIS) {
                auth = api.getAuthenticationStatus(auth.id);
                waitToPoll();
                elapsedTime = System.currentTimeMillis() - startTime;
            }
        } catch (RequestError e) {
            System.out.println("Trouble authenticating.");
            e.printStackTrace();
        }
        return auth;
    }

    private static void waitToPoll() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}