package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.repositories.DishRepository;

@ContextConfiguration(classes = {DishService.class})
@ExtendWith(SpringExtension.class)
class DishServiceTest {
    @MockBean
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    /**
     * Method under test: {@link DishService#getAllDishes()}
     */
    @Test
    void testGetAllDishes() {
        // Arrange
        ArrayList<Dish> dishList = new ArrayList<>();
        when(dishRepository.findAll()).thenReturn(dishList);

        // Act
        List<Dish> actualAllDishes = dishService.getAllDishes();

        // Assert
        verify(dishRepository).findAll();
        assertTrue(actualAllDishes.isEmpty());
        assertSame(dishList, actualAllDishes);
    }

    /**
     * Method under test: {@link DishService#findDishById(Long)}
     */
    @Test
    void testFindDishById() {
        // Arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Name");
        Optional<Dish> ofResult = Optional.of(dish);
        when(dishRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        Optional<Dish> actualFindDishByIdResult = dishService.findDishById(1L);

        // Assert
        verify(dishRepository).findById(Mockito.<Long>any());
        assertTrue(actualFindDishByIdResult.isPresent());
        assertSame(ofResult, actualFindDishByIdResult);
    }
}
