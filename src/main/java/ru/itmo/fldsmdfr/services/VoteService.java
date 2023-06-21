package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cglib.core.Local;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.util.DateUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VoteService {

    private VoteRepository voteRepository;
    private DishRepository dishRepository;

    @Value("${voteStartCron}")
    private String voteStartCron;

    @Value("${voteEndCron}")
    private String voteEndCron;

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
        List<Vote> votes = voteRepository.findByDateAndUser(LocalDate.now(), user);
        return !votes.isEmpty();
    }

    public Dish getWinnerDish(LocalDate date, FoodTime foodTime) {
        List<Pair<Dish, Long>> dishesByCount = voteRepository.findDishesGroupedByVoteCount(date, foodTime);
        dishesByCount = dishesByCount.stream().sorted(Comparator.comparing(Pair::getSecond)).collect(Collectors.toList());
        return dishesByCount.get(dishesByCount.size() - 1).getFirst();
    }

    public List<User> getWinnerUsers(LocalDate date, FoodTime foodTime){

        return null;
    }

    public boolean isVoteActive() {
        Date nextVoteStart = getNextVoteStartDate();
        Date nextVoteEnd = getNextVoteEndDate();
        return !DateUtils.isToday(nextVoteStart) && DateUtils.isToday(nextVoteEnd);
    }

    public Date getNextVoteStartDate() {
        CronSequenceGenerator voteStartCronGenerator = new CronSequenceGenerator(voteStartCron);
        return voteStartCronGenerator.next(new Date());
    }

    public Date getNextVoteEndDate() {
        CronSequenceGenerator voteEndCronGenerator = new CronSequenceGenerator(voteEndCron);
        return voteEndCronGenerator.next(new Date());
    }

    @EventListener(ApplicationStartedEvent.class)
    public void logTest() {
        log.info("is vote active : {}", this.isVoteActive());
    }
}
