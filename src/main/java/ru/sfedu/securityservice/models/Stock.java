package ru.sfedu.securityservice.models;


import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import ru.sfedu.securityservice.models.enums.StockCategory;

import java.util.Objects;


/**
 * The type Stock.
 */
@Root(name = "stock")
public class Stock extends SecurityPaper {
    @Element
    @CsvBindByName
    private Integer normalValue;
    @Element
    @CsvBindByName
    private StockCategory category;

    /**
     * Instantiates a new Stock.
     */
    public Stock() {
    }

    /**
     * Instantiates a new Stock.
     *
     * @param normalValue the normal value
     * @param category    the category
     */
    public Stock(Integer normalValue, StockCategory category) {
        this.normalValue = normalValue;
        this.category = category;
    }

    /**
     * Instantiates a new Stock.
     *
     * @param id          the id
     * @param name        the name
     * @param cost        the cost
     * @param normalValue the normal value
     * @param category    the category
     */
    public Stock(long id, String name, Integer cost, Integer normalValue, StockCategory category) {
        super(id, name, cost);
        this.normalValue = normalValue;
        this.category = category;
    }

    /**
     * Gets normal value.
     *
     * @return the normal value
     */
    public Integer getNormalValue() {
        return normalValue;
    }

    /**
     * Sets normal value.
     *
     * @param normalValue the normal value
     */
    public void setNormalValue(Integer normalValue) {
        this.normalValue = normalValue;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public StockCategory getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(StockCategory category) {
        this.category = category;
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
        if (!(o instanceof Stock)) return false;//был ли объект, на который ссылается переменная , создан на основе какого-либо класса .
        if (!super.equals(o)) return false;
        Stock stock = (Stock) o;
        return getNormalValue().equals(stock.getNormalValue()) && getCategory() == stock.getCategory();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNormalValue(), getCategory());
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Stock{" +
                "normalValue=" + normalValue +
                ", category=" + category +
                '}';
    }
}
