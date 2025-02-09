package org.sb.eezebeassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sb.eezebeassignment.model.Video;
import org.sb.eezebeassignment.model.VideoEngagement;
import org.sb.eezebeassignment.repository.VideoEngagementRepository;
import org.sb.eezebeassignment.repository.VideoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoServiceTest {

    @InjectMocks
    private VideoService videoService;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoEngagementRepository engagementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishVideo() {
        Video video = new Video();
        video.setTitle("Inception");

        when(videoRepository.save(any(Video.class))).thenReturn(video);

        Video savedVideo = videoService.publishVideo(video);
        assertNotNull(savedVideo);
        assertEquals("Inception", savedVideo.getTitle());
    }

    @Test
    void testGetVideoById() {
        Video video = new Video();
        video.setId(1L);

        when(videoRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(video));

        Optional<Video> foundVideo = videoService.getVideoById(1L);
        assertTrue(foundVideo.isPresent());
        assertEquals(1L, foundVideo.get().getId());
    }

    @Test
    void testGetVideoByIdNotFound() {
        when(videoRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        Optional<Video> foundVideo = videoService.getVideoById(1L);
        assertFalse(foundVideo.isPresent());
    }

    @Test
    void testUpdateVideo() {
        Video video = new Video();
        video.setTitle("Inception Updated");

        when(videoRepository.save(any(Video.class))).thenReturn(video);

        Video updatedVideo = videoService.updateVideo(video);
        assertNotNull(updatedVideo);
        assertEquals("Inception Updated", updatedVideo.getTitle());
    }

    @Test
    void testSoftDeleteVideo() {
        Video video = new Video();
        video.setId(1L);

        when(videoRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(video));
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        boolean result = videoService.softDeleteVideo(1L);
        assertTrue(result);
        assertTrue(video.isDeleted());
    }

    @Test
    void testSoftDeleteVideoNotFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = videoService.softDeleteVideo(1L);
        assertFalse(result);
    }

    @Test
    void testRecordView() {
        VideoEngagement engagement = new VideoEngagement();
        engagement.setViews(5);

        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));
        when(engagementRepository.save(any(VideoEngagement.class))).thenReturn(engagement);

        videoService.recordView(1L);
        assertEquals(6, engagement.getViews());
    }

    @Test
    void testRecordImpression() {
        VideoEngagement engagement = new VideoEngagement();
        engagement.setImpressions(10);

        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));
        when(engagementRepository.save(any(VideoEngagement.class))).thenReturn(engagement);

        videoService.recordImpression(1L);
        assertEquals(11, engagement.getImpressions());
    }

    @Test
    void testGetVideoEngagement() {
        VideoEngagement engagement = new VideoEngagement();
        engagement.setViews(100);
        engagement.setImpressions(200);

        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));

        VideoEngagement result = videoService.getVideoEngagement(1L);
        assertNotNull(result);
        assertEquals(100, result.getViews());
        assertEquals(200, result.getImpressions());
    }

    @Test
    void testGetVideoEngagementNotFound() {
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.empty());

        VideoEngagement result = videoService.getVideoEngagement(1L);
        assertNotNull(result);
        assertEquals(0, result.getViews());
        assertEquals(0, result.getImpressions());
    }
}
