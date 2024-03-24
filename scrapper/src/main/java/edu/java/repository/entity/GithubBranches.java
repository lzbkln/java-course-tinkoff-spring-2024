package edu.java.repository.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GithubBranches {
    private Long linkId;
    private Set<String> branches;

}
