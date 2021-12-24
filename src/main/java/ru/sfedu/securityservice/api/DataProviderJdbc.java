package ru.sfedu.securityservice.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.securityservice.Constants;
import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.models.*;
import ru.sfedu.securityservice.models.enums.BondType;
import ru.sfedu.securityservice.models.enums.InvestmentDirection;
import ru.sfedu.securityservice.models.enums.Outcomes;
import ru.sfedu.securityservice.models.enums.StockCategory;
import ru.sfedu.securityservice.utils.ConfigurationUtil;
import ru.sfedu.securityservice.utils.HistoryContent;

import static java.lang.Thread.currentThread;
import static ru.sfedu.securityservice.utils.HistoryUtil.saveToLog;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Date;

/**
 * The type Data provider jdbc.
 */
public class DataProviderJdbc implements DataProvider {

    /**
     * The Connection.
     */
    Connection connection;
    /**
     * The constant log.
     */
    public static final Logger log = LogManager.getLogger(DataProviderJdbc.class);

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
            if (user.getId() == 0) {
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
            List<SecurityPaper> securityPaperList = getListOfSecurityPaper();
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
            List<Bond> bondList = getListOfBond();
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
            List<Stock> bondList = getListOfStock();
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
            List<ExchangeTradedFunction> bondList = getListOfETF();
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
        Outcomes outcomes;
        try {
            outcomes = execute(bond,String.format(
                    Constants.INSERT_BOND,
                    bond.getName(),
                    bond.getCost(),
                    bond.getNominalValue(),
                    bond.getType()),Constants.ADD_BOND).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes,bond);
    }

