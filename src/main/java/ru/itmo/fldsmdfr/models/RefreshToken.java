package ru.itmo.fldsmdfr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @Column(unique = true)
    @NotBlank(message = "Refresh token could be not empty")
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
