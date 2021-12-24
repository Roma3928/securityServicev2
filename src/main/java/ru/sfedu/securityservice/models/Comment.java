package ru.sfedu.securityservice.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.securityservice.utils.SecurityPaperConverter;
import ru.sfedu.securityservice.utils.UserConverter;

import java.io.Serializable;
import java.util.Objects;


/**
 * The type Comment.
 */
@Root(name = "comment")
public class Comment implements Serializable {
    @Attribute
    @CsvBindByName
    private long id;
    @Element
    @CsvBindByName
    private long userId;
    @Element
    @CsvCustomBindByName(converter = SecurityPaperConverter.class)
    private SecurityPaper paper;
    @Element
    @CsvBindByName
    private String content;

    /**
     * Instantiates a new Comment.
     */
    public Comment() {
    }

    /**
     * Instantiates a new Comment.
     *
     * @param id      the id
     * @param userId  the user id
     * @param paper   the paper
     * @param content the content
     */
    public Comment(long id, long userId, SecurityPaper paper, String content) {
        this.id = id;
        this.userId = userId;
        this.paper = paper;
        this.content = content;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Gets paper.
     *
     * @return the paper
     */
    public SecurityPaper getPaper() {
        return paper;
    }

    /**
     * Sets paper.
     *
     * @param paper the paper
     */
    public void setPaper(SecurityPaper paper) {
        this.paper = paper;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId() && getUserId() == comment.getUserId() && Objects.equals(getPaper(), comment.getPaper()) && Objects.equals(getContent(), comment.getContent());
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getPaper(), getContent());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", paper=" + paper +
                ", content='" + content + '\'' +
                '}';
    }
}
