package org.sb.eezebeassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "New Movie")
    private String title;

    @Schema(example = "Jane Doe")
    private String director;

    @Schema(example = "Action")
    private String genre;

    @Schema(example = "2024")
    private int releaseYear;

    @Schema(example = "120")
    private int runningTime;
}