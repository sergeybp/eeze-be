package org.sb.eezebeassignment.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sb.eezebeassignment.model.Video;
import org.sb.eezebeassignment.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VideoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @BeforeEach
    void setUp() {
        videoRepository.deleteAll(); // Clean database before each test
    }

    @Test
    void testPublishVideo() throws Exception {
        String requestBody = "{" +
                "\"title\": \"Inception\"," +
                "\"synopsis\": \"A mind-bending thriller.\"," +
                "\"director\": \"Christopher Nolan\"," +
                "\"releaseYear\": 2010," +
                "\"genre\": \"Sci-Fi\"," +
                "\"runningTime\": 148," +
                "\"cast\": \"Leonardo DiCaprio, Joseph Gordon-Levitt\"" +
                "}";

        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test
    void testGetVideoById() throws Exception {
        Video video = new Video("Interstellar", "Space exploration.", "Christopher Nolan", List.of("Matthew McConaughey", "Anne Hathaway"), 2014, "Sci-Fi", 169);
        video = videoRepository.save(video);

        mockMvc.perform(get("/videos/" + video.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")));
    }

    @Test
    void testGetVideoByIdNotFound() throws Exception {
        mockMvc.perform(get("/videos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateVideo() throws Exception {
        Video video = new Video("Interstellar", "Space exploration.", "Christopher Nolan", List.of("Matthew McConaughey", "Anne Hathaway"), 2014, "Sci-Fi", 169);
        video = videoRepository.save(video);

        String requestBody = "{" +
                "\"title\": \"Inception\"," +
                "\"synopsis\": \"A mind-bending thriller.\"," +
                "\"director\": \"Christopher Nolan\"," +
                "\"releaseYear\": 2010," +
                "\"genre\": \"Sci-Fi\"," +
                "\"runningTime\": 148," +
                "\"cast\": \"Leonardo DiCaprio, Joseph Gordon-Levitt\"" +
                "}";

        mockMvc.perform(put("/videos/" + video.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        Optional<Video> updatedVideo = videoRepository.findById(video.getId());
        assertEquals(updatedVideo.map(Video::getTitle), Optional.of("Inception"));
    }

    @Test
    void testPlayVideo() throws Exception {
        Video video = new Video("The Dark Knight", "Gotham's hero.", "Christopher Nolan", List.of("Christian Bale"), 2008, "Action", 152);
        video = videoRepository.save(video);

        mockMvc.perform(get("/videos/" + video.getId() + "/play"))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a simulated video content for video ID: " + video.getId()));
    }

    @Test
    void testSoftDeleteVideo() throws Exception {
        Video video = new Video("Memento", "Memory loss mystery.", "Christopher Nolan", List.of("Guy Pearce", "Carrie-Anne Moss"), 2000, "Thriller", 113);
        video = videoRepository.save(video);

        mockMvc.perform(delete("/videos/" + video.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/videos/" + video.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchVideos() throws Exception {
        Video video1 = new Video("Inception", "A thriller", "Christopher Nolan", List.of("Leonardo DiCaprio"), 2010, "Sci-Fi", 148);
        Video video2 = new Video("Dunkirk", "War film", "Not Christopher Nolan", List.of("Tom Hardy"), 2017, "War", 106);
        Video video3 = new Video("The Prestige", "Magicians rivalry", "Christopher Nolan", List.of("Hugh Jackman"), 2006, "Drama", 130);

        videoRepository.save(video1);
        videoRepository.save(video2);
        videoRepository.save(video3);

        mockMvc.perform(get("/videos/search?director=Christopher Nolan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
        mockMvc.perform(get("/videos/search?genre=War"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
        mockMvc.perform(get("/videos/search?title=The Prestige"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testEngagement() throws Exception {
        Video video = new Video("The Dark Knight", "Gotham's hero.", "Christopher Nolan", List.of("Christian Bale"), 2008, "Action", 152);
        video = videoRepository.save(video);

        mockMvc.perform(get("/videos/" + video.getId() + "/play"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/videos/" + video.getId() + "/play"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/videos/" + video.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/videos/" + video.getId() + "/engagement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.views", is(2)))
                .andExpect(jsonPath("$.impressions", is(1)));
    }
}
