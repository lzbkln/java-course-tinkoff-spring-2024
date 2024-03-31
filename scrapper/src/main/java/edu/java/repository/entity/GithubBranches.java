package edu.java.repository.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GithubBranches {
    private Long linkId;
    private List<String> branches;

}
