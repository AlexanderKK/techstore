package com.techx7.techstore.utils;

import com.techx7.techstore.model.multipart.MultipartFileImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

import static com.techx7.techstore.constant.Paths.RESOURCES_IMAGES_DIRECTORY;
import static com.techx7.techstore.utils.StringUtils.replaceAllWhiteSpacesWithUnderscores;

public class FileUtils {

    public static void uploadFileLocally(MultipartFile multipartFile, String entityType, String entityName) throws IOException {
        String filePath = getFilePath(multipartFile, entityType, entityName);

        File file = new File(RESOURCES_IMAGES_DIRECTORY + filePath);

        file.getParentFile().mkdirs();
        file.createNewFile();

        byte[] imgBytes = multipartFile.getBytes();

        if(imgBytes.length != 0) {
            BufferedOutputStream bufferedWriter = new BufferedOutputStream(new FileOutputStream(file));

            bufferedWriter.write(imgBytes);
            bufferedWriter.close();
        }
    }

    private static String getFilePath(MultipartFile multipartFile, String entityType, String entityName) {
        String fileExtension = getFileExtension(
                Objects.requireNonNull(multipartFile.getOriginalFilename())
        );

        if(entityType.equals("user")) {
            return String.format("%s/%s.%s",
                    replaceAllWhiteSpacesWithUnderscores(entityType), UUID.randomUUID(), fileExtension);
        }

        return String.format("%s/%s.%s",
                entityType, replaceAllWhiteSpacesWithUnderscores(entityName), fileExtension);
    }

    private static String getFileExtension(String fileName) {
        String[] fileNameArr = fileName.split("\\.");

        return fileNameArr[fileNameArr.length - 1];
    }

    public static MultipartFile manageImage(MultipartFile image, String imageUrl) throws IOException {
        if(image == null || image.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {
            MultipartFile newImage = new MultipartFileImpl(
                    "Virtual image", imageUrl, "image/png", new byte[] {0});

            return newImage;
        }

        return image;
    }

}
