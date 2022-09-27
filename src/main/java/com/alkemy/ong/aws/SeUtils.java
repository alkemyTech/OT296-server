package com.alkemy.ong.aws;

import com.alkemy.ong.aws.UtilImage.IImage;
import com.alkemy.ong.aws.UtilImage.IImageUploader;
import com.alkemy.ong.aws.UtilImage.ImageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SeUtils implements IImageUploader {
    private final AwsConfig awsConfig;

    public String upload(IImage image) {
        AmazonS3 awsClient = awsConfig.init();
        PutObjectRequest putObjectRequest = buildPutObjectRequest(image);
        awsClient.putObject(putObjectRequest);
        return awsClient.getUrl(awsConfig.getBucketName(), putObjectRequest.getKey()).toString();
    }

    private PutObjectRequest buildPutObjectRequest(IImage image) {
        ObjectMetadata objectMetadata = buildObjectMetadata(image);
        return new PutObjectRequest(
                awsConfig.getBucketName(),
                image.getFileName(),
                image.getContent(),
                objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
    }

    private ObjectMetadata buildObjectMetadata(IImage object) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(object.getContentType());
        return objectMetadata;
    }

    @Override
    public ImageService getService() {
        return ImageService.AWS;
    }
}
