/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.tables.pojos;


import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class GithubBranches implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long linkId;
    private String[] branches;

    public GithubBranches() {}

    public GithubBranches(GithubBranches value) {
        this.linkId = value.linkId;
        this.branches = value.branches;
    }

    @ConstructorProperties({ "linkId", "branches" })
    public GithubBranches(
        @Nullable Long linkId,
        @NotNull String[] branches
    ) {
        this.linkId = linkId;
        this.branches = branches;
    }

    /**
     * Getter for <code>GITHUB_BRANCHES.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>GITHUB_BRANCHES.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>GITHUB_BRANCHES.BRANCHES</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public String[] getBranches() {
        return this.branches;
    }

    /**
     * Setter for <code>GITHUB_BRANCHES.BRANCHES</code>.
     */
    public void setBranches(@NotNull String[] branches) {
        this.branches = branches;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GithubBranches other = (GithubBranches) obj;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        if (this.branches == null) {
            if (other.branches != null)
                return false;
        }
        else if (!Arrays.deepEquals(this.branches, other.branches))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.branches == null) ? 0 : Arrays.deepHashCode(this.branches));
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GithubBranches (");

        sb.append(linkId);
        sb.append(", ").append(Arrays.deepToString(branches));

        sb.append(")");
        return sb.toString();
    }
}
