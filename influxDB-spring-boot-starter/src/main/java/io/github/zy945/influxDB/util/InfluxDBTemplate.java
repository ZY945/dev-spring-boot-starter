package io.github.zy945.influxDB.util;

import com.influxdb.client.*;
import com.influxdb.client.domain.Query;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxTable;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;
import io.github.zy945.influxDB.empty.bo.query.InfluxBO;
import io.github.zy945.influxDB.empty.bo.query.QueryType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.DependsOn;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 伍六七
 * @date 2023/7/2 21:33
 * 目前client是在每个方法
 */
@DependsOn(value = "influxByToken")
public class InfluxDBTemplate {
    private static volatile InfluxDBTemplate influxDBTemplate;
    private static volatile QueryApi queryApi;
    private static volatile WriteApi writeApi;


    @Resource(name = "influxByToken")
    private InfluxDBClient client;

    //1.获取对象
    public static InfluxDBTemplate getSingleton() {
        if (influxDBTemplate == null) {
            synchronized (InfluxDBTemplate.class) {
                if (influxDBTemplate == null) {
                    influxDBTemplate = new InfluxDBTemplate();
                }
            }
        }
        return influxDBTemplate;
    }

    public void getQueryApi() {
        if (queryApi == null) {
            synchronized (InfluxDBTemplate.class) {
                if (queryApi == null) {
                    queryApi = client.getQueryApi();
                }
            }
        }
    }

    public void getWriteApi() {
        if (writeApi == null) {
            synchronized (InfluxDBTemplate.class) {
                if (writeApi == null) {
                    writeApi = client.makeWriteApi(WriteOptions.builder().flushInterval(5_000).build());
                }
            }
        }
    }


    //2.query方法

    /**
     * 有问题---这可能是和时序数据库返回的数据格式有关
     */
    @Deprecated
    public <M> List<M> query(@Nonnull QueryType queryType, @Nonnull InfluxBO bo, @Nonnull Class<M> mClass) {
        getQueryApi();

        switch (queryType) {
            case FluxStr -> {
                String flux = bo.getFluxStr();
                if (flux != null) {
                    return queryApi.query(flux, mClass);
                }
            }
            case Flux -> {
                Flux flux = bo.getFlux();
                if (flux != null) {
                    return queryApi.query(flux.toString(), mClass);
                }
            }
            case Query -> {
                Query query = bo.getQuery();
                if (query != null) {
                    return queryApi.query(query, mClass);
                }
            }
        }
        client.close();
        // 返回一个空列表作为默认值或根据实际需求进行处理
        return new ArrayList<>();
    }

    /**
     * 查询行数(无限制)
     *
     * @param measurement
     * @return
     */
    public Long count(String measurement) {
        Instant stop = DateUtil.getNowInstant();
        Instant start = Instant.ofEpochMilli(-365243219162L);
        return countByRange(measurement, start, stop);
    }

    /**
     * 查询行数(无限制)
     *
     * @param measurement
     * @return
     */
    public Long countByRange(String measurement, Instant start, Instant stop) {
        InfluxBO influxBO = new InfluxBO();
        System.out.println(start);
        Flux bucket = Flux.from("bucket")
                .range(start, stop)
                .filter(Restrictions.and(
                        Restrictions.measurement().equal(measurement)))
                .count();
        influxBO.setFlux(bucket);
        List<FluxTable> tables = query(QueryType.Flux, influxBO);
        return (Long) tables.get(0).getRecords().get(0).getRow().get(4);
    }

    public List<FluxTable> query(QueryType queryType, @Nonnull InfluxBO bo) {
        getQueryApi();

        switch (queryType) {
            case FluxStr -> {
                String flux = bo.getFluxStr();
                if (flux != null) {
                    return queryApi.query(flux);
                }
            }
            case Flux -> {
                Flux flux = bo.getFlux();
                if (flux != null) {
                    return queryApi.query(flux.toString());
                }
            }
            case Query -> {
                Query query = bo.getQuery();
                if (query != null) {
                    return queryApi.query(query);
                }
            }
        }
        client.close();
        // 返回一个空列表作为默认值或根据实际需求进行处理
        return new ArrayList<>();
    }

    public void writePoint(Point point) {
        getWriteApi();
        writeApi.writePoint(point);
        writeApi.close();
        writeApi = null;
    }

    public void writeObject(WritePrecision writePrecision, Object o) {
        getWriteApi();
        writeApi.writeMeasurement(writePrecision, o);
        writeApi.close();
        writeApi = null;
    }

    /**
     * @param start  OffsetDateTime.now().minus(1, ChronoUnit.HOURS)
     * @param stop   OffsetDateTime.now()
     * @param bucket bucket
     * @param org    org
     */
    public boolean del(OffsetDateTime start, OffsetDateTime stop, String bucket, String org) {
        DeleteApi deleteApi = client.getDeleteApi();

        try {
            deleteApi.delete(start, stop, "", bucket, org);

        } catch (InfluxException ie) {
            System.out.println("InfluxException: " + ie);
        }

        client.close();
        return true;
    }
}
