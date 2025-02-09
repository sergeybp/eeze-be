package org.sb.eezebeassignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoEngagementResponse {

    @Schema(example = "1", description = "Unique identifier for the video")
    private Long videoId;

    @Schema(example = "1500", description = "Total number of views")
    private int views;

    @Schema(example = "3000", description = "Total number of impressions")
    private int impressions;
}
