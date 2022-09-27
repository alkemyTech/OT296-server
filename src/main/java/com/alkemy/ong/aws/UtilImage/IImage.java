package com.alkemy.ong.aws.UtilImage;

import java.io.InputStream;

public interface IImage {

    InputStream getContent();

    String getContentType();

    String getFileName();

    InputStream getInputStream();
}
