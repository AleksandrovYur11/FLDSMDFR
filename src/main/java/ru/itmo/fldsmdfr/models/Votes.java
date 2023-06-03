package ru.itmo.fldsmdfr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Votes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long dish_id;
    private Long userId;
    private Date date;
    @Enumerated(EnumType.STRING)
    private FoodTime foodTime;
}
