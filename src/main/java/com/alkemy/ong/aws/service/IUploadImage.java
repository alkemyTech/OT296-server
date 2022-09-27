package com.alkemy.ong.aws.service;

import java.io.InputStream;

public interface IUploadImage {
    String upload(InputStream inputStream, String contentType, String fileName);
}
