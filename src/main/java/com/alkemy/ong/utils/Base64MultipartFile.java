package com.alkemy.ong.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class Base64MultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private final String fileName;

    private final File file;

    public Base64MultipartFile(byte[] fileData, String name) {
        this.fileContent = fileData;
        this.fileName = name;
        String destPath = System.getProperty("java.io.tmpdir");
        file = new File(destPath + fileName);

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(fileContent);
    }
    public File getFile() {
        return this.file;
    }
}
