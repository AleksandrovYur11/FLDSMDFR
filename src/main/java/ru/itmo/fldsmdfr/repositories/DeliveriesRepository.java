package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.Deliveries;

@Repository
public interface DeliveriesRepository extends JpaRepository<Deliveries, Long> {

}
