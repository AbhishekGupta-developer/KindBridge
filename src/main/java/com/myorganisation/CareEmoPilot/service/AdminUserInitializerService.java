package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.model.User;
import com.myorganisation.CareEmoPilot.model.enums.Role;
import com.myorganisation.CareEmoPilot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializerService {

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setFirstName("Abhishek");
                admin.setLastName("Gupta");
                admin.setUsername("admin");
                admin.setEmail("careemopilot@gmail.com");
                admin.setPhone("8840204441");
                admin.setPassword(passwordEncoder.encode("admin@CEP-25"));
                admin.setRole(Role.ADMIN);
                admin.setAnonymous(true);
                admin.setActive(true);

                userRepository.save(admin);
                System.out.println("Default admin user created!");
            }
        };
    }

}
