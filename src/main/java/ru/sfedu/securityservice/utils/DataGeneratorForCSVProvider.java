package ru.sfedu.securityservice.utils;

import ru.sfedu.securityservice.api.DataProvider;
import ru.sfedu.securityservice.api.DataProviderCsv;
import ru.sfedu.securityservice.models.*;

import java.util.ArrayList;
import java.util.List;

public class DataGeneratorForCSVProvider {
    public static DataProviderCsv instance = new DataProviderCsv();

    public static void addRecord(DataProvider dataProvider) {

        for (int i = 1; i <= 4; i++) {
            SecurityPaper securityPaper = new SecurityPaper(i, DataForGenerator.name[i - 1], DataForGenerator.price[i - 1]);
            dataProvider.insertSecurityPaper(securityPaper);
        }
        for (int i = 1; i <= 4; i++) {
            Bond bond = new Bond(i, DataForGenerator.name[i - 1], DataForGenerator.price[i - 1], DataForGenerator.nominalPrice[i - 1], DataForGenerator.bondTypes[i - 1]);
            dataProvider.insertBond(bond);
        }
        for (int i = 1; i <= 4; i++) {
            Stock stock = new Stock(i, DataForGenerator.name[i - 1], DataForGenerator.price[i - 1], DataForGenerator.nominalPrice[i - 1], DataForGenerator.stockCategories[i - 1]);
            dataProvider.insertStock(stock);
        }
        for (int i = 1; i <= 4; i++) {
            ExchangeTradedFunction exchangeTradedFunction = new ExchangeTradedFunction(i, DataForGenerator.name[i - 1], DataForGenerator.price[i - 1], DataForGenerator.investmentDirection[i - 1]);
            dataProvider.insertExchangeTradedFunction(exchangeTradedFunction);
        }
        for (int i = 1; i <= 4; i++) {
            User user = new User(i, DataForGenerator.name[i - 1], getSecurityPapers(dataProvider));
            dataProvider.insertUser(user);
        }
        for (int i = 1; i <= 4; i++) {
            Comment comment = new Comment(i, i, getSecurityPaper(dataProvider), DataForGenerator.comment[i - 1]);
            dataProvider.insertComment(comment);
        }
    }

    public static List<SecurityPaper> getSecurityPapers(DataProvider dataProvider) {
        List<SecurityPaper> securityPaperList = new ArrayList<>();
        int max = 2;
        int min = 1;
        for (int i = 1; i <= 2; i++) {
            securityPaperList.add(dataProvider.getBondById(((int) ((Math.random() * ((max - min) + 1)) + min))).get());
            securityPaperList.add(dataProvider.getStock(((int) ((Math.random() * ((max - min) + 1)) + min))).get());
            securityPaperList.add(dataProvider.getExchangeTradedFunctionById(((int) ((Math.random() * ((max - min) + 1)) + min))).get());
        }
        return securityPaperList;
    }

    public static SecurityPaper getSecurityPaper(DataProvider dataProvider) {
        int max = 2;
        int min = 1;
        return dataProvider.getSecurityPaperById(((int) ((Math.random() * ((max - min) + 1)) + min))).get();
    }
}
