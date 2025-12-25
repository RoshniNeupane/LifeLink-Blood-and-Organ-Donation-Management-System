package com.lifelink.cloudinary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
	UploadedFile findByPublicId(String publicId);

}
