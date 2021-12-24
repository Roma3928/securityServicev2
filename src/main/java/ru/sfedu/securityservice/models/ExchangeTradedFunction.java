package ru.sfedu.securityservice.models;


import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.securityservice.models.enums.InvestmentDirection;

import java.util.Objects;


/**
 * The type Exchange traded function.
 */
@Root(name = "etf")
public class ExchangeTradedFunction extends SecurityPaper {

    @Element
    @CsvBindByName
    private InvestmentDirection direction;

    /**
     * Instantiates a new Exchange traded function.
     */
    public ExchangeTradedFunction() {
    }

    /**
     * Instantiates a new Exchange traded function.
     *
     * @param direction the direction
     */
    public ExchangeTradedFunction(InvestmentDirection direction) {
        this.direction = direction;
    }

    /**
     * Instantiates a new Exchange traded function.
     *
     * @param id        the id
     * @param name      the name
     * @param cost      the cost
     * @param direction the direction
     */
    public ExchangeTradedFunction(long id, String name, Integer cost, InvestmentDirection direction) {
        super(id, name, cost);
        this.direction = direction;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public InvestmentDirection getDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(InvestmentDirection direction) {
        this.direction = direction;
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
        if (!(o instanceof ExchangeTradedFunction)) return false;
        if (!super.equals(o)) return false;
        ExchangeTradedFunction that = (ExchangeTradedFunction) o;
        return getDirection() == that.getDirection();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDirection());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ExchangeTradedFunction{" +
                "direction=" + direction +
                '}';
    }
}
