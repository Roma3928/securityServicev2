package ru.sfedu.securityservice.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.securityservice.Constants;
import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.models.*;
import ru.sfedu.securityservice.models.enums.Outcomes;
import ru.sfedu.securityservice.utils.ConfigurationUtil;
import ru.sfedu.securityservice.utils.HistoryContent;
import ru.sfedu.securityservice.utils.Wrapper;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static ru.sfedu.securityservice.utils.HistoryUtil.saveToLog;

/**
 * The type Data provider xml.
 */
public class DataProviderXML implements DataProvider {

    private static final Logger log = LogManager.getLogger(DataProviderCsv.class);

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
            if (user == null) {
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
            if (user == null) {
                return new Result<>(Outcomes.Fail, false);
            }
            user.getFavorites().remove(securityPaper);
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
            List<SecurityPaper> securityPaperList = xmlToBean(Constants.SECURITY_XML, method);
            SecurityPaper securityPaper = securityPaperList.stream()
                    .filter(el -> Objects.equals(el.getName(), paperName))
                    .findFirst().orElse(null);
            if (securityPaper == null) {
                return new Result(Outcomes.Fail, false);
            }
            switch (command.toUpperCase()) {
                case "ADDFAV":
                    return new Result(Outcomes.Complete, addToFavorites(securityPaper, userId).getData());
                case "DELFAV":
                    return new Result(Outcomes.Complete, deleteFromFavorites(securityPaper, userId).getData());
                default:
                    return new Result<>(Outcomes.Complete, securityPaper);
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
                case "Bond":
                    return new Result<>(Outcomes.Complete, calculateBond().getData());
                case "Stock":
                    return new Result<>(Outcomes.Complete, calculateStock().getData());
                case "ETF":
                    return new Result<>(Outcomes.Complete, calculateETF().getData());
                default:
                    return new Result<>(Outcomes.Complete, calculateETF().getData() + calculateStock().getData() + calculateETF().getData());
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
            List<Bond> bondList = xmlToBean(Constants.BOND_XML, method);
            return new Result<>(Outcomes.Complete, bondList.stream().mapToInt(bond -> bond.getCost()).sum());
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
            List<Stock> bondList = xmlToBean(Constants.STOCK_XML, method);
            return new Result<>(Outcomes.Complete, bondList.stream().mapToInt(stock -> stock.getCost()).sum());
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
            List<ExchangeTradedFunction> bondList = xmlToBean(Constants.ETF_XML, method);
            return new Result<>(Outcomes.Complete, bondList.stream().mapToInt(etf -> etf.getCost()).sum());
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
        List<Bond> bondList = xmlToBean(Constants.BOND_XML, method);
        if (bondList.stream().anyMatch(o -> o.getId() == bond.getId())) {
            return new Result<>(Outcomes.Fail, bond);
        }
        bondList.add(bond);
        if (beanToXml(bondList, Constants.BOND_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, bond);
        }
        log.debug("Success", bond);
        saveToLog(createHistoryContent(method, bond, Outcomes.Complete));
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
        List<Bond> objects = xmlToBean(Constants.BOND_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == bond.getId())) {
            return new Result(Outcomes.Fail, bond, format(Constants.ID_NOT_EXISTS, bond.getId()));
        }
        objects.removeIf(o -> o.getId() == bond.getId());
        objects.add(bond);
        if (beanToXml(objects, Constants.BOND_XML, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, bond);
        }
        saveToLog(createHistoryContent(method, bond, Outcomes.Complete));
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
        List<Bond> objects = xmlToBean(Constants.BOND_XML, method);
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
        List<Bond> objects = xmlToBean(Constants.BOND_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.BOND_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
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
        List<Comment> orderList = xmlToBean(Constants.COMMENT_XML, method);
        if (orderList.stream().anyMatch(o -> o.getId() == comment.getId())) {
            return new Result<>(Outcomes.Fail, comment);
        }
        orderList.add(comment);
        if (beanToXml(orderList, Constants.COMMENT_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, comment);
        }
        log.debug("Success", comment);
        saveToLog(createHistoryContent(method, comment, Outcomes.Complete));
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
        List<Comment> objects = xmlToBean(Constants.COMMENT_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == comment.getId())) {
            return new Result(Outcomes.Fail, comment, format(Constants.ID_NOT_EXISTS, comment.getId()));
        }
        objects.removeIf(o -> o.getId() == comment.getId());
        objects.add(comment);
        if (beanToXml(objects, Constants.COMMENT_XML, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, comment);
        }
        saveToLog(createHistoryContent(method, comment, Outcomes.Complete));
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
        List<Comment> objects = xmlToBean(Constants.COMMENT_XML, method);
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
        List<Comment> objects = xmlToBean(Constants.COMMENT_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.COMMENT_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
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
        List<ExchangeTradedFunction> exchangeTradedFunctions = xmlToBean(Constants.ETF_XML, method);
        if (exchangeTradedFunctions.stream().anyMatch(o -> o.getId() == exchangeTradedFunction.getId())) {
            return new Result<>(Outcomes.Fail, exchangeTradedFunction);
        }
        exchangeTradedFunctions.add(exchangeTradedFunction);
        if (beanToXml(exchangeTradedFunctions, Constants.ETF_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, exchangeTradedFunction);
        }
        log.debug("Success", exchangeTradedFunctions);
        saveToLog(createHistoryContent(method, exchangeTradedFunction, Outcomes.Complete));
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
        List<ExchangeTradedFunction> objects = xmlToBean(Constants.ETF_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == exchangeTradedFunction.getId())) {
            return new Result(Outcomes.Fail, exchangeTradedFunction, format(Constants.ID_NOT_EXISTS, exchangeTradedFunction.getId()));
        }
        objects.removeIf(o -> o.getId() == exchangeTradedFunction.getId());
        objects.add(exchangeTradedFunction);
        if (beanToXml(objects, Constants.ID_NOT_EXISTS, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, exchangeTradedFunction);
        }
        saveToLog(createHistoryContent(method, exchangeTradedFunction, Outcomes.Complete));
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
        List<ExchangeTradedFunction> objects = xmlToBean(Constants.ETF_XML, method);
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
        List<ExchangeTradedFunction> objects = xmlToBean(Constants.ETF_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.ETF_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
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
        List<SecurityPaper> securityPaperList = xmlToBean(Constants.SECURITY_XML, method);
        if (securityPaperList.stream().anyMatch(o -> o.getId() == securityPaper.getId())) {
            return new Result<>(Outcomes.Fail, securityPaper);
        }
        securityPaperList.add(securityPaper);
        if (beanToXml(securityPaperList, Constants.SECURITY_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, securityPaper);
        }
        log.debug("Success", securityPaper);
        saveToLog(createHistoryContent(method, securityPaper, Outcomes.Complete));
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
        List<SecurityPaper> objects = xmlToBean(Constants.SECURITY_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == securityPaper.getId())) {
            return new Result(Outcomes.Fail, securityPaper, format(Constants.ID_NOT_EXISTS, securityPaper.getId()));
        }
        objects.removeIf(o -> o.getId() == securityPaper.getId());
        objects.add(securityPaper);
        if (beanToXml(objects, Constants.SECURITY_XML, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, securityPaper);
        }
        saveToLog(createHistoryContent(method, securityPaper, Outcomes.Complete));
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
        List<SecurityPaper> objects = xmlToBean(Constants.SECURITY_XML, method);
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
        List<SecurityPaper> objects = xmlToBean(Constants.SECURITY_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.SECURITY_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
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
        List<Stock> stockList = xmlToBean(Constants.STOCK_XML, method);
        if (stockList.stream().anyMatch(o -> o.getId() == stock.getId())) {
            return new Result<>(Outcomes.Fail, stock);
        }
        stockList.add(stock);
        if (beanToXml(stockList, Constants.STOCK_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, stock);
        }
        log.debug("Success", stockList);
        saveToLog(createHistoryContent(method, stock, Outcomes.Complete));
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
        List<Stock> objects = xmlToBean(Constants.STOCK_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == stock.getId())) {
            return new Result(Outcomes.Fail, stock, format(Constants.ID_NOT_EXISTS, stock.getId()));
        }
        objects.removeIf(o -> o.getId() == stock.getId());
        objects.add(stock);
        if (beanToXml(objects, Constants.STOCK_XML, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, stock);
        }
        saveToLog(createHistoryContent(method, stock, Outcomes.Complete));
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
        List<Stock> objects = xmlToBean(Constants.STOCK_XML, method);
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
        List<Stock> objects = xmlToBean(Constants.STOCK_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.STOCK_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
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
        List<User> users = xmlToBean(Constants.USER_XML, method);
        if (users.stream().anyMatch(o -> o.getId() == user.getId())) {
            return new Result<>(Outcomes.Fail, user);
        }
        users.add(user);
        if (beanToXml(users, Constants.USER_XML, method) == Outcomes.Fail) {
            return new Result<>(Outcomes.Fail, user);
        }
        log.debug("Success", users);
        saveToLog(createHistoryContent(method, user, Outcomes.Complete));
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
        List<User> objects = xmlToBean(Constants.USER_XML, method);
        if (objects.stream().noneMatch(o -> o.getId() == user.getId())) {
            return new Result(Outcomes.Fail, user, format(Constants.ID_NOT_EXISTS, user.getId()));
        }
        objects.removeIf(o -> o.getId() == user.getId());
        objects.add(user);
        if (beanToXml(objects, Constants.USER_XML, method) == Outcomes.Fail) {
            return new Result(Outcomes.Fail, user);
        }
        saveToLog(createHistoryContent(method, user, Outcomes.Complete));
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
        List<User> objects = xmlToBean(Constants.USER_XML, method);
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
        List<User> objects = xmlToBean(Constants.USER_XML, method);
        objects.removeIf(o -> o.getId() == id);
        beanToXml(objects, Constants.USER_XML, method);
        saveToLog(createHistoryContent(method, id, Outcomes.Complete));
        return new Result<>(Outcomes.Complete);
    }

    private <T> Outcomes beanToXml(List<T> ts, String key, String method) {
        Outcomes outcomes;
        try {
            FileWriter fileWriter = new FileWriter(ConfigurationUtil.getConfigurationEntry(key));
            Serializer serializer = new Persister();
            Wrapper<T> container = new Wrapper<T>(ts);
            serializer.write(container, fileWriter);
            fileWriter.close();
            outcomes = Outcomes.Complete;
        } catch (Exception exception) {
            log.error(exception);
            outcomes = Outcomes.Fail;
        }
        //saveToLog(createHistoryContent(method, ts, outcomes));
        return outcomes;
    }

    private <T> List<T> xmlToBean(String key, String method) {
        try {
            FileReader reader = new FileReader(ConfigurationUtil.getConfigurationEntry(key));
            Serializer serializer = new Persister();
            Wrapper<T> container = serializer.read(Wrapper.class, reader);
            final List<T> querySet = container.getList();
            saveToLog(createHistoryContent(method, querySet, Outcomes.Complete));
            reader.close();
            return querySet;
        } catch (Exception e) {
            log.error(e);
        }
        saveToLog(createHistoryContent(method, null, Outcomes.Fail));
        return new ArrayList<>();
    }

    private HistoryContent createHistoryContent(String method, Object object, Outcomes outcomes) {
        return new HistoryContent(this.getClass().getSimpleName(), new Date(), Constants.DEFAULT_ACTOR, method, object, outcomes);
    }
}
