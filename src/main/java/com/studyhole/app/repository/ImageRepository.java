package com.studyhole.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studyhole.app.model.DataTypes.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
