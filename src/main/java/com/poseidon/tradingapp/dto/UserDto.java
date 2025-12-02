package com.poseidon.tradingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères dont une majuscule, une minuscule, un chiffre et un caractère spécial"
    )
    private String password;
    @NotBlank(message = "Le fullname est obligatoire")
    @Size(min = 3, max = 30, message = "Le fullname doit contenir entre 3 et 30 caractères")
    private String fullname;
    @NotBlank(message = "Le rôle est obligatoire")
    private String role;

}
