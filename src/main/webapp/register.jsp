<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, java.sql.PreparedStatement, java.sql.SQLException" %>
<%@ page import="com.yourtube.util.DBConnection" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - YourTube</title>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script> <!-- jQuery for validation -->

    <style>
        body {
            background-color: #121212;
            color: #fff;
            font-family: Arial, sans-serif;
        }

        .navbar {
            background-color: #FF0000;
        }

        .navbar-brand {
            color: #fff;
            font-weight: bold;
        }

        .navbar-nav .nav-link {
            color: white;
        }

        .container {
            animation: fadeIn 1.5s ease-in-out;
        }

        .card {
            background-color: #181818;
            border: 1px solid #FF0000;
        }

        .form-control {
            background-color: #121212;
            color: white;
            border: 1px solid #FF0000;
        }

        .btn-primary {
            background-color: #FF0000;
            border: none;
            transition: transform 0.2s ease;
        }

        .btn-primary:hover {
            transform: scale(1.05);
            background-color: #FF4D4D;
        }

        .alert {
            animation: slideDown 0.8s ease forwards;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>

<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg">
        <a class="navbar-brand" href="index.jsp">YourTube</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="login.jsp">Login</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="register.jsp">Register</a>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Registration Form Section -->
    <div class="container mt-5">
        <h2 class="text-center mb-4">Create Your Account</h2>
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg">
                    <div class="card-body">
                        <form action="register.jsp" method="post" id="registerForm">
                            <!-- Username Field -->
                            <div class="form-group">
                                <label for="username">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>

                            <!-- Email Field -->
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>

                            <!-- Name Field -->
                            <div class="form-group">
                                <label for="name">Full Name</label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>

                            <!-- Password Field -->
                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>

                            <!-- Confirm Password Field -->
                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                            </div>

                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-primary btn-block">Register</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JSP Code for Registration Handling -->
    <%
        if (request.getMethod().equalsIgnoreCase("POST")) {
            Connection conn = null;
            PreparedStatement pstmt = null;

            try {
                conn = DBConnection.getConnection();
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String confirmPassword = request.getParameter("confirmPassword");
                String name = request.getParameter("name");
                String email = request.getParameter("email");

                if (!password.equals(confirmPassword)) {
                    out.println("<div class='alert alert-danger text-center'>Passwords do not match!</div>");
                } else if (username != null && password != null && email != null && name != null) {
                    String sql = "INSERT INTO users (username, password, full_name, email) VALUES (?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.setString(3, name);
                    pstmt.setString(4, email);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        out.println("<div class='alert alert-success text-center'>Registration successful! You can now <a href='login.jsp'>login</a>.</div>");
                    } else {
                        out.println("<div class='alert alert-danger text-center'>Registration failed. Please try again.</div>");
                    }
                } else {
                    out.println("<div class='alert alert-danger text-center'>Please fill out all required fields.</div>");
                }

            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    out.println("<div class='alert alert-danger text-center'>Username or email already exists. Please choose a different one.</div>");
                } else {
                    e.printStackTrace();
                    out.println("<div class='alert alert-danger text-center'>An error occurred: " + e.getMessage() + "</div>");
                }
            } finally {
                if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    %>

    <!-- Footer Section -->
    <footer class="bg-dark text-white text-center py-3 mt-5">
        <p>&copy; 2024 YourTube. All rights reserved.</p>
    </footer>

    <!-- Bootstrap JS and Dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#registerForm').submit(function (event) {
                var password = $('#password').val();
                var confirmPassword = $('#confirmPassword').val();

                if (password !== confirmPassword) {
                    event.preventDefault();
                    alert('Passwords do not match!');
                }
            });
        });
    </script>
</body>

</html>