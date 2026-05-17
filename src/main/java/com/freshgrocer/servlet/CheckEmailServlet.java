package com.freshgrocer.servlet;

import com.freshgrocer.util.UserFileHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/CheckEmailServlet")
public class CheckEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            UserFileHandler fh = new UserFileHandler();
            boolean exists = fh.emailExists(email);
            PrintWriter out = response.getWriter();
            out.print("{\"exists\":" + exists + "}");
            out.flush();
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.print("{\"exists\":false}");
            out.flush();
        }
    }
}