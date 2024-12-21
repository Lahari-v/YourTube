
package com.yourtube.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {
    // doPost method to handle form submission for login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get login details from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check credentials against the database
        boolean isValidUser = validateUser(username, password);

        if (isValidUser) {
            // Credentials are correct, set session attribute
            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            // Redirect to the YouTube-like homepage
            response.sendRedirect("home.jsp");
        } else {
            // Invalid credentials, forward to the login page with an error message
            request.setAttribute("errorMessage", "Invalid username or password");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }

    // doGet method to handle page access and ensure login status
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If the user is already logged in, redirect to the homepage
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect("home.jsp");
        } else {
            // Otherwise, show the login page
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }

    // Method to validate user credentials from the database
    private boolean validateUser(String username, String password) {
        System.out.println("hello");
        boolean isValid = false;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    isValid = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error validating user: " + e.getMessage());
        }
        return isValid;
    }
}
