package io.github.zy945.util.monitor;

import com.sun.management.OperatingSystemMXBean;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;


/**
 * @author 伍六七
 * @date 2023/5/31 10:38
 */
public class JMX {
    private static String host = "remote-host"; // 远程主机名或 IP 地址
    private static String port = "9999"; // 远程 JMX RMI 端口号

    // 连接到远程 JMX RMI 代理
    private static JMXServiceURL url;

    private static JMXConnector jmxc;

    // 获取 MBeanServerConnection 对象，并指定要访问的 MBean 名称

    private static ObjectName osObjName;
    private static MBeanServerConnection mbsc;


    public static void main(String[] args) {


        try {
            url = new JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


        try {
            jmxc = JMXConnectorFactory.connect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            mbsc = jmxc.getMBeanServerConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            osObjName = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }

        // 获取 OperatingSystemMXBean 对象
        OperatingSystemMXBean osMbean = null;
        try {
            osMbean = ManagementFactory.newPlatformMXBeanProxy(mbsc, osObjName.toString(), OperatingSystemMXBean.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 调用 OperatingSystemMXBean 的方法，例如获取 CPU 利用率
        double cpuUsage = osMbean.getProcessCpuLoad();
    }
}
