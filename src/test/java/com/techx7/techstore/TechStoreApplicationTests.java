package com.techx7.techstore;

import com.techx7.techstore.service.impl.CloudinaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TechStoreApplicationTests {

    @Mock
    private CloudinaryServiceImpl cloudinaryService;

    @Test
    void contextLoads() throws IOException {
        when(cloudinaryService.seedFile(anyString(), anyString())).thenReturn("image.png");
    }

}
