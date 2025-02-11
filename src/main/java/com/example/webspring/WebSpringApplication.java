package com.example.webspring;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebSpringApplication {

    public static void main(String[] args) {
        // Load file .env từ thư mục gốc
        Dotenv dotenv = Dotenv.load();
        // Thiết lập các biến môi trường vào System properties (nếu cần)
        // Ví dụ:
        System.setProperty("SENDGRID_API_KEY", dotenv.get("SENDGRID_API_KEY"));
        SpringApplication.run(WebSpringApplication.class, args);

    }

}
