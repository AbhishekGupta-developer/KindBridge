package com.myorganisation.KindBridge.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dependent_relationships")
public class DependentRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String relation;

    @OneToOne(mappedBy = "dependent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserProfile profile;
}

