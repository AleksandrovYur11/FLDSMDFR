package ru.itmo.fldsmdfr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.fldsmdfr.models.Votes;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Long> {
}
