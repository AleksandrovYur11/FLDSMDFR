package ru.itmo.fldsmdfr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Deliveries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dishId;
    private Date date;
    @Enumerated(EnumType.STRING)
    private FoodTime foodTime;
    @Enumerated(EnumType.STRING)
    private StatusDelivery status;

}
