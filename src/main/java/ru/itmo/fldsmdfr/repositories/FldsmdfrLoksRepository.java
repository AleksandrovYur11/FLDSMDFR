package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.FldsmdfrLoks;

@Repository
public interface FldsmdfrLoksRepository extends JpaRepository<FldsmdfrLoks, Long> {
}
