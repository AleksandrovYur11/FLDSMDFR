package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.FldsmdfrLock;

import java.util.Optional;

@Repository
public interface FldsmdfrLocksRepository extends JpaRepository<FldsmdfrLock, Long> {

    Optional<FldsmdfrLock> findTopBy(Sort sort);
}
