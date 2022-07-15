package com.poc.ilovegithub.core.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitRepoGroupDto {

    private String login;
    private Integer size;
    private Integer star_count;
}
