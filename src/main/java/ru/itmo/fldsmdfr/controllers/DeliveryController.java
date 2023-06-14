package ru.itmo.fldsmdfr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.fldsmdfr.services.DeliveryService;

@Controller
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/delivery/setprogress/{id}")
    public String setInProgress(@PathVariable("id")Long deliveryId) {
        log.info("setting state of delivery with id {} to IN_PROGRESS", deliveryId);
        deliveryService.setInProgress(deliveryId);
        return "redirect:/cabinet";
    }

    @GetMapping("/delivery/setdelivered/{id}")
    public String setDelivered(@PathVariable("id")Long deliveryId) {
        log.info("setting state of delivery with id {} to DELIVERED", deliveryId);
        deliveryService.setDelivered(deliveryId);
        return "redirect:/cabinet";
    }
}
