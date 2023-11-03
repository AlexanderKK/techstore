package com.techx7.techstore.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.techx7.techstore.constant.FilePaths.RESOURCES_IMAGES_DIRECTORY;

public class FileUtils {

    public static void saveImageLocally(MultipartFile image) throws IOException {
        byte[] imgBytes = image.getBytes();

        if(imgBytes.length != 0) {
//            String imgDir = Objects.requireNonNull(
//                    getClass().getResource("/static/images/")).toURI().getPath();

            BufferedOutputStream bufferedWriter = new BufferedOutputStream(
                    new FileOutputStream(RESOURCES_IMAGES_DIRECTORY + image.getOriginalFilename()));

            bufferedWriter.write(imgBytes);
            bufferedWriter.close();
        }
    }

}
