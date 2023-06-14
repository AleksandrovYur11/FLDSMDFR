package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.Delivery;
import ru.itmo.fldsmdfr.models.DeliveryStatus;
import ru.itmo.fldsmdfr.repositories.DeliveryRepository;
import ru.itmo.fldsmdfr.repositories.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public void createDeliveriesForWinners(LocalDate date) {

    }

    public void setInProgress(Long deliveryId) {
        Optional<Delivery> deliveryOptional = deliveryRepository.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(
                () -> new IllegalArgumentException("not found delivery with id " + deliveryId));
        if(!delivery.getStatus().equals(DeliveryStatus.NEW)) {
            throw new IllegalStateException(
                    "Can't set delivery to state IN_PROGRESS. This state is achieved only from NEW.");
        }
        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);
    }

    public void setDelivered(Long deliveryId) {
        Optional<Delivery> deliveryOptional = deliveryRepository.findById(deliveryId);
        Delivery delivery = deliveryOptional.orElseThrow(
                () -> new IllegalArgumentException("not found delivery with id " + deliveryId));
        if(!delivery.getStatus().equals(DeliveryStatus.IN_PROGRESS)) {
            throw new IllegalStateException(
                    "Can't set delivery to state DELIVERED. This state is achieved only from IN_PROGRESS.");
        }
        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
    }
}
