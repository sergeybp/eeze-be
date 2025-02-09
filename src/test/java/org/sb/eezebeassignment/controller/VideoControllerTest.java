package org.sb.eezebeassignment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sb.eezebeassignment.dto.VideoRequest;
import org.sb.eezebeassignment.model.Video;
import org.sb.eezebeassignment.model.VideoEngagement;
import org.sb.eezebeassignment.service.VideoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoControllerTest {

    @InjectMocks
    private VideoController videoController;

    @Mock
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishVideo() {
        VideoRequest request = new VideoRequest();
        request.setTitle("Inception");
        request.setSynopsis("A mind-bending thriller.");
        request.setDirector("Christopher Nolan");
        request.setReleaseYear(2010);
        request.setGenre("Sci-Fi");
        request.setRunningTime(148);

        Video video = new Video();
        video.setId(1L);
        when(videoService.publishVideo(any(Video.class))).thenReturn(video);

        ResponseEntity<?> response = videoController.publishVideo(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetVideoById() {
        Video video = new Video();
        video.setId(1L);
        video.setTitle("Inception");

        when(videoService.getVideoById(1L)).thenReturn(Optional.of(video));

        ResponseEntity<?> response = videoController.getVideoById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetVideoByIdNotFound() {
        when(videoService.getVideoById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = videoController.getVideoById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPlayVideo() {
        Video video = new Video();
        video.setId(1L);

        when(videoService.getVideoContent(1L)).thenReturn(new InputStreamResource(new ByteArrayInputStream("content".getBytes())));
        doNothing().when(videoService).recordView(1L);

        ResponseEntity<?> response = videoController.playVideo(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPlayVideoNotFound() {
        when(videoService.getVideoContent(1L)).thenReturn(null);

        ResponseEntity<?> response = videoController.playVideo(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetVideoEngagement() {
        VideoEngagement engagement = new VideoEngagement();
        engagement.setViews(100);
        engagement.setImpressions(200);

        when(videoService.getVideoById(1L)).thenReturn(Optional.of(new Video()));
        when(videoService.getVideoEngagement(1L)).thenReturn(engagement);

        ResponseEntity<?> response = videoController.getVideoEngagement(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetVideoEngagementNotFound() {
        when(videoService.getVideoById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = videoController.getVideoEngagement(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSoftDeleteVideo() {
        when(videoService.softDeleteVideo(1L)).thenReturn(true);

        ResponseEntity<?> response = videoController.softDeleteVideo(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSoftDeleteVideoNotFound() {
        when(videoService.softDeleteVideo(1L)).thenReturn(false);

        ResponseEntity<?> response = videoController.softDeleteVideo(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateVideoNotFound() {
        when(videoService.getVideoById(999L)).thenReturn(Optional.empty());

        VideoRequest request = new VideoRequest();
        request.setTitle("Updated Title");

        ResponseEntity<?> response = videoController.updateVideo(999L, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSoftDeleteVideoAlreadyDeleted() {
        when(videoService.softDeleteVideo(1L)).thenReturn(false);

        ResponseEntity<?> response = videoController.softDeleteVideo(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testPlayVideoNullVideo() {
        when(videoService.getVideoById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = videoController.playVideo(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
