package ru.itmo.fldsmdfr.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itmo.fldsmdfr.dto.LockStatusDto;
import ru.itmo.fldsmdfr.models.FldsmdfrLock;
import ru.itmo.fldsmdfr.models.LockStatus;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class LockService {

    private FldsmdfrLocksRepository locksRepository;

    @Autowired
    public LockService(FldsmdfrLocksRepository locksRepository) {
        this.locksRepository = locksRepository;
    }

    public void saveLock(LockStatusDto lockStatusDto) {
        locksRepository.save(
                FldsmdfrLock.builder()
                        .type(lockStatusDto.getLockStatus())
                        .dateTime(Instant.now())
                        .build()
        );
    }

    public Boolean isLocked() {
        Optional<FldsmdfrLock> lockOptional = locksRepository.findTopBy(Sort.by(Sort.Direction.DESC, "dateTime"));
        boolean result = false;
        if (lockOptional.isPresent()) {
            result = lockOptional.get().getType().equals(LockStatus.LOCK);
        }
        log.info("checking if locked: {}", result);
        return result;
    }

}
