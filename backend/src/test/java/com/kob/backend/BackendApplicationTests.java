package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("ll"));
        System.out.println(passwordEncoder.encode("test"));
        System.out.println(passwordEncoder.encode("hapi"));
//        System.out.println(passwordEncoder.matches("test","$2a$10$CG9hYPYyw7HUtpVElBCLx.gyIRvTjutY8NiizkWC1hEL6TthGMHCm"));
    }

}
