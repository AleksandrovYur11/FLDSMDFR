package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
