package com.poc.ilovegithub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitRepoDto {
    int id;
    String name;
    Integer size;
    Integer stargazers_count;
    String language;
    String created_at;
    String updated_at;
    String pushed_at;

}
