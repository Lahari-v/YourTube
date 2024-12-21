package com.yourtube.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {

    // Method to retrieve video details by video ID
    public VideoDetails getVideoById(String videoId) {
        VideoDetails video = null;
        String query = "SELECT video_id, title, description, thumbnail_url, view_count, like_count FROM videos WHERE video_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, videoId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                video = new VideoDetails(

                        resultSet.getString("video_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail_url"),
                        resultSet.getInt("view_count"),
                        resultSet.getInt("like_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(video == null) {
            System.out.println("No video found for id: " + videoId);
        }
        return video;
    }

    // Method to add video to the user's history
    public void addHistory(String username, String videoId) {
        String query = "INSERT INTO video_history (username, video_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, videoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve the user's watch history
    public List<VideoDetails> getUserHistory(String username) {
        List<VideoDetails> historyVideos = new ArrayList<>();
        String query = "SELECT v.video_id, v.title, v.description, v.thumbnail_url " +
                "FROM video_history vh " +
                "JOIN videos v ON vh.video_id = v.video_id " +
                "WHERE vh.username = ? " +
                "ORDER BY vh.watch_time DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                VideoDetails video = new VideoDetails(
                        resultSet.getString("video_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail_url")
                );
                historyVideos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyVideos;
    }

    // Method to retrieve all videos (for homepage or search)
    public List<VideoDetails> getAllVideos() {
        List<VideoDetails> videos = new ArrayList<>();
        String query = "SELECT video_id, title, description, thumbnail_url FROM videos";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                VideoDetails video = new VideoDetails(
                        resultSet.getString("video_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail_url")
                );
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return videos;
    }

    // Method to add a video (used during upload)
    public void addVideo(VideoDetails video) {
        String query = "INSERT INTO videos (video_id, title, description, thumbnail_url, view_count, like_count) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, video.getVideoId());
            statement.setString(2, video.getTitle());
            statement.setString(3, video.getDescription());
            statement.setString(4, video.getThumbnailUrl());
            statement.setInt(5, video.getViewCount());
            statement.setInt(6, video.getLikeCount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}