package com.url_shortner.shortner.dtos;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class CreateURLRequest {
    private String long_url;
    private String userId;
    private Timestamp created_at;
    private Timestamp expiryTime;

}
