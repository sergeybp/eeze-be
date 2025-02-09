package org.sb.eezebeassignment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String synopsis;

    @Column(nullable = false)
    private String director;

    @ElementCollection
    @CollectionTable(name = "video_cast", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "cast_member")
    private List<String> cast;

    @Column(nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int runningTime;

    @Column(nullable = false)
    private boolean deleted = false;


    public Video(String title, String synopsis, String director, List<String> cast, int releaseYear, String genre, int runningTime) {
        this.title = title;
        this.synopsis = synopsis;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.runningTime = runningTime;
        this.cast = cast;
    }
}
