package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class VoteService {

    private VoteRepository voteRepository;
    private DishRepository dishRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, DishRepository dishRepository) {
        this.voteRepository = voteRepository;
        this.dishRepository = dishRepository;
    }

    public void saveVote(Map<String, String> formData, UserDetailsImpl userDetails) {
        if(!formData.containsKey("breakfast") || !formData.containsKey("lunch") || !formData.containsKey("dinner")) {
            throw new IllegalArgumentException("All choices must be provided: breakfast, lunch, dinner");
        }
        Long breakfastDishId = Long.parseLong(formData.get("breakfast"));
        Long lunchDishId = Long.parseLong(formData.get("lunch"));
        Long dinnerDishId = Long.parseLong(formData.get("dinner"));
        Dish breakfastDish = dishRepository.findById(breakfastDishId).orElseThrow();
        Dish lunchDish = dishRepository.findById(lunchDishId).orElseThrow();
        Dish dinnerDish = dishRepository.findById(dinnerDishId).orElseThrow();
        saveOneVote(userDetails.getUser(), FoodTime.BREAKFAST, breakfastDish);
        saveOneVote(userDetails.getUser(), FoodTime.LUNCH, lunchDish);
        saveOneVote(userDetails.getUser(), FoodTime.DINNER, dinnerDish);
    }

    private void saveOneVote(User user, FoodTime foodTime, Dish dish) {
        Vote vote = Vote.builder()
                .user(user)
                .foodTime(foodTime)
                .dish(dish)
                .date(LocalDate.now())
                .build();
        voteRepository.save(vote);
    }

    public boolean hasUserVotedToday(User user) {
        List<Vote> votes = voteRepository.findByDate(LocalDate.now());
        return !votes.isEmpty();
    }

    public Dish getWinnerDish(LocalDate date, FoodTime foodTime) {

        return null;
    }

    public List<User> getWinnerUsers(LocalDate date, FoodTime foodTime){

        return null;
    }
}
