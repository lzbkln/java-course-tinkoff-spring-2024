package edu.java.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "stackoverflow_question")
@NoArgsConstructor
public class JpaStackOverflowQuestion {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "link_id", referencedColumnName = "id")
    private JpaLink linkId;

    @Column(name = "answer_count")
    int answerCount;

    public JpaStackOverflowQuestion(JpaLink linkId, int answerCount) {
        this.linkId = linkId;
        this.answerCount = answerCount;
    }
}
