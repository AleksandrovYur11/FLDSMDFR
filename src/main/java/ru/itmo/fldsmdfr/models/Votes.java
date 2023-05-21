package ru.itmo.fldsmdfr.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Votes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long dish_id;

    //    @Column(name = "user_id")
    private Long userId;

    //    @Column(name = "date")
    private Date date;

    //    @Column(name = "food_time")
    private Enum foodTime;
}
