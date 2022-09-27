package com.alkemy.ong.aws.UtilImage;

public interface IImageUploader {
    String upload(IImage image);
    ImageService getService();
}
