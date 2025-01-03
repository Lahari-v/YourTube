package com.yourtube.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VideoPlaybackServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Step 1: Get the videoId from the request
        String videoId = request.getParameter("videoId");

        // Step 2: Ensure videoId is provided
        if (videoId == null || videoId.isEmpty()) {
            response.sendRedirect("error.jsp"); // Redirect to an error page if no videoId
            return;
        }

        System.out.println("Video ID received: " + videoId); // For debugging

        // Step 3: Check if the user is logged in (session check)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // Redirect to login page and pass the videoId as a query parameter
            response.sendRedirect("login.jsp?videoId=" + videoId);
            return;
        }

        String username = (String) session.getAttribute("username");
        System.out.println("User logged in: " + username); // For debugging

        // Step 4: Fetch video details from the database
        VideoDAO videoDAO = new VideoDAO();
        VideoDetails video = videoDAO.getVideoById(videoId);


        // Step 5: Check if video exists
        if (video == null) {
            response.sendRedirect("error.jsp"); // Redirect if video is not found
            return;
        }
        System.out.println("Video found: " + video.getTitle());// For debugging

        // Step 6: Add video to the user's watch history
        videoDAO.addHistory(username, videoId);

        // Step 7: Pass video details to the JSP
        request.setAttribute("video", video);
        request.getRequestDispatcher("videoplayback.jsp").forward(request, response);
    }



}
