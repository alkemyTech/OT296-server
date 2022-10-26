package com.alkemy.ong.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = Base64MultipartFile.class)
class Base64MultipartFileTest {

    @MockBean
    private byte[] fileContent;
    @MockBean
    private String fileName;
    @MockBean
    private File file;

    private Base64MultipartFile base64MultipartFile;

    @BeforeEach
    void setup(){
        fileContent = new byte[0];
        file = new File("unstringpath");
        base64MultipartFile = new Base64MultipartFile(fileContent, fileName);
    }

    @Test
    @DisplayName("get name")
    void getName(){
        String name = base64MultipartFile.getName();
    }

    @Test
    @DisplayName("get file name")
    void getOriginalFilename(){
        String name = base64MultipartFile.getOriginalFilename();
    }

    @Test
    @DisplayName("getContentType")
    void getContentType() {
        String contentType = base64MultipartFile.getContentType();
    }

    @Test
    @DisplayName("isEmpty")
    void isEmpty() {
        boolean isEmpty = base64MultipartFile.isEmpty();
    }

    @Test
    @DisplayName("getSize")
    void getSize() {
        long size = base64MultipartFile.getSize();

    }

    @Test
    @DisplayName("getBytes")
    void getBytes() throws IOException {
        byte[] bytes = base64MultipartFile.getBytes();
    }

    @Test
    @DisplayName("getInputStream")
    void getInputStream() throws IOException {
        ByteArrayInputStream inputStream = (ByteArrayInputStream) base64MultipartFile.getInputStream();
    }

    @Test
    @DisplayName("transferTo")
    void transferTo() throws IOException, IllegalStateException {
        base64MultipartFile.transferTo(file);
    }

    @Test
    @DisplayName("getFile")
    void getFile() {
        File file1 = base64MultipartFile.getFile();
    }
}