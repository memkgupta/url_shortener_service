package com.url_shortner.shortner.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
public class CreateURLResponse {
 private String id;
 private String url;
 private Timestamp timestamp;
}
