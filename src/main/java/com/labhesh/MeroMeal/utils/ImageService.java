package com.labhesh.MeroMeal.utils;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.models.Files;
import com.labhesh.MeroMeal.repos.FileRepo;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileRepo fileRepo;

    public Files saveImage(MultipartFile imageFile) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString();

        Files file = Files.builder()
                .filename(uniqueFileName)
                .contentType(imageFile.getContentType())
                .content(imageFile.getBytes())
                .build();
        fileRepo.save(file);
        return file;
    }

    // To view an image
    public byte[] getImage(String imageName) throws BadRequestException {
        Files file = fileRepo.findByFilename(imageName).orElseThrow(() -> new BadRequestException("File not found"));
        return file.getContent();
    }

    public MediaType getImageContentType(String contentType){
        if (contentType.equals("image/png")) {
            return MediaType.IMAGE_PNG;
        } else if (contentType.equals("image/jpg") || contentType.equals("image/jpeg")) {
            return MediaType.IMAGE_JPEG;
        }else{
            return MediaType.IMAGE_GIF;
        }

    }
}