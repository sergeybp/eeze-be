package org.sb.eezebeassignment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_engagements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long videoId;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int impressions;

    public VideoEngagement(Long videoId, int views, int impressions) {
        this.videoId = videoId;
        this.views = views;
        this.impressions = impressions;
    }
}
