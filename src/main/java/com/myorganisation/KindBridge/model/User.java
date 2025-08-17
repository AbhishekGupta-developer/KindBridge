package com.myorganisation.KindBridge.model;

import com.myorganisation.KindBridge.enums.AreaType;
import com.myorganisation.KindBridge.enums.RoleType;
import com.myorganisation.KindBridge.enums.SupporterType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean isEmailVerified = false;

    @Column(nullable = false)
    private String password;

    // set active = true after successful password setup
    @Column(nullable = false)
    private boolean active = false;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private SupporterType supporterType;

    @ElementCollection(targetClass = AreaType.class)
    @CollectionTable(name = "user_areas", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private List<AreaType> areas;

    private String firstName;
    private String lastName;

//    @Column(unique = true, nullable = true)
//    private String username;

    @Column(unique = true, nullable = true)
    private String phone;

    private boolean anonymous;

    // set isRegistrationCompleted = true after completion of registration
    @Column(nullable = false)
    private boolean isRegistrationCompleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + (role != null ? role.name() : RoleType.GUEST.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return this.active;
    }

}

