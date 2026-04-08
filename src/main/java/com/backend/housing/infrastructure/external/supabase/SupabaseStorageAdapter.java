package com.backend.housing.infrastructure.external.supabase;

import com.backend.housing.domain.ports.out.external.supabase.ImageStoragePort;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public class SupabaseStorageAdapter implements ImageStoragePort {

    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;

    public SupabaseStorageAdapter(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String uploadImage(String propertyId, MultipartFile file) {
        try {
            String fileName = propertyId + "/" + UUID.randomUUID() + getExtension(file);
            String uploadUrl = supabaseProperties.getUrl()
                    + "/storage/v1/object/"
                    + supabaseProperties.getBucket() + "/"
                    + fileName;

            HttpHeaders headers = buildHeaders(file.getContentType());
            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);

            restTemplate.exchange(uploadUrl, HttpMethod.POST, request, String.class);

            return supabaseProperties.getUrl()
                    + "/storage/v1/object/public/"
                    + supabaseProperties.getBucket() + "/"
                    + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            String path = extractPathFromUrl(imageUrl);
            String deleteUrl = supabaseProperties.getUrl()
                    + "/storage/v1/object/"
                    + supabaseProperties.getBucket() + "/"
                    + path;

            HttpHeaders headers = buildHeaders(null);
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllImages(String propertyId) {
        listImages(propertyId).forEach(this::deleteImage);
    }

    @Override
    public List<String> listImages(String propertyId) {
        try {
            String listUrl = supabaseProperties.getUrl() + "/storage/v1/object/list/" + supabaseProperties.getBucket();

            HttpHeaders headers = buildHeaders(MediaType.APPLICATION_JSON_VALUE);
            String body = "{\"prefix\": \"" + propertyId + "/\", \"limit\": 100}";
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<List> response = restTemplate.exchange(listUrl, HttpMethod.POST, request, List.class);

            if (response.getBody() == null) return List.of();

            return response.getBody().stream()
                    .map(obj -> {
                        java.util.Map<?, ?> map = (java.util.Map<?, ?>) obj;
                        String name = (String) map.get("name");
                        return supabaseProperties.getUrl()
                                + "/storage/v1/object/public/"
                                + supabaseProperties.getBucket() + "/"
                                + propertyId + "/" + name;
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar imágenes: " + e.getMessage(), e);
        }
    }


    private HttpHeaders buildHeaders(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseProperties.getKey());
        headers.set("apikey", supabaseProperties.getKey());
        if (contentType != null) {
            headers.setContentType(MediaType.valueOf(contentType));
        }
        return headers;
    }

    private String getExtension(MultipartFile file) {
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            return original.substring(original.lastIndexOf("."));
        }
        return ".jpg";
    }

    private String extractPathFromUrl(String imageUrl) {
        String marker = "/object/public/" + supabaseProperties.getBucket() + "/";
        int index = imageUrl.indexOf(marker);
        if (index == -1) throw new RuntimeException("URL de imagen inválida: " + imageUrl);
        return imageUrl.substring(index + marker.length());
    }
}