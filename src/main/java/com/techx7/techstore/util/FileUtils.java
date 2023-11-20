package com.techx7.techstore.util;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.techx7.techstore.constant.Paths.RESOURCES_IMAGES_DIRECTORY;

public class FileUtils {

    public static void saveFileLocally(MultipartFile image) throws IOException {
        byte[] imgBytes = image.getBytes();

        if(imgBytes.length != 0) {
            BufferedOutputStream bufferedWriter = new BufferedOutputStream(
                    new FileOutputStream(RESOURCES_IMAGES_DIRECTORY + image.getOriginalFilename()));

            bufferedWriter.write(imgBytes);
            bufferedWriter.close();
        }
    }

    public static MultipartFile manageImage(MultipartFile image, String imageUrl) throws IOException {
        if(image.isEmpty() && !imageUrl.isEmpty()) {
            FileInputStream input = new FileInputStream(RESOURCES_IMAGES_DIRECTORY + imageUrl);

            MultipartFile newImage = new MockMultipartFile(
                    "Existing image", imageUrl, "image/png", IOUtils.toByteArray(input));

            return newImage;
        }

        return image;
    }

}
