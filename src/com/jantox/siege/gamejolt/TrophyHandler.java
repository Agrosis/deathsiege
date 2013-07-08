package com.jantox.siege.gamejolt;

import com.jantox.siege.GameInstance;
import com.jantox.siege.gfx.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TrophyHandler {

    private ArrayList<Trophy> trophies;

    public TrophyHandler() {
        this.loadTrophies();
    }

    public String generateSignature(String orig) {
        byte[] after = null;
        String digest = "";

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public void loadTrophies() {
        trophies = new ArrayList<Trophy>();

        String req = "http://gamejolt.com/api/game/v1/trophies/?game_id=15326&username=agrosis&user_token=1f368a";
        String signature = generateSignature(req + "78c8a6868ddabaa3e208b2ce50b6b5b9");

        req += "&signature=" + signature;

        try {
            URLConnection url = new URL(req).openConnection();
            String result = displayResult(url);

            String lines[] = result.split("\n");
            Trophy current = null;

            String id = "", title = "", desc = "", diff = "", achieved = "";

            for(int i = 0; i < lines.length; i++) {
                String line = lines[i];

                if(line.startsWith("id")) {
                    if(current != null) {
                        current = new Trophy(Integer.valueOf(id), title, desc, diff, achieved);
                        this.trophies.add(current);
                    } else
                        current = new Trophy(0, "", "", "", "");
                    id = getData(line);
                } else if(line.startsWith("title")) {
                    title = getData(line);
                } else if(line.startsWith("description")) {
                    desc = getData(line);
                } else if(line.startsWith("difficulty")) {
                    diff = getData(line);
                } else if(line.startsWith("achieved")) {
                    achieved = getData(line);
                }
            }

            current = new Trophy(Integer.valueOf(id), title, desc, diff, achieved);
            this.trophies.add(current);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTrophy(int tid) {
        String req = "http://gamejolt.com/api/game/v1/trophies/add-achieved/?game_id=15326&username=agrosis&user_token=1f368a&trophy_id=" + tid;
        String signature = generateSignature(req + "78c8a6868ddabaa3e208b2ce50b6b5b9");

        req += "&signature=" + signature;

        for(int i = 0; i < trophies.size(); i++) {
            if(trophies.get(i).getID() == tid) {
                trophies.get(i).setAchieved(true);
                GameInstance.notifications.add(new Notification("Achievement Get!", trophies.get(i).getTitle(), 600));
            }
        }

        Thread t = new Thread(new MessageSender(req));
        t.start();
    }

    public String getData(String mod) {
        String copy = mod;
        copy = copy.substring(mod.indexOf("\""));
        copy = copy.replaceAll("\"", "");
        return copy;
    }

    public String displayResult(URLConnection url) {
        BufferedReader in = null;

        String data = "";

        try {
            in = new BufferedReader(new InputStreamReader(
                    url.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data += inputLine + "\n";
                //System.out.println(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean has(int trophyid) {
        for(Trophy t : trophies) {
            if(t.getID() == trophyid && t.isAchieved()) {
                return true;
            }
        }
        return false;
    }
}
