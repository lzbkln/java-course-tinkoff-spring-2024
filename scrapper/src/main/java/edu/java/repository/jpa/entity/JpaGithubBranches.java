package edu.java.repository.jpa.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "github_branches")
@NoArgsConstructor
public class JpaGithubBranches {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "link_id", referencedColumnName = "id")
    private JpaLink linkId;

    @Type(ListArrayType.class)
    @Column(name = "branches", columnDefinition = "VARCHAR(255)[]")
    private Set<String> branches;

    public JpaGithubBranches(JpaLink linkId, Set<String> branches) {
        this.linkId = linkId;
        this.branches = branches;
    }
}
