package com.alkemy.ong.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ImageHelper.class)
class ImageHelperTest {

    private ImageHelper imageHelper;

    @BeforeEach
    void setup(){
        imageHelper = new ImageHelper();
    }

    @Test
    @DisplayName("base64ToImage - ok")
    void base64ToImage() throws IOException {
        String encodedImage = "data:image/png;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMSEhUTEBIWFRUWGBcYGRcTFhcYGBg";
        String fileName = "imagenQueHaceFallarAMaven.jpg";

        assertDoesNotThrow(() -> {
            Base64MultipartFile myBase64file = imageHelper.base64ToImage(encodedImage, fileName);
        });

    }
}