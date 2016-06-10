package com.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil {
    public static void write(HttpServletResponse response, String str) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.write(str);
    }
}
