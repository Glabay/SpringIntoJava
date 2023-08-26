package xyz.glabaystudios.network.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationFormDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long discordUserId;
}
