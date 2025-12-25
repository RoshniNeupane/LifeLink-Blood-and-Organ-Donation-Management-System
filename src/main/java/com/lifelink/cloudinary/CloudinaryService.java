package com.lifelink.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadFile(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto"
        ));
    }

    public void deleteFile(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("publicId is null or empty");
        }
        System.out.println("Deleting Cloudinary file with publicId: " + publicId);
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        System.out.println("Cloudinary delete response: " + result);
    }

}
