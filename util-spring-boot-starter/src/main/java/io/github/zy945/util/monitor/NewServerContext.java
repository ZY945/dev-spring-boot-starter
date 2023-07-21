package io.github.zy945.util.monitor;


import io.github.zy945.util.jsonUtil.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

/**
 * @author 伍六七
 * @date 2023/5/11 9:31
 * 监控<br>
 * 服务器(cpu(完成)、内存(完成)、jdk(完成))<br>
 * JVM(内存(完成)、)<br>
 * ---------------------------------<br>
 * jdk11 需要加 --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED<br>
 * jdk17 不支持,获取的方法都变了<br>
 * ---------------------------------<br>
 * <a href="https://docs.oracle.com/en/java/javase/11/docs/api/jdk.management/com/sun/management/OperatingSystemMXBean.html">jdk11文档</a> <br>
 * <a href="https://docs.oracle.com/en/java/javase/17/docs/api/jdk.management/com/sun/management/OperatingSystemMXBean.html">jdk17文档</a> <br>
 * ---------------------------------<br>
 * 需要
 * @See com.example.springbootutils.utils.monitor.json.JsonUtil
 *
 * <pre>{@code
 * <dependency>
 *     <groupId>org.json</groupId>
 *     <artifactId>json</artifactId>
 *     <version>20220320</version>
 * </dependency>
 * }
 */
//TODO jdk17版本监控
public final class NewServerContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final long byteToGB = 1024 * 1024 * 1024;
    private static final int byteToMb = 1024 * 1024;

    private Runtime runtime = Runtime.getRuntime();
    private static NewServerContext instance;
    /**
     * 服务器操作系统
     */
    private String osName;
    /**
     * 服务器操作系统版本
     */
    private String osVersion;
    /**
     * 服务器电脑处理器架构
     */
    private String osArch;
    /**
     * jdk版本
     */
    private String jdkVersion;
    /**
     * 逻辑处理器(线程)
     */
    private int cpuNum;

    /**
     * 该进程占用CPU(单位:%)
     */
    private double processCpuLoad;
    /**
     * 系统CPU占用率(单位:%)
     */
    private double systemCpuLoad;
    /**
     * 总物理内存(单位:Bytes)
     */
    private Long totalPhysicalMemorySize;
    /**
     * 空闲物理内存(单位:Bytes)
     */
    private Long freePhysicalMemorySize;
    /**
     * 系统总内存(单位:GB)
     */
    private double totalMemory;
    /**
     * 系统剩余内存(单位:GB)
     */
    private double freeMemory;
    /**
     * 内存占用率(单位:%)
     */
    private double memoryUseRatio;


    //////////////////JVM(Mb)//////////////////
    /**
     * jvm型号
     */
    private String jvmName;
    /**
     * jvm版本
     */
    private String jvmVersion;
    /**
     * jvm供应商
     */
    private String jvmVendor;
    /**
     * JVM的总内存
     */
    private Long totalVMMemorySize;
    /**
     * JVM的空闲内存
     */
    private Long freeVMMemorySize;
    /**
     * JVM的使用内存
     */
    private Long useVMMemorySize;
    /**
     * JVM的最大内存
     */
    private Long maxVMMemorySize;


    //    private String appServerType;
