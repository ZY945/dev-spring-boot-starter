package io.github.zy945.influxDB.util;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;

/**
 * @author 伍六七
 * @date 2023/6/1 13:08
 */
public class InfluxDBManager {
    private static InfluxDBClient client;

    public static InfluxDBClient connectByToken(String url, String token, String bucket, String org) {

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(url)
                .authenticateToken(token.toCharArray())
                .bucket(bucket)
                .org(org)
                .build();
        client = InfluxDBClientFactory.create(options);
        return client;
    }

    public static void connectByToken(String url, String token, String org) {

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(url)
                .authenticateToken(token.toCharArray())
                .org(org)
                .build();
        client = InfluxDBClientFactory.create(options);
    }

    public static void connectByUserName(String url, String username, String password, String bucket, String org) {

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(url)
                .authenticate(username, password.toCharArray())
                .bucket(bucket)
                .org(org)
                .build();
        client = InfluxDBClientFactory.create(options);
    }

    public static void close() {
        client.close();
    }

    public static InfluxDBClient getClient() {
        return client;
    }
}
