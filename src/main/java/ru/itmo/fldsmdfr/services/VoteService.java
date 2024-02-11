package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cglib.core.Local;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.dto.UserVoteDto;
import ru.itmo.fldsmdfr.models.Dish;
import ru.itmo.fldsmdfr.models.FoodTime;
import ru.itmo.fldsmdfr.models.User;
import ru.itmo.fldsmdfr.models.Vote;
import ru.itmo.fldsmdfr.repositories.DishRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;
import ru.itmo.fldsmdfr.security.UserDetailsImpl;
import ru.itmo.fldsmdfr.util.DateUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VoteService {

    private VoteRepository voteRepository;
    private DishRepository dishRepository;

    private static boolean isVoteActive = true;

    @Value("${voteStartCron}")
    private String voteStartCron;

    @Value("${voteEndCron}")
    private String voteEndCron;

    @Autowired
    public VoteService(VoteRepository voteRepository, DishRepository dishRepository) {
        this.voteRepository = voteRepository;
        this.dishRepository = dishRepository;
    }

    public void saveVote(UserVoteDto userVoteDto) {
        Dish breakfastDish = dishRepository.findById(userVoteDto.getBreakfastDishId()).orElseThrow();
        Dish lunchDish = dishRepository.findById(userVoteDto.getLunchDishId()).orElseThrow();
        Dish dinnerDish = dishRepository.findById(userVoteDto.getDinnerDishId()).orElseThrow();
        saveOneVote(userVoteDto.getUser(), FoodTime.BREAKFAST, breakfastDish);
        saveOneVote(userVoteDto.getUser(), FoodTime.LUNCH, lunchDish);
        saveOneVote(userVoteDto.getUser(), FoodTime.DINNER, dinnerDish);
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
//        Date nextVoteStart = getNextVoteStartDate();
//        Date nextVoteEnd = getNextVoteEndDate();
//        return !DateUtils.isToday(nextVoteStart) && DateUtils.isToday(nextVoteEnd);

        return isVoteActive;
    }

    public Date getNextVoteStartDate() {
        //TODO replace deprecated CronSequenceGenerator with CronExpression as below. Should test, solution below is not tested.
//        CronExpression cronExpression = CronExpression.parse(voteStartCron);
//        return Date.from(Optional.ofNullable(cronExpression.next(Instant.now())).orElseThrow());
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

    public static void setIsVoteActive(boolean isVoteActive) {
        VoteService.isVoteActive = isVoteActive;
    }
}
