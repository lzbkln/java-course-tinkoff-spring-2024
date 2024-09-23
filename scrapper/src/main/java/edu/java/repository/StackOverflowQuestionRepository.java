package edu.java.repository;

import edu.java.repository.entity.StackOverflowQuestion;

public interface StackOverflowQuestionRepository {
    void save(StackOverflowQuestion stackOverflowQuestion);

    StackOverflowQuestion getByLinkId(Long linkId);

    void updateData(StackOverflowQuestion stackOverflowQuestion);

    void removeByLinkId(Long linkId);
}
