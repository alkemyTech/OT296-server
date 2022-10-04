package com.alkemy.ong.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ImageHelper {
    public Base64MultipartFile base64ToImage(String encodedBase64, String fileName) throws IOException {

        String encodeImg = encodedBase64.substring(encodedBase64.indexOf(",") + 1);
        byte[] decodedBytes = Base64.decodeBase64(encodeImg);

        Base64MultipartFile base64MultipartFile = new Base64MultipartFile(decodedBytes, fileName);
        try {
            base64MultipartFile.transferTo(base64MultipartFile.getFile());
        }catch (IOException e) {
            throw new IOException("IOExeption :"+e);
        }
        return base64MultipartFile;
    }
}