    /**
     * Update bond result.
     *
     * @param bond the bond
     * @return the result
     */
    @Override
    public Result<Void> updateBond(Bond bond) {
        Outcomes outcomes;
        try {
            outcomes = execute(bond, String.format(Constants.UPDATE_BOND,
                    bond.getName(),bond.getCost(),bond.getNominalValue(),bond.getType()), Constants.UPD_BOND).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets bond by id.
     *
     * @param id the id
     * @return the bond by id
     */
    @Override
    public Optional<Bond> getBondById(long id) {
        try {
            ResultSet set = setResById(Bond.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                Bond bond= new Bond();
                bond.setId(set.getLong(Constants.SECURITYPAPER_ID));
                bond.setName(set.getString(Constants.SECURITYPAPER_NAME));
                bond.setCost(set.getInt(Constants.SECURITYPAPER_COST));
                bond.setNominalValue(set.getInt(Constants.BOND_VALUE));
                bond.setType(BondType.valueOf(set.getString(Constants.BOND_TYPE)));
                return Optional.of(bond);// возвращает Optional-объект
            } else {
                return Optional.of(new Bond());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new Bond());
        }
    }

    /**
     * Delete bond result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteBond(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_BOND, id), Constants.DEL_BOND).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    /**
     * Insert comment result.
     *
     * @param comment the comment
     * @return the result
     */
    @Override
    public Result<Comment> insertComment(Comment comment) {
        Outcomes outcomes;
        try {
            outcomes = execute(comment,String.format(
                    Constants.INSERT_COMMENT,
                    comment.getUserId(),
                    comment.getPaper().getId(),
                    comment.getContent()),Constants.ADD_COMMENT).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes,comment);
    }

    /**
     * Update comment result.
     *
     * @param comment the comment
     * @return the result
     */
    @Override
    public Result<Void> updateComment(Comment comment) {
        Outcomes outcomes;
        try {
            outcomes = execute(comment, String.format(Constants.UPDATE_COMMENT,
                    comment.getUserId(),comment.getPaper().getId(),comment.getContent(),comment.getId()), Constants.UPD_COMMENT).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets comment by id.
     *
     * @param id the id
     * @return the comment by id
     */
    @Override
    public Optional<Comment> getCommentById(long id) {
        try {
            ResultSet set = setResById(Comment.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                Comment comment= new Comment();
                comment.setId(set.getLong(Constants.COMMENT_ID));
                comment.setUserId(set.getLong(Constants.COMMENT_USER_ID));
                comment.setPaper(getSecurityPaperById(set.getLong(Constants.COMMENT_PAPER)).get());
                comment.setContent(set.getString(Constants.COMMENT_CONTENT));
                return Optional.of(comment);
            } else {
                return Optional.of(new Comment());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new Comment());
        }
    }

    /**
     * Delete comment result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteComment(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_COMMENT, id), Constants.DEL_COMMENT).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    /**
     * Insert exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    @Override
    public Result<ExchangeTradedFunction> insertExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction) {
        Outcomes outcomes;
        try {
            outcomes = execute(exchangeTradedFunction,String.format(
                    Constants.INSERT_ETF,
                    exchangeTradedFunction.getName(),
                    exchangeTradedFunction.getCost(),
                    exchangeTradedFunction.getDirection()),Constants.ADD_ETF).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes,exchangeTradedFunction);
    }

    /**
     * Update exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    @Override
    public Result<Void> updateExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction) {
        Outcomes outcomes;
        try {
            outcomes = execute(exchangeTradedFunction, String.format(Constants.UPDATE_ETF,
                    exchangeTradedFunction.getName(),exchangeTradedFunction.getCost(),exchangeTradedFunction.getDirection(),exchangeTradedFunction.getId()), Constants.UPD_ETF).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets exchange traded function by id.
     *
     * @param id the id
     * @return the exchange traded function by id
     */
    @Override
    public Optional<ExchangeTradedFunction> getExchangeTradedFunctionById(long id) {
        try {
            ResultSet set = setResById(ExchangeTradedFunction.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                ExchangeTradedFunction exchangeTradedFunction= new ExchangeTradedFunction();
                exchangeTradedFunction.setId(set.getLong(Constants.SECURITYPAPER_ID));
                exchangeTradedFunction.setName(set.getString(Constants.SECURITYPAPER_NAME));
                exchangeTradedFunction.setCost(set.getInt(Constants.SECURITYPAPER_COST));
                exchangeTradedFunction.setDirection(InvestmentDirection.valueOf(set.getString(Constants.ETF_DIRECTION)));
                return Optional.of(exchangeTradedFunction);
            } else {
                return Optional.of(new ExchangeTradedFunction());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new ExchangeTradedFunction());
        }
    }

    /**
     * Delete exchange traded function result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteExchangeTradedFunction(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_ETF, id), Constants.DEL_ETF).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    /**
     * Insert security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    @Override
    public Result<SecurityPaper> insertSecurityPaper(SecurityPaper securityPaper) {
        Outcomes outcomes;
        try {
            outcomes = execute(securityPaper,String.format(
                    Constants.INSERT_SECURITYPAPER,
                    securityPaper.getName(),
                    securityPaper.getCost()),Constants.ADD_SECURITYPAPER).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes,securityPaper);
    }

    /**
     * Update security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    @Override
    public Result<Void> updateSecurityPaper(SecurityPaper securityPaper) {
        Outcomes outcomes;
        try {
            outcomes = execute(securityPaper, String.format(Constants.UPDATE_SECURITYPAPER,
                    securityPaper.getName(),securityPaper.getCost(),securityPaper.getId()), Constants.UPD_SECURITYPAPER).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets security paper by id.
     *
     * @param id the id
     * @return the security paper by id
     */
    @Override
    public Optional<SecurityPaper> getSecurityPaperById(long id) {
        try {
            ResultSet set = setResById(SecurityPaper.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                SecurityPaper securityPaper= new SecurityPaper();
                securityPaper.setId(set.getLong(Constants.SECURITYPAPER_ID));
                securityPaper.setName(set.getString(Constants.SECURITYPAPER_NAME));
                securityPaper.setCost(set.getInt(Constants.SECURITYPAPER_COST));
                return Optional.of(securityPaper);
            } else {
                return Optional.of(new SecurityPaper());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new SecurityPaper());
        }
    }

    /**
     * Delete security paper result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteSecurityPaper(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_SECURITYPAPER, id), Constants.DEL_SECURITYPAPER).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    /**
     * Insert stock result.
     *
     * @param stock the stock
     * @return the result
     */
    @Override
    public Result<Stock> insertStock(Stock stock) {
        Outcomes outcomes;
        try {
            outcomes = execute(stock,String.format(
                    Constants.INSERT_STOCK,
                    stock.getName(),
                    stock.getCost(),
                    stock.getNormalValue(),
                    stock.getCategory()),Constants.ADD_STOCK).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes,stock);
    }

    /**
     * Update stock result.
     *
     * @param stock the stock
     * @return the result
     */
    @Override
    public Result<Void> updateStock(Stock stock) {
        Outcomes outcomes;
        try {
            outcomes = execute(stock, String.format(Constants.UPDATE_STOCK,
                    stock.getName(),
                    stock.getCost(),
                    stock.getNormalValue(),
                    stock.getCategory(),stock.getId()
            ), Constants.UPD_STOCK).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets stock.
     *
     * @param id the id
     * @return the stock
     */
    @Override
    public Optional<Stock> getStock(long id) {
        try {
            ResultSet set = setResById(Stock.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                Stock stock= new Stock();
                stock.setId(set.getLong(Constants.SECURITYPAPER_ID));
                stock.setName(set.getString(Constants.SECURITYPAPER_NAME));
                stock.setCost(set.getInt(Constants.SECURITYPAPER_COST));
                stock.setNormalValue(set.getInt(Constants.STOCK_NORMALVALUE));
                stock.setCategory(StockCategory.valueOf(set.getString(Constants.STOCK_CATEGORY)));
                return Optional.of(stock);
            } else {
                return Optional.of(new Stock());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new Stock());
        }
    }

    /**
     * Delete stock result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteStock(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_STOCK, id), Constants.DEL_STOCK).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    /**
     * Insert user result.
     *
     * @param user the user
     * @return the result
     */
    @Override
    public Result<User> insertUser(User user) {
        Outcomes outcomes;
        try {
            execute(user, String.format(
                    Constants.INSERT_USER,
                    user.getName()), Constants.ADD_USER);
            DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.getDefault());
            unusualSymbols.setDecimalSeparator('.');
            user.getFavorites().stream().forEach(el -> {
                try {
                    this.execute(null,
                            String.format(
                                    Constants.INSERT_USER_SECURITYPAPER,
                                    user.getId(), el.getId()
                            ), Constants.ADD_USER_SECURITYPAPER
                    );
                    log.debug("will add to db " + el);
                } catch (Exception e) {
                    log.error(e);
                    log.debug("insert fail " + el);
                }
            });
            outcomes = Outcomes.Complete;
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes, user);
    }

    /**
     * Update user result.
     *
     * @param user the user
     * @return the result
     */
    @Override
    public Result<Void> updateUser(User user) {
        Outcomes outcomes;
        try {
            outcomes = execute(user, String.format(Constants.UPDATE_USER,
                    user.getName(),user.getId()), Constants.UPD_USER).getStatus();
        } catch (Exception e) {
            log.error(e);
            outcomes = Outcomes.Fail;
        }
        return new Result<>(outcomes);
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    @Override
    public Optional<User> getUser(long id) {
        try {
            ResultSet set = setResById(User.class, id);
            log.debug(set);
            if (set != null && set.next()) {
                User user = new User();
                user.setId(set.getLong(Constants.USER_ID));
                user.setName(set.getString(Constants.USER_NAME));
                ResultSet setSPList = getFromUserSecurityPaper(id);
                List<SecurityPaper> securityPaperList = new ArrayList<>();
                if (setSPList != null && setSPList.next()) {
                    securityPaperList.add(getSecurityPaperById(setSPList.getInt(Constants.SECURITYPAPERID)).get());
                }
                user.setFavorites(securityPaperList);
                return Optional.of(user);
            } else {
                return Optional.of(new User());
            }
        } catch (SQLException e) {
            log.error(e);
            return Optional.of(new User());
        }
    }

    /**
     * Delete user result.
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<Void> deleteUser(long id) {
        try {
            boolean a = execute(null, String.format(Constants.DELETE_USER, id), Constants.DEL_USER).getData() == Outcomes.Complete;
            if (a) return new Result<>(Outcomes.Complete);
        } catch (Exception e) {
            log.error(e);
        }
        return new Result<>(Outcomes.Fail);
    }

    private <T> Result execute(T lst, String sql, String method) {
        Outcomes outcomes;
        try {
            if (lst == null) {
                lst = (T) "";
            }
            PreparedStatement statement = getConnection().prepareStatement(sql);// строка преобразуется в объект PS который потом
            statement.executeUpdate();                                          // выполняет команду отправки запроса executeUpdate
            getConnection().close();
            outcomes = Outcomes.Complete;
        } catch (SQLException e) {
            outcomes = Outcomes.Fail;
            log.error(e);
        }
        saveToLog(createHistoryContent(method, lst, outcomes));
        return new Result(outcomes);
    }

    private Connection getConnection() {
        try {
            connection = DriverManager.getConnection(
                    ConfigurationUtil.getConfigurationEntry(Constants.DB_CONNECT),
                    ConfigurationUtil.getConfigurationEntry(Constants.DB_USER),
                    ConfigurationUtil.getConfigurationEntry(Constants.DB_PASS)
            );
            return connection;
        } catch (SQLException | IOException e) {
            log.error(e);
            return null;
        }
    }

    private static HistoryContent createHistoryContent(String method, Object object, Outcomes outcomes) {
        return new HistoryContent(DataProviderJdbc.class.getSimpleName(), new Date(), Constants.DEFAULT_ACTOR, method, object, outcomes);
    }

    /**
     * Create tables.
     */
    public void createTables() {
        execute(null, String.format(Constants.CREATE_BOND), Constants.BOND_CREATE);
        execute(null, String.format(Constants.CREATE_COMMENT), Constants.COMMENT_CREATE);
        execute(null, String.format(Constants.CREATE_ETF), Constants.ETF_CREATE);
        execute(null, String.format(Constants.CREATE_SECURITYPAPER), Constants.SECURITY_PAPER_CREATE);
        execute(null, String.format(Constants.CREATE_STOCK), Constants.STOCK_CREATE);
        execute(null, String.format(Constants.CREATE_USER), Constants.USER_CREATE);
        execute(null, String.format(Constants.CREATE_USER_SECURITYPAPER), Constants.USER_SECURITYPAPER_CREATE);
    }

    /**
     * Delete record.
     *
     * @param <T> the type parameter
     * @param cl  the cl
     */
    public <T> void deleteRecord(Class<T> cl) {
        execute(null, String.format(Constants.DROP,
                cl.getSimpleName().toUpperCase()), Constants.DROP_NAME);
        new Result<>(Outcomes.Complete);
    }

    /**
     * Delete all record.
     */
    public void deleteAllRecord() {
        deleteRecord(SecurityPaper.class);
        deleteRecord(Bond.class);
        deleteRecord(Stock.class);
        deleteRecord(ExchangeTradedFunction.class);
        deleteRecord(Comment.class);
        deleteRecord(User.class);
        execute(null, String.format(Constants.USER_SECURITYPAPER), Constants.USER_SECURITYPAPER_DELETE);
    }

    private ResultSet setResById(Class cl, long id) {
        ResultSet set = getRecords(String.format(Constants.SELECT_ALL,
                cl.getSimpleName().toLowerCase(), id));
        return set;
    }

    private ResultSet getRecords(String sql) {
        log.info(sql);
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            getConnection().close();
            return statement.executeQuery();//select
        } catch (SQLException e) {
            log.info(e);
        }
        return null;
    }

    private ResultSet getFromUserSecurityPaper(long id){
        return getRecords(String.format(Constants.GETUSERSECURITYPAPER,id));
    }

    /**
     * Gets list of security paper.
     *
     * @return the list of security paper
     */
    public List<SecurityPaper> getListOfSecurityPaper () {
        List<SecurityPaper> securityPaperList = new ArrayList<>();
        ResultSet set = getRecords(String.format(Constants.DB_SELECT_ALL_SECURITY_PAPER));
        try {
            while (set.next()) {
                SecurityPaper securityPaper = getSecurityPaperById(set.getLong(Constants.SECURITYPAPER_ID)).get();
                securityPaperList.add(securityPaper);
            }
        }catch (SQLException e){
            log.error(e);
        }
        return securityPaperList;
    }

    /**
     * Gets list of bond.
     *
     * @return the list of bond
     */
    public List<Bond> getListOfBond() {
        List<Bond> bondList = new ArrayList<>();
        ResultSet set = getRecords(String.format(Constants.DB_SELECT_ALL_BOND));
        try {
            while (set.next()) {
                Bond bond = getBondById(set.getLong(Constants.SECURITYPAPER_ID)).get();
                bondList.add(bond);
            }
        }catch (SQLException e){
            log.error(e);
        }
        return bondList;
    }

    /**
     * Gets list of stock.
     *
     * @return the list of stock
     */
    public List<Stock> getListOfStock () {
        List<Stock> stockList = new ArrayList<>();
        ResultSet set = getRecords(String.format(Constants.DB_SELECT_ALL_STOCK));
        try {
            while (set.next()) {
                Stock stock = getStock(set.getLong(Constants.SECURITYPAPER_ID)).get();
                stockList.add(stock);
            }
        }catch (SQLException e){
            log.error(e);
        }
        return stockList;
    }

    /**
     * Gets list of etf.
     *
     * @return the list of etf
     */
    public List<ExchangeTradedFunction> getListOfETF () {
        List<ExchangeTradedFunction> etfList = new ArrayList<>();
        ResultSet set = getRecords(String.format(Constants.DB_SELECT_ALL_ETF));
        try {
            while (set.next()) {
                ExchangeTradedFunction etf = getExchangeTradedFunctionById(set.getLong(Constants.SECURITYPAPER_ID)).get();
                etfList.add(etf);
            }
        }catch (SQLException e){
            log.error(e);
        }
        return etfList;
    }


}

