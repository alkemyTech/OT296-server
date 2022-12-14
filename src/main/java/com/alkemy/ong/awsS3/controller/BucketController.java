package com.alkemy.ong.awsS3.controller;

import com.alkemy.ong.awsS3.service.AmazonClient;
import com.alkemy.ong.awsS3.vm.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class BucketController {
    private AmazonClient amazonClient;
    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }
    @GetMapping(value = "/download", params = "file")
    ResponseEntity<ByteArrayResource> getImage(@RequestParam String file){
        Asset asset= amazonClient.getObject(file);
        ByteArrayResource resource= new ByteArrayResource(asset.getContent());
        return ResponseEntity
                .ok()
                .header("Content-Type", asset.getContentType())
                .contentLength(asset.getContent().length)
                .body(resource);
    }
}
