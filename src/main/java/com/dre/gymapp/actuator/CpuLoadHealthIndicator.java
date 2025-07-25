package com.dre.gymapp.actuator;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class CpuLoadHealthIndicator implements HealthIndicator {

    private final double MAX_CPU_LOAD = 0.8;

    @Override
    public Health health() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osBean.getProcessCpuLoad(); // Returns 0.0-1.0

        // If CPU load is not available (returns -1)
        if (cpuLoad < 0) {
            return Health.unknown()
                    .withDetail("message", "CPU load metric not available")
                    .build();
        }


        if (cpuLoad > MAX_CPU_LOAD) {
            return Health.down()
                    .withDetail("cpuLoad", String.format("%.2f%%", cpuLoad * 100))
                    .withDetail("threshold", String.format("%.2f%%", MAX_CPU_LOAD * 100))
                    .build();
        }

        return Health.up()
                .withDetail("cpuLoad", String.format("%.2f%%", cpuLoad * 100))
                .withDetail("threshold", String.format("%.2f%%", MAX_CPU_LOAD * 100))
                .build();
    }
}
