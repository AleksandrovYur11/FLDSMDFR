package ru.itmo.fldsmdfr.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class DateUtilsTest {
    /**
     * Method under test: {@link DateUtils#isToday(java.util.Date)}
     */
    @Test
    void testIsToday() {
        // Arrange
        java.sql.Date date = mock(java.sql.Date.class);
        when(date.getTime()).thenReturn(10L);

        // Act
        DateUtils.isToday(date);

        // Assert
        verify(date).getTime();
    }
}
