package ru.itmo.fldsmdfr.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Dish dish;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private FoodTime foodTime;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private Instant lastChangeTimestamp;

    private String address;

}
