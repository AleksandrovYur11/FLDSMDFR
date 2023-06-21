package ru.itmo.fldsmdfr.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @ManyToOne
    private Dish dish;

    @ManyToOne
    private User user;

    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private FoodTime foodTime;
}
