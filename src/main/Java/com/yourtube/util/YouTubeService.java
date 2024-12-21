package com.yourtube.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

public class YouTubeService {

    private static final String API_KEY = "AIzaSyBaadS3qs08dUK_3CXWi2gpFln4sUQGu2E"; // Replace with your YouTube Data API key
    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=";

    // Method to search for videos based on a query
    public List<VideoDetails> searchVideos(String query) {
        List<VideoDetails> videoDetailsList = new ArrayList<>();
        try {
            // URL encode the query to handle special characters
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");

            // Construct the URL for the YouTube API request
            String urlString = YOUTUBE_API_URL + encodedQuery + "&key=" + API_KEY;
            System.out.println("YouTube API URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the response into JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("API Response: " + jsonResponse.toString());
            if (jsonResponse.has("items")) {
                JSONArray items = jsonResponse.getJSONArray("items");

                // Loop through each video item and map to VideoDetails object
                for (int i = 0; i < items.length(); i++) {
                    JSONObject video = items.getJSONObject(i);
                    JSONObject snippet = video.getJSONObject("snippet");

                    // Correctly extract videoId from the "id" object
                    String videoId = video.getJSONObject("id").getString("videoId");
                    String title = snippet.getString("title");
                    String description = snippet.getString("description");
                    String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                    // Create VideoDetails object and add to the list
                    VideoDetails details = new VideoDetails(videoId, title, description, thumbnailUrl);
                    videoDetailsList.add(details);
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching YouTube videos: " + e.getMessage());
            e.printStackTrace();
        }
        return videoDetailsList;
    }

    // Method to fetch detailed information about a list of video IDs
    public static List<VideoDetails> getVideoDetails(List<String> videoIds) {
        List<VideoDetails> videoDetailsList = new ArrayList<>();
        try {
            // Build the URL to fetch details for multiple video IDs
            String videoIdsParam = String.join(",", videoIds);
            String urlString = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=" + videoIdsParam + "&key=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the response into JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("items")) {
                JSONArray items = jsonResponse.getJSONArray("items");

                // Loop through each video item and map to VideoDetails object
                for (int i = 0; i < items.length(); i++) {
                    JSONObject video = items.getJSONObject(i);
                    JSONObject snippet = video.getJSONObject("snippet");
                    JSONObject statistics = video.getJSONObject("statistics");

                    String videoId = video.getString("id");
                    String title = snippet.getString("title");
                    String description = snippet.getString("description");
                    String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    int viewCount = statistics.getInt("viewCount");
                    int likeCount = statistics.getInt("likeCount");

                    // Create VideoDetails object and add to the list
                    VideoDetails details = new VideoDetails(videoId, title, description, thumbnailUrl, viewCount, likeCount);
                    videoDetailsList.add(details);
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred while fetching video details: " + e.getMessage());
            e.printStackTrace();
        }
        return videoDetailsList;
    }
}
