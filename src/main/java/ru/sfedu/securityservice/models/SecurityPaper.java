package ru.sfedu.securityservice.models;


import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Objects;


/**
 * The type Security paper.
 */
@Root(name = "securitypaper")
public class SecurityPaper implements Serializable {
    @Attribute
    @CsvBindByName
    private long id;
    @Element
    @CsvBindByName
    private String name;
    @Element
    @CsvBindByName
    private Integer cost;

    /**
     * Instantiates a new Security paper.
     */
    public SecurityPaper() {
    }

    /**
     * Instantiates a new Security paper.
     *
     * @param id   the id
     * @param name the name
     * @param cost the cost
     */
    public SecurityPaper(long id, String name, Integer cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
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
     * Gets cost.
     *
     * @return the cost
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * Sets cost.
     *
     * @param cost the cost
     */
    public void setCost(Integer cost) {
        this.cost = cost;
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
        if (!(o instanceof SecurityPaper)) return false;
        SecurityPaper that = (SecurityPaper) o;
        return getId() == that.getId() && getName().equals(that.getName()) && getCost().equals(that.getCost());
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCost());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "SecurityPaper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }
}
