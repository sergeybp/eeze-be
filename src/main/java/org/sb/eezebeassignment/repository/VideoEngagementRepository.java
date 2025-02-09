package org.sb.eezebeassignment.repository;

import org.sb.eezebeassignment.model.VideoEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, Long> {

    Optional<VideoEngagement> findByVideoId(Long videoId);
}
