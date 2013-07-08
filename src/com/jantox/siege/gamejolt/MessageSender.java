package com.jantox.siege.gamejolt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MessageSender implements Runnable {

    private String request;

    public MessageSender(String request) {
        this.request = request;
    }

    @Override
    public void run() {
        URLConnection url = null;
        try {
            url = new URL(request).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayResult(url);
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

}
