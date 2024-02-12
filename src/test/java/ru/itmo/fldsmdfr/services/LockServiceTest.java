package ru.itmo.fldsmdfr.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.itmo.fldsmdfr.dto.LockStatusDto;
import ru.itmo.fldsmdfr.models.FldsmdfrLock;
import ru.itmo.fldsmdfr.models.LockStatus;
import ru.itmo.fldsmdfr.repositories.FldsmdfrLocksRepository;

@ContextConfiguration(classes = {LockService.class})
@ExtendWith(SpringExtension.class)
class LockServiceTest {
    @MockBean
    private FldsmdfrLocksRepository fldsmdfrLocksRepository;

    @Autowired
    private LockService lockService;

    /**
     * Method under test: {@link LockService#saveLock(LockStatusDto)}
     */
    @Test
    void testSaveLock() {
        // Arrange
        FldsmdfrLock fldsmdfrLock = new FldsmdfrLock();
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        when(fldsmdfrLocksRepository.save(Mockito.<FldsmdfrLock>any())).thenReturn(fldsmdfrLock);

        // Act
        lockService.saveLock(new LockStatusDto(LockStatus.LOCK));

        // Assert
        verify(fldsmdfrLocksRepository).save(Mockito.<FldsmdfrLock>any());
    }

    /**
     * Method under test: {@link LockService#isLocked()}
     */
    @Test
    void testIsLocked() {
        // Arrange
        FldsmdfrLock fldsmdfrLock = new FldsmdfrLock();
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        when(fldsmdfrLocksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);

        // Act
        Boolean actualIsLockedResult = lockService.isLocked();

        // Assert
        verify(fldsmdfrLocksRepository).findTopBy(Mockito.<Sort>any());
        assertTrue(actualIsLockedResult);
    }

    /**
     * Method under test: {@link LockService#isLocked()}
     */
    @Test
    void testIsLocked2() {
        // Arrange
        FldsmdfrLock fldsmdfrLock = mock(FldsmdfrLock.class);
        when(fldsmdfrLock.getType()).thenReturn(LockStatus.LOCK);
        doNothing().when(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        doNothing().when(fldsmdfrLock).setId(Mockito.<Long>any());
        doNothing().when(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        when(fldsmdfrLocksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);

        // Act
        Boolean actualIsLockedResult = lockService.isLocked();

        // Assert
        verify(fldsmdfrLock).getType();
        verify(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        verify(fldsmdfrLock).setId(Mockito.<Long>any());
        verify(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        verify(fldsmdfrLocksRepository).findTopBy(Mockito.<Sort>any());
        assertTrue(actualIsLockedResult);
    }

    /**
     * Method under test: {@link LockService#isLocked()}
     */
    @Test
    void testIsLocked3() {
        // Arrange
        FldsmdfrLock fldsmdfrLock = mock(FldsmdfrLock.class);
        when(fldsmdfrLock.getType()).thenReturn(LockStatus.UNLOCK);
        doNothing().when(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        doNothing().when(fldsmdfrLock).setId(Mockito.<Long>any());
        doNothing().when(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        fldsmdfrLock.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        fldsmdfrLock.setId(1L);
        fldsmdfrLock.setType(LockStatus.LOCK);
        Optional<FldsmdfrLock> ofResult = Optional.of(fldsmdfrLock);
        when(fldsmdfrLocksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(ofResult);

        // Act
        Boolean actualIsLockedResult = lockService.isLocked();

        // Assert
        verify(fldsmdfrLock).getType();
        verify(fldsmdfrLock).setDateTime(Mockito.<Instant>any());
        verify(fldsmdfrLock).setId(Mockito.<Long>any());
        verify(fldsmdfrLock).setType(Mockito.<LockStatus>any());
        verify(fldsmdfrLocksRepository).findTopBy(Mockito.<Sort>any());
        assertFalse(actualIsLockedResult);
    }

    /**
     * Method under test: {@link LockService#isLocked()}
     */
    @Test
    void testIsLocked4() {
        // Arrange
        Optional<FldsmdfrLock> emptyResult = Optional.empty();
        when(fldsmdfrLocksRepository.findTopBy(Mockito.<Sort>any())).thenReturn(emptyResult);

        // Act
        Boolean actualIsLockedResult = lockService.isLocked();

        // Assert
        verify(fldsmdfrLocksRepository).findTopBy(Mockito.<Sort>any());
        assertFalse(actualIsLockedResult);
    }
}
