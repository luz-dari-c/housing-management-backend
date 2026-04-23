package com.backend.housing.domain.ports.out.external.supabase;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStoragePort {
    String uploadImage(String propertyId, MultipartFile file);

    void deleteImage(String imageUrl);

    void deleteAllImages(String propertyId);

    List<String> listImages(String propertyId);
}
