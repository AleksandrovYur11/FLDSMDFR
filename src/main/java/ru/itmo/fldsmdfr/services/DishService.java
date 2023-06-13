package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.repositories.DishRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    private DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> findDishById(Long id) {
        return dishRepository.findById(id);
    }
}
