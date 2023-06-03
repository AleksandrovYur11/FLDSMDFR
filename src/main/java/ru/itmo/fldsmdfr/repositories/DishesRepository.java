package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.Dishes;

@Repository
public interface DishesRepository extends JpaRepository<Dishes, Long> {
}
