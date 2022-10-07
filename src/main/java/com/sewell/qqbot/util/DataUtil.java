package com.sewell.qqbot.util;


import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 */
public class DataUtil {
    public static String readData() {
        ClassPathResource cpr = new ClassPathResource("idiom.json");
        BufferedReader reader = null;
        try {
            InputStream in = cpr.getInputStream();
            if (in == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
