package ru.sfedu.securityservice.api;


import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.models.*;

import java.util.Optional;

/**
 * The interface Data provider.
 */
public interface DataProvider {


    /**
     * Add to favorites result.
     *
     * @param securityPaper the security paper
     * @param userId        the user id
     * @return the result
     */
    Result<Boolean> addToFavorites(SecurityPaper securityPaper, long userId);

    /**
     * Delete from favorites result.
     *
     * @param securityPaper the security paper
     * @param userId        the user id
     * @return the result
     */
    Result<Boolean> deleteFromFavorites(SecurityPaper securityPaper, long userId);

    /**
     * Security controller result.
     *
     * @param paperName the paper name
     * @param command   the command
     * @param userId    the user id
     * @return the result
     */
    Result<SecurityPaper> securityController(String paperName, String command, long userId);

    /**
     * Calculate all paper cost result.
     *
     * @param securityPaperType the security paper type
     * @return the result
     */
    Result<Integer> calculateAllPaperCost( String securityPaperType);

    /**
     * Calculate bond result .
     *
     * @return the result
     */
    Result <Integer> calculateBond();

    /**
     * Calculate stock result .
     *
     * @return the result
     */
    Result <Integer> calculateStock();

    /**
     * Calculate etf result .
     *
     * @return the result
     */
    Result <Integer> calculateETF();

    /**
     * Insert bond result.
     *
     * @param bond the bond
     * @return the result
     */
    Result<Bond> insertBond(Bond bond);

    /**
     * Update bond result.
     *
     * @param bond the bond
     * @return the result
     */
    Result<Void> updateBond(Bond bond);

    /**
     * Gets bond by id.
     *
     * @param id the id
     * @return the bond by id
     */
    Optional<Bond> getBondById(long id);

    /**
     * Delete bond result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteBond(long id);

    /**
     * Insert comment result.
     *
     * @param comment the comment
     * @return the result
     */
    Result<Comment> insertComment(Comment comment);

    /**
     * Update comment result.
     *
     * @param comment the comment
     * @return the result
     */
    Result<Void> updateComment(Comment comment);

    /**
     * Gets comment by id.
     *
     * @param id the id
     * @return the comment by id
     */
    Optional<Comment> getCommentById(long id);

    /**
     * Delete comment result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteComment(long id);

    /**
     * Insert exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    Result<ExchangeTradedFunction> insertExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction);

    /**
     * Update exchange traded function result.
     *
     * @param exchangeTradedFunction the exchange traded function
     * @return the result
     */
    Result<Void> updateExchangeTradedFunction(ExchangeTradedFunction exchangeTradedFunction);

    /**
     * Gets exchange traded function by id.
     *
     * @param id the id
     * @return the exchange traded function by id
     */
    Optional<ExchangeTradedFunction> getExchangeTradedFunctionById(long id);

    /**
     * Delete exchange traded function result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteExchangeTradedFunction(long id);

    /**
     * Insert security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    Result<SecurityPaper> insertSecurityPaper(SecurityPaper securityPaper);

    /**
     * Update security paper result.
     *
     * @param securityPaper the security paper
     * @return the result
     */
    Result<Void> updateSecurityPaper(SecurityPaper securityPaper);

    /**
     * Gets security paper by id.
     *
     * @param id the id
     * @return the security paper by id
     */
    Optional<SecurityPaper> getSecurityPaperById(long id);

    /**
     * Delete security paper result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteSecurityPaper(long id);

    /**
     * Insert stock result.
     *
     * @param stock the stock
     * @return the result
     */
    Result<Stock> insertStock(Stock stock);

    /**
     * Update stock result.
     *
     * @param stock the stock
     * @return the result
     */
    Result<Void> updateStock(Stock stock);

    /**
     * Gets stock.
     *
     * @param id the id
     * @return the stock
     */
    Optional<Stock> getStock(long id);

    /**
     * Delete stock result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteStock(long id);

    /**
     * Insert user result.
     *
     * @param user the user
     * @return the result
     */
    Result<User> insertUser(User user);

    /**
     * Update user result.
     *
     * @param user the user
     * @return the result
     */
    Result<Void> updateUser(User user);

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    Optional<User> getUser(long id);

    /**
     * Delete user result.
     *
     * @param id the id
     * @return the result
     */
    Result<Void> deleteUser(long id);
}
