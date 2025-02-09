package org.sb.eezebeassignment.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.transaction.annotation.Transactional;
import org.sb.eezebeassignment.model.Video;
import org.sb.eezebeassignment.model.VideoEngagement;
import org.sb.eezebeassignment.repository.VideoRepository;
import org.sb.eezebeassignment.repository.VideoEngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoEngagementRepository engagementRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository, VideoEngagementRepository engagementRepository) {
        this.videoRepository = videoRepository;
        this.engagementRepository = engagementRepository;
    }

    public Video publishVideo(Video video) {
        return videoRepository.save(video);
    }

    @Transactional(readOnly = true)
    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findByIdAndDeletedFalse(id);
    }

    public Video updateVideo(Video video) {
        return videoRepository.save(video);
    }

    public boolean softDeleteVideo(Long id) {
        Optional<Video> optionalVideo = videoRepository.findByIdAndDeletedFalse(id);
        if (optionalVideo.isPresent()) {
            Video video = optionalVideo.get();
            video.setDeleted(true);
            videoRepository.save(video);
            return true;
        }
        return false;
    }

    public List<Video> listAvailableVideos() {
        return videoRepository.findAllByDeletedFalse();
    }

    public List<Video> searchVideos(String director, String genre, String title) {
        return videoRepository.searchByCriteria(director, genre, title);
    }

    public void recordView(Long videoId) {
        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElse(new VideoEngagement(videoId, 0, 0));
        engagement.setViews(engagement.getViews() + 1);
        engagementRepository.save(engagement);
    }

    public VideoEngagement getVideoEngagement(Long videoId) {
        return engagementRepository.findByVideoId(videoId)
                .orElse(new VideoEngagement(videoId, 0, 0));
    }

    public void recordImpression(Long videoId) {
        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElse(new VideoEngagement(videoId, 0, 0));
        engagement.setImpressions(engagement.getImpressions() + 1);
        engagementRepository.save(engagement);
    }

    @Transactional(readOnly = true)
    public InputStreamResource getVideoContent(Long videoId) {
        // Mock video content for demonstration
        Optional<Video> optionalVideo = videoRepository.findByIdAndDeletedFalse(videoId);
        if(optionalVideo.isPresent()) {
            String mockContent = "This is a simulated video content for video ID: " + videoId;
            InputStream videoStream = new ByteArrayInputStream(mockContent.getBytes());
            return new InputStreamResource(videoStream);
        } else {
            return null;
        }

    }

}
