package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.dto.DeliveryFinishedDto;
import ru.itmo.fldsmdfr.dto.DeliveryProgressDto;
import ru.itmo.fldsmdfr.models.*;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final VoteRepository voteRepository;

    private final VoteService voteService;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, VoteRepository voteRepository, VoteService voteService) {
        this.deliveryRepository = deliveryRepository;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public void createDeliveriesForWinners(LocalDate date) {
        Dish breakfastDish = voteService.getWinnerDish(date, FoodTime.BREAKFAST);
        Dish lunchDish = voteService.getWinnerDish(date, FoodTime.LUNCH);
        Dish dinnerDish = voteService.getWinnerDish(date, FoodTime.DINNER);
        List<Vote> breakfastWinVotes = voteRepository.findByDishAndDate(breakfastDish, date);
        List<Vote> lunchWinVotes = voteRepository.findByDishAndDate(lunchDish, date);
        List<Vote> dinnerWinVotes = voteRepository.findByDishAndDate(dinnerDish, date);
        breakfastWinVotes.forEach(vote -> {
            deliveryRepository.save(Delivery.builder()
                    .lastChangeTimestamp(Instant.now())
                    .address(vote.getUser().getAddress())
                    .foodTime(vote.getFoodTime())
                    .date(vote.getDate())
                    .dish(vote.getDish())
                    .status(DeliveryStatus.NEW)
                    .build());
        });
        lunchWinVotes.forEach(vote -> {
            deliveryRepository.save(Delivery.builder()
                    .lastChangeTimestamp(Instant.now())
                    .address(vote.getUser().getAddress())
                    .foodTime(vote.getFoodTime())
                    .date(vote.getDate())
                    .dish(vote.getDish())
                    .status(DeliveryStatus.NEW)
                    .build());
        });
        dinnerWinVotes.forEach(vote -> {
            deliveryRepository.save(Delivery.builder()
                    .lastChangeTimestamp(Instant.now())
                    .address(vote.getUser().getAddress())
                    .foodTime(vote.getFoodTime())
                    .date(vote.getDate())
                    .dish(vote.getDish())
                    .status(DeliveryStatus.NEW)
                    .build());
        });
    }

        public void setInProgress (DeliveryProgressDto deliveryProgressDto) {
            Long deliveryId = deliveryProgressDto.getDeliveryId();
            Optional<Delivery> deliveryOptional = deliveryRepository.findById(deliveryId);
            Delivery delivery = deliveryOptional.orElseThrow(
                    () -> new IllegalArgumentException("not found delivery with id " + deliveryId));
            if (!delivery.getStatus().equals(DeliveryStatus.NEW)) {
//            throw new IllegalStateException(
//                    "Can't set delivery to state IN_PROGRESS. This state is achieved only from NEW.");
            }
            delivery.setStatus(DeliveryStatus.IN_PROGRESS);
            delivery.setLastChangeTimestamp(Instant.now());
            deliveryRepository.save(delivery);
        }

        public void setDelivered (DeliveryFinishedDto deliveryFinishedDto){
            Long deliveryId = deliveryFinishedDto.getDeliveryId();
            Optional<Delivery> deliveryOptional = deliveryRepository.findById(deliveryId);
            Delivery delivery = deliveryOptional.orElseThrow(
                    () -> new IllegalArgumentException("not found delivery with id " + deliveryId));
            if (!delivery.getStatus().equals(DeliveryStatus.IN_PROGRESS)) {
//            throw new IllegalStateException(
//                    "Can't set delivery to state DELIVERED. This state is achieved only from IN_PROGRESS.");
            }
            delivery.setStatus(DeliveryStatus.DELIVERED);
            delivery.setLastChangeTimestamp(Instant.now());
            deliveryRepository.save(delivery);
        }
    }
