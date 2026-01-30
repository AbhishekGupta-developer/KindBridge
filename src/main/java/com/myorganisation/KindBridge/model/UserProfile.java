package com.myorganisation.KindBridge.model;

import com.myorganisation.KindBridge.enums.Gender;
import com.myorganisation.KindBridge.enums.UserProfileType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_id", nullable = true)
    private DependentRelationship dependent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserProfileType type;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private LocalTime birthTime; // HH:mm format
    private String birthCity;
    private String birthCountry;
    private String email;
    private String phoneNumber;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfilePremium userProfilePremium;
}

