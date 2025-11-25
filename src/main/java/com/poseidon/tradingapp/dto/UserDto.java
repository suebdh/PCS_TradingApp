package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    @NotBlank(message = "Le username est obligatoire")
    @Size(min = 4, max = 20, message = "Le username doit contenir entre 4 et 20 caractères")
    private String username;
    @NotBlank(message = "Le password est obligatoire")
    @Size(min = 4, message = "Le password doit contenir au moins 4 caractères")
    private String password;
    @NotBlank(message = "Le fullname est obligatoire")
    @Size(min = 3, max = 30, message = "Le fullname doit contenir entre 3 et 30 caractères")
    private String fullname;
    @NotBlank(message = "Le rôle est obligatoire")
    private String role;

}
