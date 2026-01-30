package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.enums.Gender;
import com.myorganisation.KindBridge.enums.UserProfileType;
import com.myorganisation.KindBridge.enums.UserRoleType;
import com.myorganisation.KindBridge.model.User;
import com.myorganisation.KindBridge.model.UserMetaData;
import com.myorganisation.KindBridge.model.UserProfile;
import com.myorganisation.KindBridge.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class AdminUserInitializerService {

    private Logger logger = LoggerFactory.getLogger(AdminUserInitializerService.class);

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepository.findByEmail("kindbridgepilot@gmail.com").isEmpty()) {
                User admin = new User();

                admin.setEmail("kindbridgepilot@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin@CEP26"));

                admin.setActive(true);
                admin.setRole(UserRoleType.ADMIN);

                UserProfile userProfile = new UserProfile();
                userProfile.setUser(admin);
                userProfile.setDependent(null);
                userProfile.setType(UserProfileType.INDEPENDENT);
                userProfile.setFirstName("Abhishek");
                userProfile.setMiddleName(null);
                userProfile.setLastName("Gupta");
                userProfile.setDateOfBirth(LocalDate.of(2001, 12, 31));
                userProfile.setGender(Gender.MALE);
                userProfile.setBirthTime(LocalTime.of(12, 00));
                userProfile.setBirthCity("Ballia");
                userProfile.setBirthCountry("INDIA");
                userProfile.setEmail("kindbridgepilot@gmail.com");
                userProfile.setPhoneNumber("8840204441");
                userProfile.setUserProfilePremium(null);

                admin.setProfile(userProfile);
                admin.setDependents(null);

                UserMetaData userMetaData = new UserMetaData();
                userMetaData.setUser(admin);
                userMetaData.setEmailVerified(true);
                userMetaData.setRegistrationCompleted(true);
                userMetaData.setMeta("Admin user. Testing phase.");

                admin.setMetaData(userMetaData);

                userRepository.save(admin);
                logger.info("Default admin user created!");
            } else  {
                logger.info("Admin user already exists!");
            }
        };
    }

}
