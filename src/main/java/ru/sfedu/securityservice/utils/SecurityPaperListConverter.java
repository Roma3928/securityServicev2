package ru.sfedu.securityservice.utils;

import com.opencsv.bean.AbstractBeanField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.securityservice.api.DataProviderCsv;
import ru.sfedu.securityservice.models.SecurityPaper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityPaperListConverter extends AbstractBeanField<SecurityPaper, Integer> {
    @Override
    protected Object convert(String s) {
        String indexString;
        indexString = s.substring(1, s.length() - 1);
        String[] unparsedIndexList = indexString.split(",");
        List<SecurityPaper> securityPapers = new ArrayList<>();
        for (String strIndex : unparsedIndexList) {
            if (!strIndex.isEmpty()) {
                SecurityPaper masterAccount = new SecurityPaper();
                masterAccount.setId(Long.parseLong(strIndex));
                securityPapers.add(masterAccount);
            }
        }
        return securityPapers;
    }

    public String convertToWrite(Object value) {
        List<SecurityPaper> securityPaperList = (List<SecurityPaper>) value;
        StringBuilder builder = new StringBuilder("[");
        if (securityPaperList.size() > 0) {
            for (SecurityPaper securityPaper : securityPaperList) {
                builder.append(securityPaper.getId());
                builder.append(",");
            }

            builder.delete(builder.length() - 1, builder.length());
        }
        builder.append("]");
        return builder.toString();
    }
}