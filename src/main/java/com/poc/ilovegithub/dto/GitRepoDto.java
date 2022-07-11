package com.poc.ilovegithub.dto;

import com.poc.ilovegithub.core.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

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
