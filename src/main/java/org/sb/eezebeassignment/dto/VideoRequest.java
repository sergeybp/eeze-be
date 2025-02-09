package org.sb.eezebeassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequest {

    @NotBlank
    @Schema(example = "Inception", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(example = "A mind-bending thriller.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String synopsis;

    @NotBlank
    @Schema(example = "Christopher Nolan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String director;

    @NotNull
    @Schema(example = "2010", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer releaseYear;

    @NotBlank
    @Schema(example = "Sci-Fi", requiredMode = Schema.RequiredMode.REQUIRED)
    private String genre;

    @NotNull
    @Schema(example = "148", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer runningTime;
}
