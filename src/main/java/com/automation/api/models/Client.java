package com.automation.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String name;
    private String lastName;
    private String country;
    private String city;
    private String email;
    private String phone;
}
