package com.url_shortner.shortner.enitities;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
@Entity
@Data

public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String url;
    @Column(unique = true)
    private String shortCode;
    private Timestamp created_at;
    private Timestamp expired_at;
}
