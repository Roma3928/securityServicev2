package ru.sfedu.securityservice.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.securityservice.models.SecurityPaper;

public class SecurityPaperConverter extends AbstractBeanField<SecurityPaper, Integer> {
    private static final Logger log = LogManager.getLogger(SecurityPaperConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String indexString = s.substring(1, s.length() - 1);
        SecurityPaper securityPaper = new SecurityPaper();
        if (!indexString.isEmpty()) {
            securityPaper.setId(Long.parseLong(indexString));
        }
        return securityPaper;
    }

    public String convertToWrite(Object value) {
        SecurityPaper securityPaper = (SecurityPaper) value;
        StringBuilder builder = new StringBuilder("[");
        builder.append(securityPaper.getId());
        builder.append(",");
        builder.delete(builder.length() - 1, builder.length());
        builder.append("]");
        return builder.toString();
    }
}
