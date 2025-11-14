package com.example.Clops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;

    @NotBlank(message = "Password hash cannot be blank")
    @Size(min = 1, max = 255, message = "Password hash must be between 1 and 255 characters")
    private String passwordHash;

    private Boolean isActive = true;
}

