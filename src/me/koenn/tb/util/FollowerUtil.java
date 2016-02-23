package me.koenn.tb.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

public class FollowerUtil implements Runnable {

    public static ArrayList<String> currentFollowers = new ArrayList<>();
    private static int lastFollowersSize;

    private static void checkDone(JSONObject jsonObject) {
        if ((long) jsonObject.get("_total") > currentFollowers.size()) {
            ArrayList<String> newFollowers = getFollowers((String) ((JSONObject) jsonObject.get("_links")).get("next"));
            if (newFollowers == null) {
                return;
            }
            currentFollowers.addAll(newFollowers);
            checkDone(jsonObject);
        }
    }

    private static ArrayList<String> getFollowers(String url) {
        System.out.println("Getting Information From Twitch API");
        String followerInformation;
        try {
            followerInformation = Utils.urlToString(url);
        } catch (ConnectException e) {
            System.err.println("Couldn't Connect To Twitch API!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(followerInformation);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray followers = (JSONArray) jsonObject.get("follows");
            ArrayList<String> currentFollowers = new ArrayList<>();
            for (Object follower : followers) {
                currentFollowers.add(((String) ((JSONObject) ((JSONObject) follower).get("user")).get("display_name")).toLowerCase());
            }
            return currentFollowers;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            System.out.println("Getting Information From Twitch API");
            String followerInformation;
            try {
                followerInformation = Utils.urlToString("https://api.twitch.tv/kraken/channels/" + BotUtil.login.getChannel() + "/follows?direction=DESC&limit=100");
            } catch (ConnectException e) {
                System.err.println("Couldn't Connect To Twitch API!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(followerInformation);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray followers = (JSONArray) jsonObject.get("follows");
                currentFollowers.clear();
                for (Object follower : followers) {
                    currentFollowers.add(((String) ((JSONObject) ((JSONObject) follower).get("user")).get("display_name")).toLowerCase());
                }
                checkDone(jsonObject);
                System.out.println("Current follower count: " + currentFollowers.size());
                if (currentFollowers.size() > lastFollowersSize) {
                    obj = parser.parse(followerInformation);
                    jsonObject = (JSONObject) obj;
                    followers = (JSONArray) jsonObject.get("follows");
                    System.out.println("New follower! " + ((JSONObject) ((JSONObject) followers.get(0)).get("user")).get("display_name"));
                }
                lastFollowersSize = currentFollowers.size();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
