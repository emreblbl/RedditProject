package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
