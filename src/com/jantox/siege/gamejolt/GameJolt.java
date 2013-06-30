package com.jantox.siege.gamejolt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// maintains ping session etc
public class GameJolt {

    MessageDigest md5;

    public GameJolt() {
        this.open();
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String generateSignature(String orig) {
        byte[] after = null;
        String digest = "";

        try {
            after = md5.digest(orig.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder(2 * after.length);
            for(byte b : after) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return digest;
    }

    public void open() {
        String req = "http://gamejolt.com/api/game/v1/sessions/open/?game_id=15326&username=agrosis&user_token=1f368a";
        String signature = generateSignature(req + "78c8a6868ddabaa3e208b2ce50b6b5b9");

        req += "&signature=" + signature;

        try {
            URLConnection url = new URL(req).openConnection();
            displayResult(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ping() {
        String req = "http://gamejolt.com/api/game/v1/sessions/ping/?game_id=15326&username=agrosis&user_token=1f368a";
        String signature = generateSignature(req + "78c8a6868ddabaa3e208b2ce50b6b5b9");

        req += "&signature=" + signature;

        try {
            URLConnection url = new URL(req).openConnection();
            displayResult(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayResult(URLConnection url) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    url.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void close() {
        String req = "http://gamejolt.com/api/game/v1/sessions/close/?game_id=15326&username=agrosis&user_token=1f368a";
        String signature = generateSignature(req + "78c8a6868ddabaa3e208b2ce50b6b5b9");

        req += "&signature=" + signature;

        try {
            URLConnection url = new URL(req).openConnection();
            displayResult(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
