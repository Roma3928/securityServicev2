package ru.sfedu.securityservice.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.securityservice.models.User;

public class UserConverter extends AbstractBeanField<User, Integer> {
    private static final Logger log = LogManager.getLogger(SecurityPaperConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        String indexString = s.substring(1, s.length() - 1);
        User user = new User();
        if (!indexString.isEmpty()) {
            user.setId(Long.parseLong(indexString));
        }
        return user;
    }

    public String convertToWrite(Object value) {
        User user = (User) value;
        StringBuilder builder = new StringBuilder("[");
        builder.append(user.getId());
        builder.append(",");
        builder.delete(builder.length() - 1, builder.length());
        builder.append("]");
        return builder.toString();
    }
}