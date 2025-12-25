package com.lifelink.cloudinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @PostMapping("/uploadToCloudinary")
    public String handleMultipleUploads(@RequestParam("files") MultipartFile[] files, Model model) {
        for (MultipartFile file : files) {
            try {
                Map uploadResult = cloudinaryService.uploadFile(file);

                String url = uploadResult.get("secure_url").toString();
                String type = uploadResult.get("resource_type").toString();
                String publicId = uploadResult.get("public_id").toString();

                UploadedFile uploadedFile = new UploadedFile();
                uploadedFile.setUrl(url);
                uploadedFile.setType(type);
                uploadedFile.setPublicId(publicId);
                uploadedFile.setUploadedAt(LocalDateTime.now());

                uploadedFileRepository.save(uploadedFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<UploadedFile> allFiles = uploadedFileRepository.findAll();
        model.addAttribute("files", allFiles);
        model.addAttribute("message", "All uploaded files shown below.");
        return "uploadResult";
    }



    @PostMapping("/editFile")
    public String editFile(@RequestParam("publicId") String publicId,
                           @RequestParam("newFile") MultipartFile newFile,
                           Model model) {
        try {
            // 1. Find the UploadedFile entity by publicId (or add a method in repo for this)
            UploadedFile existingFile = uploadedFileRepository.findByPublicId(publicId);
            if (existingFile == null) {
                model.addAttribute("message", "Original file not found.");
                return "uploadResult";
            }

            // 2. Delete old file from Cloudinary
            cloudinaryService.deleteFile(publicId);

            // 3. Upload new file
            Map uploadResult = cloudinaryService.uploadFile(newFile);
            String newUrl = uploadResult.get("secure_url").toString();
            String newType = uploadResult.get("resource_type").toString();
            String newPublicId = uploadResult.get("public_id").toString();

            // 4. Update DB record with new values
            existingFile.setUrl(newUrl);
            existingFile.setType(newType);
            existingFile.setPublicId(newPublicId);
            existingFile.setUploadedAt(LocalDateTime.now());
            uploadedFileRepository.save(existingFile);

            model.addAttribute("message", "File updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to update file.");
        }

        // Reload all files
        List<UploadedFile> allFiles = uploadedFileRepository.findAll();
        model.addAttribute("files", allFiles);
        return "uploadResult";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam("id") Long id, Model model) {
        try {
            UploadedFile file = uploadedFileRepository.findById(id).orElse(null);
            if (file != null) {
                System.out.println("Deleting file with id: " + id + " and publicId: " + file.getPublicId());
                cloudinaryService.deleteFile(file.getPublicId());
                uploadedFileRepository.deleteById(id);
                model.addAttribute("message", "File deleted successfully!");
            } else {
                System.out.println("File not found with id: " + id);
                model.addAttribute("message", "File not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to delete file.");
        }

        List<UploadedFile> allFiles = uploadedFileRepository.findAll();
        model.addAttribute("files", allFiles);
        return "uploadResult";
    }

}
