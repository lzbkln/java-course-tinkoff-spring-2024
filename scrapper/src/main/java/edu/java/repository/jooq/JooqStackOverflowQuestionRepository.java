package edu.java.repository.jooq;

import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.StackOverflowQuestion;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.tables.StackoverflowQuestion.STACKOVERFLOW_QUESTION;

@RequiredArgsConstructor
public class JooqStackOverflowQuestionRepository implements StackOverflowQuestionRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public void save(StackOverflowQuestion stackOverflowQuestion) {
        dslContext.insertInto(STACKOVERFLOW_QUESTION)
            .values(stackOverflowQuestion.getLinkId(), stackOverflowQuestion.getAnswerCount())
            .execute();
    }

    @Override
    @Transactional
    public StackOverflowQuestion findByLinkId(Long linkId) {
        return dslContext.selectFrom(STACKOVERFLOW_QUESTION)
            .where(STACKOVERFLOW_QUESTION.LINK_ID.eq(linkId))
            .fetchOneInto(StackOverflowQuestion.class);
    }

    @Override
    @Transactional
    public void updateData(StackOverflowQuestion stackOverflowQuestion) {
        dslContext.update(STACKOVERFLOW_QUESTION)
            .set(STACKOVERFLOW_QUESTION.ANSWER_COUNT, (long) stackOverflowQuestion.getAnswerCount())
            .where(STACKOVERFLOW_QUESTION.LINK_ID.eq(stackOverflowQuestion.getLinkId()))
            .execute();
    }

    @Override
    @Transactional
    public void removeByLinkId(Long linkId) {
        dslContext.deleteFrom(STACKOVERFLOW_QUESTION)
            .where(STACKOVERFLOW_QUESTION.LINK_ID.eq(linkId))
            .execute();
    }
}
