package com.alkemy.ong.aws.UtilImage;

import lombok.Getter;

@Getter
public enum ImageService {
    AWS("AWS");

    private final String serviceName;

    ImageService(String serviceName) {
        this.serviceName = serviceName;
    }
}
