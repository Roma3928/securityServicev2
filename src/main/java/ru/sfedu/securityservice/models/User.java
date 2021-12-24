package ru.sfedu.securityservice.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import ru.sfedu.securityservice.utils.SecurityPaperListConverter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * The type User.
 */
@Root(name = "user")
public class User implements Serializable {
    @Attribute
    @CsvBindByName
    private long id;
    @Element
    @CsvBindByName
    private String name;
    @ElementList
    @CsvCustomBindByName(converter = SecurityPaperListConverter.class)
    private List<SecurityPaper> favorites;

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param id        the id
     * @param name      the name
     * @param favorites the favorites
     */
    public User(long id, String name, List<SecurityPaper> favorites) {
        this.id = id;
        this.name = name;
        this.favorites = favorites;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets favorites.
     *
     * @return the favorites
     */
    public List<SecurityPaper> getFavorites() {
        return favorites;
    }

    /**
     * Sets favorites.
     *
     * @param favorites the favorites
     */
    public void setFavorites(List<SecurityPaper> favorites) {
        this.favorites = favorites;
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
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && getName().equals(user.getName()) && Objects.equals(getFavorites(), user.getFavorites());
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getFavorites());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", favorites=" + favorites +
                '}';
    }
}
