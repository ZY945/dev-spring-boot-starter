package io.github.zy945.util.http;


import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 伍六七
 * @date 2023/5/17 9:39
 */
public class HttpUtil {
    public HttpURLConnection proxy(String url, String proxyHost, int proxyPort) throws IOException {
        URL httpUrl = new URL(url);
        SocketAddress sa = new InetSocketAddress(proxyHost, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
        return (HttpURLConnection) httpUrl.openConnection(proxy);
    }

    public static String getResponseStr(HttpURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        }
        return response.toString();
    }

    public static JSONObject getJsonObject(HttpURLConnection con) throws IOException {
        JSONObject jsonObject = new JSONObject();
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            jsonObject = new JSONObject(response.toString());
        }
        return jsonObject;
    }

    public static String getUrlByParams(String url, Map<String, String> params) {
        // 将请求参数拼接到 URL 上
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean isFirst = true;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (isFirst) {
                urlBuilder.append("?");
                isFirst = false;
            } else {
                urlBuilder.append("&");
            }
            urlBuilder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            urlBuilder.append('=');
            urlBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
        }
        return urlBuilder.toString();
    }

    public static String get(String url) {
        StringBuilder response = new StringBuilder();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            // 发送 HTTP 请求
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                InputStream inStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "get请求失败";
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String url) {
        StringBuilder response = new StringBuilder();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            // 发送 HTTP 请求
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                InputStream inStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                jsonObject = new JSONObject(response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public static JSONObject getDataJsonObject(JSONObject jsonObject, String url) {
        return getJsonObject(jsonObject, url).getJSONObject("data");
    }

    public static String get(String url, Map<String, String> params) {
        StringBuilder response = new StringBuilder();
        try {
            // 将请求参数拼接到 URL 上
            url = getUrlByParams(url, params);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            // 发送 HTTP 请求
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                InputStream inStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "get请求失败";
    }

    /**
     * post请求错误
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    @Deprecated
    public static String postForm(String url, Map<String, String> params) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        // 设置连接超时时间
        con.setConnectTimeout(3000);
        // 设置是否使用缓存
//            con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        //对于post请求，参数要放在 http 正文内，因此需要设为true，默认为false。
        con.setDoOutput(true);
        StringBuilder postData = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() == 0 && isFirst) {
                postData.append("?");
                isFirst = false;
            } else {
                postData.append("&");
            }

            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append("=");
            postData.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }


        //将表单写入请求
        OutputStream os = con.getOutputStream();
        byte[] bytes = postData.toString().getBytes(StandardCharsets.UTF_8);
        os.write(bytes);
        os.flush();
        os.close();


        // 发送 HTTP 请求
        int responseCode = con.getResponseCode();
        String requestMethod = con.getRequestMethod();//POST
        String responseMessage = con.getResponseMessage();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应数据
            InputStream inStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            return "post请求失败";
        }
        //不要在finally里return throw,会导致try和throw的return和throw失效
        //如果catch捕获异常了,finally会在异常被抛出前执行
        //try中return后的throw是不会执行
    }
}
