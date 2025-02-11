// AsyncConfig.java
package com.example.webspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Bạn có thể cấu hình Executor tại đây nếu cần
}
