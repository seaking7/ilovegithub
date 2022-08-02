package com.poc.ilovegithub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgMembersDto {
    int id;
    String login;
    Integer size;
    String type;
}
