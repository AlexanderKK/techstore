package com.techx7.techstore.service.schedulers;

import com.techx7.techstore.service.UserActivationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivationLinkCleanupSchedulerTest {

    @Mock
    private UserActivationService userActivationService;

    @InjectMocks
    private ActivationLinkCleanupScheduler activationLinkCleanupScheduler;

    @Test
    public void testCleanUp() {
        // Arrange
        int expectedMinutesLifetime = 15;

        // Act
        activationLinkCleanupScheduler.cleanUp();

        // Assert
        verify(userActivationService, times(1)).cleanUpObsoleteActivationLinks(expectedMinutesLifetime);
    }

    @Test
    public void testCleanUpThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Cleanup failed")).when(userActivationService).cleanUpObsoleteActivationLinks(anyInt());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> activationLinkCleanupScheduler.cleanUp());
    }

}
