package ru.sfedu.securityservice.models;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.securityservice.models.enums.BondType;

import java.util.Objects;


/**
 * The type Bond.
 */
@Root(name = "bond")
public class Bond extends SecurityPaper{
    @Element
    @CsvBindByName
    private Integer nominalValue;
    @Element
    @CsvBindByName
    private BondType type;

    /**
     * Instantiates a new Bond.
     */
    public Bond() {
    }

    /**
     * Instantiates a new Bond.
     *
     * @param nominalValue the nominal value
     * @param type         the type
     */
    public Bond(Integer nominalValue, BondType type) {
        this.nominalValue = nominalValue;
        this.type = type;
    }

    /**
     * Instantiates a new Bond.
     *
     * @param id           the id
     * @param name         the name
     * @param cost         the cost
     * @param nominalValue the nominal value
     * @param type         the type
     */
    public Bond(long id, String name, Integer cost, Integer nominalValue, BondType type) {
        super(id, name, cost);
        this.nominalValue = nominalValue;
        this.type = type;
    }

    /**
     * Gets nominal value.
     *
     * @return the nominal value
     */
    public Integer getNominalValue() {
        return nominalValue;
    }

    /**
     * Sets nominal value.
     *
     * @param nominalValue the nominal value
     */
    public void setNominalValue(Integer nominalValue) {
        this.nominalValue = nominalValue;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public BondType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(BondType type) {
        this.type = type;
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
        if (!(o instanceof Bond)) return false;
        if (!super.equals(o)) return false;
        Bond bond = (Bond) o;
        return getNominalValue().equals(bond.getNominalValue()) && getType() == bond.getType();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNominalValue(), getType());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Bond{" +
                "nominalValue=" + nominalValue +
                ", type=" + type +
                '}';
    }
}
