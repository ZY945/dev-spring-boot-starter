package io.github.zy945.influxDB.empty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 伍六七
 * @date 2023/7/2 21:47
 */
public class InfluxVO<M> {
    private Class<M> mClass;
    private List<M> result;

    public InfluxVO(Class<M> mClass) {
        this.mClass = mClass;
        this.result = new ArrayList<>();
    }

    public Class<M> getmClass() {
        return mClass;
    }

    public void setmClass(Class<M> mClass) {
        this.mClass = mClass;
    }

    public List<M> getResult() {
        return result;
    }

    public void setResult(List<M> result) {
        this.result = result;
    }
}
