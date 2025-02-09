package org.sb.eezebeassignment.repository;

import org.sb.eezebeassignment.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByIdAndDeletedFalse(Long id);

    List<Video> findAllByDeletedFalse();

    @Query("SELECT v FROM Video v WHERE " +
            "(:director IS NULL OR v.director = :director) AND " +
            "(:genre IS NULL OR v.genre = :genre) AND " +
            "(:title IS NULL OR v.title LIKE %:title%) AND " +
            "v.deleted = false")
    List<Video> searchByCriteria(String director, String genre, String title);
}
