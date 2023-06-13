package ru.itmo.fldsmdfr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.models.FldsmdfrLock;
import ru.itmo.fldsmdfr.models.LockStatus;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class LockService {

    private FldsmdfrLocksRepository locksRepository;

    @Autowired
    public LockService(FldsmdfrLocksRepository locksRepository) {
        this.locksRepository = locksRepository;
    }

    public void saveLock() {

    }

    public Boolean isLocked() {
        Optional<FldsmdfrLock> lockOptional = locksRepository.findTopByDateTime(Instant.now());
        return lockOptional.orElseThrow().getType().equals(LockStatus.LOCK);
    }
}
