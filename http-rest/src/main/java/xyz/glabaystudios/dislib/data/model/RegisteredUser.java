package xyz.glabaystudios.dislib.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RegisteredUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long uid;

    private String createdOn;
    private String updatedOn;

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Long discordUserId;
}
