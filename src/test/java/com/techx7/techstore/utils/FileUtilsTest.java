package com.techx7.techstore.utils;

import com.techx7.techstore.model.multipart.MultipartFileImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.techx7.techstore.constant.Paths.RESOURCES_IMAGES_DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class FileUtilsTest {

    @Mock
    private MultipartFile multipartFile;

    private static final String TEST_FILE_NAME = "test-file.png";

    private static final Path TEST_FILE_PATH = Path.of(RESOURCES_IMAGES_DIRECTORY + TEST_FILE_NAME);

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(TEST_FILE_PATH.getParent());
        Files.createFile(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(TEST_FILE_PATH);
    }

    @Test
    void testSaveFileLocallyWhenMultipartFileNotEmpty() throws IOException {
        // Arrange
        byte[] expectedFileContent = "test content".getBytes();

        when(multipartFile.getBytes()).thenReturn(expectedFileContent);
        when(multipartFile.getOriginalFilename()).thenReturn(TEST_FILE_NAME);

        // Act
        FileUtils.saveFileLocally(multipartFile);

        // Assert
        byte[] actualFileContent = Files.readAllBytes(TEST_FILE_PATH);

        assertArrayEquals(expectedFileContent, actualFileContent);
    }

    @Test
    void testSaveFileLocallyWhenMultipartFileEmpty() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(multipartFile.getOriginalFilename()).thenReturn(TEST_FILE_NAME);

        // Act
        FileUtils.saveFileLocally(multipartFile);

        // Assert
        long fileSize = multipartFile.getSize();

        assertEquals(0, fileSize);
    }

    @Test
    void testManageImageWhenMultipartFileEmptyAndImageUrlNotEmpty() throws IOException {
        // Arrange
        String imageUrl = "test.png";

        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(multipartFile.isEmpty()).thenReturn(true);

        FileInputStream input = new FileInputStream(RESOURCES_IMAGES_DIRECTORY + imageUrl);

        MultipartFile testImage = new MultipartFileImpl(
                "Existing image", imageUrl, "image/png", input.readAllBytes());

        byte[] expectedImageBytes = testImage.getBytes();

        // Act
        MultipartFile actualMultipartFile = FileUtils.manageImage(multipartFile, imageUrl);

        // Assert
        assertThat(actualMultipartFile).isInstanceOf(MultipartFileImpl.class);

        byte[] actualImageBytes = actualMultipartFile.getBytes();

        assertArrayEquals(expectedImageBytes, actualImageBytes);
    }

    @Test
    void testManageImageWhenMultipartFileEmpty() throws IOException {
        // Arrange
        when(multipartFile.isEmpty()).thenReturn(true);

        // Act
        MultipartFile result = FileUtils.manageImage(multipartFile, "");

        // Assert
        assertEquals(result, multipartFile);
    }

}
