package com.myorganisation.KindBridge.model;

import com.myorganisation.KindBridge.enums.BusinessType;
import com.myorganisation.KindBridge.enums.Gender;
import com.myorganisation.KindBridge.enums.PreferredLanguage;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_profiles_premium")
public class UserProfilePremium {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    // PREMIUM
    private String businessName;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    private LocalDate businessLaunchDate;

    // For couple/compatibility
    private String partnerName;
    private LocalDate partnerDob;

    @Enumerated(EnumType.STRING)
    private Gender partnerGender;

    // For alternate names
    @ElementCollection
    @CollectionTable(name = "user_alternate_names")
    private Set<String> alternateNames;

    private PreferredLanguage preferredLanguage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
