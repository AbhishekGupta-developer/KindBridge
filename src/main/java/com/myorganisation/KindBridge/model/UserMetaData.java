package com.myorganisation.KindBridge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserMetaData {

    @Id
    private Long id;

    @OneToOne(mappedBy = "metaData")
    private User user;

    private String meta;
}
