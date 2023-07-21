package io.github.zy945.influxDB.empty.bo.query;

import com.influxdb.client.domain.Query;
import com.influxdb.query.dsl.Flux;

/**
 * @author 伍六七
 * @date 2023/7/2 21:46
 */
public class InfluxBO {
    private String fluxStr;
    private Flux flux;
    private Query query;

    public String getFluxStr() {
        return fluxStr;
    }

    public void setFluxStr(String fluxStr) {
        this.fluxStr = fluxStr;
    }

    public Flux getFlux() {
        return flux;
    }

    public void setFlux(Flux flux) {
        this.flux = flux;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
