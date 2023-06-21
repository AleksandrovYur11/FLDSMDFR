package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByDate(LocalDate date);

    List<Vote> findByDateAndUser(LocalDate date, User user);

    @Query("SELECT " +
            "    new org.springframework.data.util.Pair(v.dish, COUNT(v)) " +
            "FROM " +
            "    Vote v " +
            "WHERE v.date = :localDate and v.foodTime = :foodTime " +
            "GROUP BY " +
            "    v.dish")
    List<Pair<Dish, Long>> findDishesGroupedByVoteCount(@Param(value = "localDate") LocalDate localDate, @Param("foodTime") FoodTime foodTime);

    List<Vote> findByDishAndDate(Dish dish, LocalDate date);
}
