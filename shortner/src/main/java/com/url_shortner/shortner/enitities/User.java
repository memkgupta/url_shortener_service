package com.url_shortner.shortner.enitities;

import jakarta.persistence.*;

@Table(name = "_user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
}
