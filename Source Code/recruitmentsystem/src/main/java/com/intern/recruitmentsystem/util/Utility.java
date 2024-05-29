package com.fpt.recruitmentsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utility {
    // Private constructor to hide the implicit public one
    private Utility() {}

    public static List<Integer> parseIdList(String idList) {
        return Arrays.stream(idList.split(","))
            .map(Integer::parseInt)
            .toList();
    }
    public static String trimString(String str) {
        return str.trim();
    }
    public static <T> List<T> getPage(List<T> items, int pageSize, int pageNumber) {
        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (pageNumber < 1 || pageNumber > totalPages) {
            throw new IllegalArgumentException("Invalid page number");
        }
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        return items.subList(fromIndex, toIndex);
    }

    public static boolean checkEmail(String email) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);

        String requestBody = mapToFormData(parameters);

        HttpRequest request = null;
        try {
            String emailCheckURI = "https://melink.vn/checkmail/checkemail.php";
            request = HttpRequest.newBuilder()
                    .uri(new URI(emailCheckURI))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            return responseBody.equals("<span style='color:green'><b>Valid!</b>");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String mapToFormData(Map<String, String> parameters) {
        StringBuilder formData = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (formData.length() > 0) {
                formData.append("&");
            }
            formData.append(entry.getKey());
            formData.append("=");
            formData.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return formData.toString();
    }

    public static Date convertStringToDate(String dateString) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        java.util.Date utilDate;

        try {
            utilDate = dateFormat.parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void responseObject(HttpServletResponse response, Object data) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(data));
    }


    public static Pageable getPageable(Integer page, Integer limit) {
        int pageNumber = page != null ? page - 1 : 0;
        int pageSize = limit != null ? limit : Integer.MAX_VALUE;
        if (limit == null && page != null) {
            pageSize = 10;
        }
        return PageRequest.of(pageNumber, pageSize);
    }
}
