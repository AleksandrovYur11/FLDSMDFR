package ru.itmo.fldsmdfr.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Deliveries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
    private Long id;

    //    @Column(name = "dish_id")
    private Long dishId;

    //    @Column(name = "date")
    private Date date;

    //    @Column(name = "food_time")
    private FoodTime foodTime;

    private Enum status;

}
