package com.re.it211_project.repository;

import com.re.it211_project.model.entity.KycProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<KycProfile,Long> {
    boolean existsByUserId(Long userId);
}