//    private String appServerVersion;
//    private String serverName;
//    private String adminServerIP;
//    private int adminServerPort;
//    private String serverHome;
//    private int startupMode;
//    private String localIP;
//    private String localHostName;
//    private int adminPort;
    private String language;
    private String encoding;


    /**
     * 需要jdk1.8,jdk11报错<br>
     * <pre> {@code
     * String osJson = JSON.toJSONString(operatingSystemMXBean);<br>
     * System.out.println("osJson is " + osJson);<br>
     * JSONObject jsonObject = JSON.parseObject(osJson);}<br>
     * </pre>
     * 可取参数:<br>
     * {"arch":"amd64","availableProcessors":16,"committedVirtualMemorySize":420270080,"freePhysicalMemorySize":5422137344,"freeSwapSpaceSize":3576918016,"name":"Windows 10","objectName":{"canonicalKeyPropertyListString":"type=OperatingSystem","canonicalName":"java.lang:type=OperatingSystem","domain":"java.lang","domainPattern":false,"keyPropertyList":{"type":"OperatingSystem"},"keyPropertyListString":"type=OperatingSystem","pattern":false,"propertyListPattern":false,"propertyPattern":false,"propertyValuePattern":false},"processCpuLoad":0.06104087883239054,"processCpuTime":8156250000,"systemCpuLoad":0.21877929577640842,"systemLoadAverage":-1.0,"totalPhysicalMemorySize":17042837504,"totalSwapSpaceSize":22948417536,"version":"10.0"}
     */
    public void getCPU() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        String jsonStr = null;

        jsonStr = JsonUtil.toJSONString(operatingSystemMXBean);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            systemCpuLoad = threeDecimal(jsonObject.getDouble("systemCpuLoad") * 100);
            processCpuLoad = threeDecimal(jsonObject.getDouble("processCpuLoad") * 100);
            totalPhysicalMemorySize = jsonObject.getLong("totalPhysicalMemorySize");
            freePhysicalMemorySize = jsonObject.getLong("freePhysicalMemorySize");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        totalMemory = threeDecimal(1.0 * totalPhysicalMemorySize / byteToGB);
        freeMemory = threeDecimal(1.0 * freePhysicalMemorySize / byteToGB);
        memoryUseRatio = threeDecimal(1.0 * (totalPhysicalMemorySize - freePhysicalMemorySize) / totalPhysicalMemorySize * 100);
    }

    public void getJVM() {
        freeVMMemorySize = runtime.freeMemory() / byteToMb;
        totalVMMemorySize = runtime.totalMemory() / byteToMb;
        maxVMMemorySize = runtime.maxMemory() / byteToMb;
        useVMMemorySize = (totalVMMemorySize - freeVMMemorySize) / byteToMb;
    }

    public static void main(String[] args) {
        NewServerContext instance = NewServerContext.getInstance();
        instance.start();
    }

    private NewServerContext() {
        //os
        this.osName = System.getProperty("os.name");
        this.osVersion = System.getProperty("os.version");
        this.osArch = System.getProperty("os.arch");
        this.language = System.getProperty("user.language");
        this.encoding = System.getProperty("file.encoding");
        this.cpuNum = Runtime.getRuntime().availableProcessors();

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        String jsonStr = null;
        jsonStr = JsonUtil.toJSONString(operatingSystemMXBean);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            this.systemCpuLoad = jsonObject.getDouble("systemCpuLoad") * 100;
            this.processCpuLoad = jsonObject.getDouble("processCpuLoad") * 100;
            this.totalPhysicalMemorySize = jsonObject.getLong("totalPhysicalMemorySize");
            this.freePhysicalMemorySize = jsonObject.getLong("freePhysicalMemorySize");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.totalMemory = threeDecimal(1.0 * totalPhysicalMemorySize / byteToGB);
        this.freeMemory = threeDecimal(1.0 * freePhysicalMemorySize / byteToGB);
        this.memoryUseRatio = 1.0 * (totalPhysicalMemorySize - freePhysicalMemorySize) / totalPhysicalMemorySize * 100;


        this.jdkVersion = System.getProperty("java.version");
        //jvm
        this.jvmName = System.getProperty("java.vm.name");
        this.jvmVendor = System.getProperty("java.vm.vendor");
        this.jvmVersion = System.getProperty("java.vm.version");
        this.freeVMMemorySize = runtime.freeMemory() / byteToMb;
        this.totalVMMemorySize = runtime.totalMemory() / byteToMb;
        this.maxVMMemorySize = runtime.maxMemory() / byteToMb;
        this.useVMMemorySize = (totalVMMemorySize - freeVMMemorySize) / byteToMb;
    }


    /**
     * 保留两位小数
     *
     * @param doubleValue
     * @return
     */
    public double threeDecimal(double doubleValue) {
        BigDecimal bigDecimal = new BigDecimal(doubleValue).setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static NewServerContext getInstance() {
        if (instance == null) {
            synchronized (ServerContext.class) {
                if (instance == null) {
                    instance = new NewServerContext();
                }
            }
        }
        return instance;
    }

    /**
     * 启动监控,后期可以换成心跳检测等
     */
    public void start() {
        while (true) {
            getCPU();
            getJVM();
            System.out.println(this.toString());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getAllProperty() {
        Properties properties = System.getProperties();
        for (String key : properties.stringPropertyNames()) {
            //输出对应的键和值
            System.out.println(key + "=" + properties.getProperty(key));
        }
    }


    @Override
    public String toString() {
        return "ServerContext{" +
                "osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osArch='" + osArch + '\'' +
                ", jdkVersion='" + jdkVersion + '\'' +
                ", cpuNum=" + cpuNum +
                ", processCpuLoad=" + processCpuLoad +
                ", systemCpuLoad=" + systemCpuLoad +
                ", totalPhysicalMemorySize=" + totalPhysicalMemorySize +
                ", freePhysicalMemorySize=" + freePhysicalMemorySize +
                ", totalMemory=" + totalMemory +
                ", freeMemory=" + freeMemory +
                ", memoryUseRatio=" + memoryUseRatio +
                ", jvmName='" + jvmName + '\'' +
                ", jvmVersion='" + jvmVersion + '\'' +
                ", jvmVendor='" + jvmVendor + '\'' +
                ", totalVMMemorySize=" + totalVMMemorySize +
                ", freeVMMemorySize=" + freeVMMemorySize +
                ", useVMMemorySize=" + useVMMemorySize +
                ", maxVMMemorySize=" + maxVMMemorySize +
                ", language='" + language + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOsArch() {
        return osArch;
    }

    public String getJdkVersion() {
        return jdkVersion;
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public double getProcessCpuLoad() {
        return processCpuLoad;
    }

    public double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public Long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    public Long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    public double getTotalMemory() {
        return totalMemory;
    }

    public double getFreeMemory() {
        return freeMemory;
    }

    public double getMemoryUseRatio() {
        return memoryUseRatio;
    }

    public String getJvmName() {
        return jvmName;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public String getJvmVendor() {
        return jvmVendor;
    }

    public Long getTotalVMMemorySize() {
        return totalVMMemorySize;
    }

    public Long getFreeVMMemorySize() {
        return freeVMMemorySize;
    }

    public Long getUseVMMemorySize() {
        return useVMMemorySize;
    }

    public Long getMaxVMMemorySize() {
        return maxVMMemorySize;
    }

    public String getLanguage() {
        return language;
    }

    public String getEncoding() {
        return encoding;
    }
}
