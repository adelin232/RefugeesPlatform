package com.project.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class News {
    private String source_name;
    private String author;
    private String title;
    private String description;
    private String url;
    private String image_url;
    private String publish_time;
}
