package edu.java.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StackOverflowQuestion {
    Long linkId;
    int answerCount;
}
