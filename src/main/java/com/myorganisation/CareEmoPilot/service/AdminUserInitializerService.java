package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.enums.RoleType;
import com.myorganisation.CareEmoPilot.model.User;
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
            if(userRepository.findByEmail("careemopilot@gmail.com").isEmpty()) {
                User admin = new User();

                admin.setEmail("careemopilot@gmail.com");
                admin.setEmailVerified(true);
                admin.setPassword(passwordEncoder.encode("admin@CEP-25"));
                admin.setActive(true);
                admin.setRole(RoleType.ADMIN);
                admin.setSupporterType(null);
                admin.setAreas(null);
                admin.setFirstName("Abhishek");
                admin.setLastName("Gupta");
//                admin.setUsername("admin");
                admin.setPhone("8840204441");
                admin.setAnonymous(true);
                admin.setRegistrationCompleted(true);

                userRepository.save(admin);
                System.out.println("Default admin user created!");
            }
        };
    }

}
