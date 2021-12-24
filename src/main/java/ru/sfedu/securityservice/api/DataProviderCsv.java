package ru.sfedu.securityservice.api;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.securityservice.Constants;
import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.models.*;
import ru.sfedu.securityservice.models.enums.Outcomes;
import ru.sfedu.securityservice.utils.ConfigurationUtil;
import ru.sfedu.securityservice.utils.HistoryContent;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static ru.sfedu.securityservice.utils.HistoryUtil.saveToLog;

/**
 * The type Data provider csv.
 */
public class DataProviderCsv implements DataProvider {

    private static final Logger log = LogManager.getLogger(DataProviderCsv.class);

    /**
     * Instantiates a new Data provider csv.
     */
    public DataProviderCsv() {
    }


    /**
     * Add to favorites result.
     *
     * @param securityPaper the security paper
     * @param userId        the user id
     * @return the result
     */
    @Override
    public Result<Boolean> addToFavorites(SecurityPaper securityPaper, long userId) {
        try {
            User user = getUser(userId).orElse(null);
            if(user == null){
                return new Result<>(Outcomes.Fail, false);
            }
            user.getFavorites().add(securityPaper);
            updateUser(user);
            return new Result<>(Outcomes.Complete, true);
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Delete from favorites result.
     *
     * @param securityPaper the security paper
     * @param userId        the user id
     * @return the result
     */
    @Override
    public Result<Boolean> deleteFromFavorites(SecurityPaper securityPaper, long userId) {
        try {
            User user = getUser(userId).orElse(null);
            if(user == null){
                return new Result<>(Outcomes.Fail, false);
            }

            List<SecurityPaper> fav = new ArrayList<>();
//            log.error(fav);
            for (SecurityPaper f: user.getFavorites()){
                if(f.getId()==securityPaper.getId()) continue;
                else fav.add(f);
            }
//            log.error(fav);
            user.setFavorites(fav);
            updateUser(user);
            return new Result<>(Outcomes.Complete, true);
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Security controller result.
     *
     * @param paperName the paper name
     * @param command   the command
     * @param userId    the user id
     * @return the result
     */
    @Override
    public Result<SecurityPaper> securityController(String paperName, String command, long userId) {
        try {
            final String method = currentThread().getStackTrace()[1].getMethodName();
            List<SecurityPaper> securityPaperList = csvToBean(SecurityPaper.class, Constants.SECURITY, method);
            SecurityPaper securityPaper = securityPaperList.stream()
                    .filter(el -> Objects.equals(el.getName(), paperName))
                    .findFirst().orElse(null);
            if(securityPaper == null){
                return new Result(Outcomes.Fail,false);
            }
            switch (command.toUpperCase()) {
                case "ADDFAV":
                    return new Result(Outcomes.Complete,addToFavorites(securityPaper, userId).getData());
                case "DELFAV":
                    return new Result(Outcomes.Complete,deleteFromFavorites(securityPaper, userId).getData());
                default:
                    return new Result<>(Outcomes.Complete,securityPaper);
            }
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Calculate all paper cost result.
     *
     * @param securityPaperType the security paper type
     * @return the result
     */
    @Override
    public Result<Integer> calculateAllPaperCost(String securityPaperType) {
        try {
            switch (securityPaperType.toLowerCase()) {
                case "bond":
                    return new Result<>(Outcomes.Complete, calculateBond().getData());
                case "stock":
                    return new Result<>(Outcomes.Complete, calculateStock().getData());
                case "etf":
                    return new Result<>(Outcomes.Complete, calculateETF().getData());
                default:
                    return new Result<>(Outcomes.Complete, calculateETF().getData()+calculateStock().getData()+calculateETF().getData());
            }
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Calculate bond result.
     *
     * @return the result
     */
    @Override
    public Result<Integer> calculateBond() {
        try {
            final String method = currentThread().getStackTrace()[1].getMethodName();
            List<Bond> bondList = csvToBean(Bond.class, Constants.BOND, method);
            return new Result<>(Outcomes.Complete,bondList.stream().mapToInt(bond -> bond.getCost()).sum());
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Calculate stock result.
     *
     * @return the result
     */
    @Override
    public Result<Integer> calculateStock() {
        try {
            final String method = currentThread().getStackTrace()[1].getMethodName();
            List<Stock> bondList = csvToBean(Stock.class, Constants.STOCK, method);
            return new Result<>(Outcomes.Complete,bondList.stream().mapToInt(stock -> stock.getCost()).sum());
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Calculate etf result.
     *
     * @return the result
     */
    @Override
    public Result<Integer> calculateETF() {
        try {
            final String method = currentThread().getStackTrace()[1].getMethodName();
            List<ExchangeTradedFunction> bondList = csvToBean(ExchangeTradedFunction.class, Constants.ETF_CSV, method);
            return new Result<>(Outcomes.Complete,bondList.stream().mapToInt(etf -> etf.getCost()).sum());
        } catch (Exception e) {
            log.error(e);
            return new Result<>(Outcomes.Fail);
        }
    }

    /**
     * Insert bond result.
     *
     * @param bond the bond
     * @return the result
     */
    @Override
    public Result<Bond> insertBond(Bond bond) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Bond> bondList = csvToBean(Bond.class, Constants.BOND, method);
        if (bondList.stream().anyMatch(o -> o.getId() == bond.getId())) {
            return new Result<>(Outcomes.Fail, bond);
        }
        bondList.add(bond);
        if (beanToCsv(bondList, Constants.BOND, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, bond);
        }
        log.debug("Success",bond);
        return new Result<>(Outcomes.Complete, bond);
    }

    /**
     * Update bond result.
     *
     * @param bond the bond
     * @return the result
     */
    @Override
    public Result<Void> updateBond(Bond bond) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Bond> objects = csvToBean(Bond.class, Constants.BOND, method);
        if (objects.stream().noneMatch(o -> o.getId() == bond.getId())) {
            return new Result(Outcomes.Fail, bond, format(Constants.ID_NOT_EXISTS, bond.getId()));
        }
        objects.removeIf(o -> o.getId() == bond.getId());
        objects.add(bond);
        if (beanToCsv(objects, Constants.BOND, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, bond);
        }
        return new Result(Outcomes.Complete, bond);
    }

    /**
     * Gets bond by id.
     *
     * @param id the id
     * @return the bond by id
     */
    @Override
    public Optional<Bond> getBondById(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Bond> objects = csvToBean(Bond.class, Constants.BOND, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete bond result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteBond(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Bond> objects = csvToBean(Bond.class, Constants.BOND, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.BOND, method);
        return new Result<>(Outcomes.Complete);
    }

    /**
     * Insert comment result.
     *
     * @param comment the comment
     * @return the result
     */
    @Override
    public Result<Comment> insertComment(Comment comment) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Comment> orderList = csvToBean(Comment.class, Constants.COMMENT, method);
        if (orderList.stream().anyMatch(o -> o.getId() == comment.getId())) {
            return new Result<>(Outcomes.Fail, comment);
        }
        orderList.add(comment);
        if (beanToCsv(orderList, Constants.COMMENT, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, comment);
        }
        log.debug("Success",comment);
        return new Result<>(Outcomes.Complete, comment);
    }

    /**
     * Update comment result.
     *
     * @param comment the comment
     * @return the result
     */
    @Override
    public Result<Void> updateComment(Comment comment) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Comment> objects = csvToBean(Comment.class, Constants.COMMENT, method);
        if (objects.stream().noneMatch(o -> o.getId() == comment.getId())) {
            return new Result(Outcomes.Fail, comment, format(Constants.ID_NOT_EXISTS, comment.getId()));
        }
        objects.removeIf(o -> o.getId() == comment.getId());
        objects.add(comment);
        if (beanToCsv(objects, Constants.COMMENT, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, comment);
        }
        return new Result(Outcomes.Complete, comment);
    }

    /**
     * Gets comment by id.
     *
     * @param id the id
     * @return the comment by id
     */
    @Override
    public Optional<Comment> getCommentById(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Comment> objects = csvToBean(Comment.class, Constants.COMMENT, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete comment result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteComment(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Comment> objects = csvToBean(Comment.class, Constants.COMMENT, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.COMMENT, method);
        return new Result<>(Outcomes.Complete);
    }

    /**
     * Insert exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    @Override
    public Result<ExchangeTradedFunction> insertExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<ExchangeTradedFunction> exchangeTradedFunctions = csvToBean(ExchangeTradedFunction.class, Constants.ETF_CSV, method);
        if (exchangeTradedFunctions.stream().anyMatch(o -> o.getId() == exchangeTradedFunction.getId())) {
            return new Result<>(Outcomes.Fail, exchangeTradedFunction);
        }
        exchangeTradedFunctions.add(exchangeTradedFunction);
        if (beanToCsv(exchangeTradedFunctions, Constants.ETF_CSV, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, exchangeTradedFunction);
        }
        log.debug("Success",exchangeTradedFunctions);
        return new Result<>(Outcomes.Complete, exchangeTradedFunction);
    }

    /**
     * Update exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    @Override
    public Result<Void> updateExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<ExchangeTradedFunction> objects = csvToBean(ExchangeTradedFunction.class, Constants.ETF_CSV, method);
        if (objects.stream().noneMatch(o -> o.getId() == exchangeTradedFunction.getId())) {
            return new Result(Outcomes.Fail, exchangeTradedFunction, format(Constants.ID_NOT_EXISTS, exchangeTradedFunction.getId()));
        }
        objects.removeIf(o -> o.getId() == exchangeTradedFunction.getId());
        objects.add(exchangeTradedFunction);
        if (beanToCsv(objects, Constants.ETF_CSV, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, exchangeTradedFunction);
        }
        return new Result(Outcomes.Complete, exchangeTradedFunction);
    }

    /**
     * Gets exchange traded function by id.
     *
     * @param id the id
     * @return the exchange traded function by id
     */
    @Override
    public Optional<ExchangeTradedFunction> getExchangeTradedFunctionById(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<ExchangeTradedFunction> objects = csvToBean(ExchangeTradedFunction.class, Constants.ETF_CSV, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete exchange traded function result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteExchangeTradedFunction(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<ExchangeTradedFunction> objects = csvToBean(ExchangeTradedFunction.class, Constants.ETF_CSV, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.ETF_CSV, method);
        return new Result<>(Outcomes.Complete);
    }

    /**
     * Insert security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    @Override
    public Result<SecurityPaper> insertSecurityPaper(SecurityPaper securityPaper) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<SecurityPaper> securityPaperList = csvToBean(SecurityPaper.class, Constants.SECURITY, method);
        if (securityPaperList.stream().anyMatch(o -> o.getId() == securityPaper.getId())) {
            return new Result<>(Outcomes.Fail, securityPaper);
        }
        securityPaperList.add(securityPaper);
        if (beanToCsv(securityPaperList, Constants.SECURITY, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, securityPaper);
        }
        log.debug("Success",securityPaper);
        return new Result<>(Outcomes.Complete, securityPaper);
    }

    /**
     * Update security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    @Override
    public Result<Void> updateSecurityPaper(SecurityPaper securityPaper) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<SecurityPaper> objects = csvToBean(SecurityPaper.class, Constants.SECURITY, method);
        if (objects.stream().noneMatch(o -> o.getId() == securityPaper.getId())) {
            return new Result(Outcomes.Fail, securityPaper, format(Constants.ID_NOT_EXISTS, securityPaper.getId()));
        }
        objects.removeIf(o -> o.getId() == securityPaper.getId());
        objects.add(securityPaper);
        if (beanToCsv(objects, Constants.SECURITY, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, securityPaper);
        }
        return new Result(Outcomes.Complete, securityPaper);
    }

    /**
     * Gets security paper by id.
     *
     * @param id the id
     * @return the security paper by id
     */
    @Override
    public Optional<SecurityPaper> getSecurityPaperById(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<SecurityPaper> objects = csvToBean(SecurityPaper.class, Constants.SECURITY, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete security paper result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteSecurityPaper(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<SecurityPaper> objects = csvToBean(SecurityPaper.class, Constants.SECURITY, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.SECURITY, method);
        return new Result<>(Outcomes.Complete);
    }

    /**
     * Insert stock result.
     *
     * @param stock the stock
     * @return the result
     */
    @Override
    public Result<Stock> insertStock(Stock stock) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Stock> stockList = csvToBean(Stock.class, Constants.STOCK, method);
        if (stockList.stream().anyMatch(o -> o.getId() == stock.getId())) {
            return new Result<>(Outcomes.Fail, stock);
        }
        stockList.add(stock);
        if (beanToCsv(stockList, Constants.STOCK, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, stock);
        }
        log.debug("Success",stockList);
        return new Result<>(Outcomes.Complete, stock);
    }

    /**
     * Update stock result.
     *
     * @param stock the stock
     * @return the result
     */
    @Override
    public Result<Void> updateStock(Stock stock) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Stock> objects = csvToBean(Stock.class, Constants.STOCK, method);
        if (objects.stream().noneMatch(o -> o.getId() == stock.getId())) {
            return new Result(Outcomes.Fail, stock, format(Constants.ID_NOT_EXISTS, stock.getId()));
        }
        objects.removeIf(o -> o.getId() == stock.getId());
        objects.add(stock);
        if (beanToCsv(objects, Constants.STOCK, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, stock);
        }
        return new Result(Outcomes.Complete, stock);
    }

    /**
     * Gets stock.
     *
     * @param id the id
     * @return the stock
     */
    @Override
    public Optional<Stock> getStock(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Stock> objects = csvToBean(Stock.class, Constants.STOCK, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete stock result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteStock(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<Stock> objects = csvToBean(Stock.class, Constants.STOCK, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.STOCK, method);
        return new Result<>(Outcomes.Complete);
    }

    /**
     * Insert user result.
     *
     * @param user the user
     * @return the result
     */
    @Override
    public Result<User> insertUser(User user) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<User> users = csvToBean(User.class, Constants.USER, method);
        if (users.stream().anyMatch(o -> o.getId() == user.getId())) {
            return new Result<>(Outcomes.Fail, user);
        }
        users.add(user);
        if (beanToCsv(users, Constants.USER, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, user);
        }
        log.debug("Success",users);
        return new Result<>(Outcomes.Complete, user);
    }

    /**
     * Update user result.
     *
     * @param user the user
     * @return the result
     */
    @Override
    public Result<Void> updateUser(User user) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<User> objects = csvToBean(User.class, Constants.USER, method);
        if (objects.stream().noneMatch(o -> o.getId() == user.getId())) {
            return new Result(Outcomes.Fail, user, format(Constants.ID_NOT_EXISTS, user.getId()));
        }
        objects.removeIf(o -> o.getId() == user.getId());
        objects.add(user);
        if (beanToCsv(objects, Constants.USER, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, user);
        }
        return new Result(Outcomes.Complete, user);
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    @Override
    public Optional<User> getUser(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<User> objects = csvToBean(User.class, Constants.USER, method);
        return objects.stream().filter(o -> o.getId() == id).findFirst();
    }

    /**
     * Delete user result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteUser(long id) {
        final String method = currentThread().getStackTrace()[1].getMethodName();
        List<User> objects = csvToBean(User.class, Constants.USER, method);
        objects.removeIf(o -> o.getId() == id);
        beanToCsv(objects, Constants.USER, method);
        return new Result<>(Outcomes.Complete);
    }

    private static HistoryContent createHistoryContent(String method, Object object, Outcomes outcomes) {
        return new HistoryContent(DataProviderCsv.class.getSimpleName(), new Date(), Constants.DEFAULT_ACTOR, method, object, outcomes);
    }

    private <T> Outcomes beanToCsv(List<T> ts, String key, String method) {
        Outcomes outcomes;
        try {
            FileWriter fileWriter = new FileWriter(ConfigurationUtil.getConfigurationEntry(key), false);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter).build();
            beanToCsv.write(ts);
            csvWriter.close();
            fileWriter.close();
            outcomes = Outcomes.Complete;
        } catch (Exception exception) {
            log.error(exception);
            outcomes = Outcomes.Fail;
        }
        saveToLog(createHistoryContent(method, ts, outcomes));
        return outcomes;
    }

    /**
     * Csv to bean list.
     *
     * @param <T>    the type parameter
     * @param cls    the cls
     * @param key    the key
     * @param method the method
     * @return the list
     */
    public static <T> List<T> csvToBean(Class<T> cls, String key, String method) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(ConfigurationUtil.getConfigurationEntry(key)));
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader).withType(cls).build();
            List<T> querySet = csvToBean.parse();
            csvReader.close();
            saveToLog(createHistoryContent(method, querySet, Outcomes.Complete));
            return querySet;
        } catch (Exception exception) {
            log.error(exception);
        }
        saveToLog(createHistoryContent(method, null, Outcomes.Fail));
        return new ArrayList<>();
    }
}