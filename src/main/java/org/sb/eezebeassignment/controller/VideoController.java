package org.sb.eezebeassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sb.eezebeassignment.dto.VideoRequest;
import org.sb.eezebeassignment.dto.VideoResponse;
import org.sb.eezebeassignment.dto.VideoEngagementResponse;
import org.sb.eezebeassignment.model.Video;
import org.sb.eezebeassignment.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videos")
@Tag(name = "Video API", description = "API for managing videos")
public class VideoController {

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    @Operation(summary = "Publish a new video", description = "Creates a new video without an ID. The ID will be auto-generated.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Video object to be created",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = VideoRequest.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Video created successfully", content = @Content(schema = @Schema(implementation = VideoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request format")
            })
    public ResponseEntity<VideoResponse> publishVideo(@Valid @RequestBody VideoRequest videoRequest) {
        logger.info("Received publish video request");

        Video video = new Video();
        video.setTitle(videoRequest.getTitle());
        video.setSynopsis(videoRequest.getSynopsis());
        video.setDirector(videoRequest.getDirector());
        video.setReleaseYear(videoRequest.getReleaseYear());
        video.setGenre(videoRequest.getGenre());
        video.setRunningTime(videoRequest.getRunningTime());

        Video savedVideo = videoService.publishVideo(video);
        logger.info("New video added with ID {}", savedVideo.getId());
        return ResponseEntity.ok(new VideoResponse(
                savedVideo.getId(),
                savedVideo.getTitle(),
                savedVideo.getDirector(),
                savedVideo.getGenre(),
                savedVideo.getReleaseYear(),
                savedVideo.getRunningTime()
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update video metadata", description = "Updates metadata for an existing video.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated video metadata",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = VideoRequest.class)))
            ,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Video updated successfully", content = @Content(schema = @Schema(implementation = VideoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Video not found")
            })
    public ResponseEntity<VideoResponse> updateVideo(@PathVariable Long id, @Valid @RequestBody VideoRequest videoRequest) {
        logger.info("Received update video request for video with ID {}", id);
        Optional<Video> optionalVideo = videoService.getVideoById(id);

        if (optionalVideo.isPresent()) {
            Video video = optionalVideo.get();
            video.setTitle(videoRequest.getTitle());
            video.setSynopsis(videoRequest.getSynopsis());
            video.setDirector(videoRequest.getDirector());
            video.setReleaseYear(videoRequest.getReleaseYear());
            video.setGenre(videoRequest.getGenre());
            video.setRunningTime(videoRequest.getRunningTime());

            Video updatedVideo = videoService.updateVideo(video);
            logger.info("Video with ID {} updated successfully", id);
            return ResponseEntity.ok(new VideoResponse(
                    updatedVideo.getId(),
                    updatedVideo.getTitle(),
                    updatedVideo.getDirector(),
                    updatedVideo.getGenre(),
                    updatedVideo.getReleaseYear(),
                    updatedVideo.getRunningTime()
            ));
        } else {
            logger.info("Video with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a video", description = "Marks a video as deleted without removing it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Video soft deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Video not found")
            })
    public ResponseEntity<Void> softDeleteVideo(@PathVariable Long id) {
        logger.info("Received delete video request for video with ID {}", id);
        boolean deleted = videoService.softDeleteVideo(id);
        if (deleted) {
            logger.info("Video with ID {} deleted successfully", id);
            return ResponseEntity.ok().build();
        } else {
            logger.info("Video with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search for videos", description = "Searches for videos based on query parameters such as director or genre.",
            responses = {@ApiResponse(responseCode = "200", description = "List of matching videos")})
    public ResponseEntity<List<VideoResponse>> searchVideos(
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String title) {

        logger.info("Received search request for videos");
        List<VideoResponse> videos = videoService.searchVideos(director, genre, title).stream()
                .map(v -> new VideoResponse(
                        v.getId(), v.getTitle(), v.getDirector(), v.getGenre(), v.getReleaseYear(), v.getRunningTime()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a video by ID", description = "Retrieves a video's metadata by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Video found", content = @Content(schema = @Schema(implementation = VideoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Video not found")
            })
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        logger.info("Received request to get video with ID: {}", id);
        Optional<Video> video = videoService.getVideoById(id);
        if (video.isPresent()) {
            videoService.recordImpression(id);
        }
        if (video.isPresent()) {
            logger.info("Successfully retrieved video for ID: {}", id);
        } else {
            logger.info("No video found for ID: {}", id);
        }
        return video.map(v -> ResponseEntity.ok(new VideoResponse(
                v.getId(), v.getTitle(), v.getDirector(), v.getGenre(), v.getReleaseYear(), v.getRunningTime()
        ))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "List all available videos", description = "Retrieves a list of all non-deleted videos.",
            responses = {@ApiResponse(responseCode = "200", description = "List of videos")})
    public ResponseEntity<List<VideoResponse>> listAvailableVideos() {
        logger.info("Received list videos request");
        List<VideoResponse> videos = videoService.listAvailableVideos().stream()
                .map(v -> new VideoResponse(
                        v.getId(), v.getTitle(), v.getDirector(), v.getGenre(), v.getReleaseYear(), v.getRunningTime()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{id}/play")
    @Operation(summary = "Play a video", description = "Returns stream with mock video content.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mock video content"),
                    @ApiResponse(responseCode = "404", description = "Video not found")
            })
    public ResponseEntity<InputStreamResource> playVideo(@PathVariable Long id) {
        logger.info("Received request to play video with ID: {}", id);
        InputStreamResource videoStream = videoService.getVideoContent(id);

        if (videoStream != null) {
            logger.info("Successfully retrieved video stream for ID: {}", id);
            videoService.recordView(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=video-" + id + ".mp4")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(videoStream);
        } else {
            logger.info("Video with ID {} not found!", id);
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}/engagement")
    @Operation(summary = "Retrieve video engagement statistics", description = "Gets the view and impression metrics for a video.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Engagement data retrieved", content = @Content(schema = @Schema(implementation = VideoEngagementResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Video not found")
            })
    public ResponseEntity<VideoEngagementResponse> getVideoEngagement(@PathVariable Long id) {
        logger.info("Received request to get engagement stats for video with ID: {}", id);
        Optional<Video> video = videoService.getVideoById(id);
        if (video.isPresent()) {
            var engagement = videoService.getVideoEngagement(id);
            logger.info("Successfully retrieved engagement for video with ID: {}", id);
            return ResponseEntity.ok(new VideoEngagementResponse(
                    id,
                    engagement.getViews(),
                    engagement.getImpressions()
            ));
        } else {
            logger.info("Video with ID {} not found!", id);
            return ResponseEntity.notFound().build();
        }
    }
}
