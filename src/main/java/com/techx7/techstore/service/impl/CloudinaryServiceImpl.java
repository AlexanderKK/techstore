package com.techx7.techstore.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.techx7.techstore.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.techx7.techstore.utils.StringUtils.replaceAllWhiteSpacesWithUnderscores;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String entityType, String entityName) throws IOException {
        String pluralEntityType = getPluralEntityType(entityType);

        String folder = replaceAllWhiteSpacesWithUnderscores(pluralEntityType);
        String fileName = replaceAllWhiteSpacesWithUnderscores(entityName.toLowerCase());

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", fileName,
                                "folder", folder
                        )
                ).get("secure_url")
                .toString();
    }

    private static String getPluralEntityType(String entityType) {
        char lastCharacter = entityType.charAt(entityType.length() - 1);

        if(lastCharacter == 'y') {
            entityType = entityType.substring(0, entityType.length() - 1) + "ies";
        } else {
            entityType = entityType + "s";
        }

        return entityType.toLowerCase();
    }

    private void removeFile() {
        try {
            ApiResponse apiResponse = cloudinary.api().deleteResources(
                    List.of("{folder}/{filename}"),
                    ObjectUtils.asMap("type", "upload", "resource_type", "image")
            );

            System.out.println(apiResponse);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
