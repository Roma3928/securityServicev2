package ru.sfedu.securityservice.utils;

import ru.sfedu.securityservice.models.enums.BondType;
import ru.sfedu.securityservice.models.enums.InvestmentDirection;
import ru.sfedu.securityservice.models.enums.StockCategory;

public class DataForGenerator {

    public static String[] name = {"Eugen","Victor","Lisa","Valeriy","Anastasia","Slavik","Sergay","Veniamin","Olga","Anatoly"};
    public static Integer[] price = {8000,7000,300,9000,1000,6000,7000,8500,9700,10000};
    public static Integer[] nominalPrice = {18000,17000,1300,19000,11000,16000,17000,18500,19700,110000};
    public static BondType[] bondTypes = { BondType.Government,BondType.Municipal,BondType.Corporation,BondType.Government,BondType.Municipal,BondType.Corporation,BondType.Government,BondType.Municipal,BondType.Corporation,BondType.Corporation};
    public static StockCategory[] stockCategories = {StockCategory.COMMON,StockCategory.PREFERENCE, StockCategory.COMMON,StockCategory.PREFERENCE,StockCategory.COMMON,StockCategory.PREFERENCE,StockCategory.COMMON,StockCategory.PREFERENCE,StockCategory.COMMON,StockCategory.PREFERENCE};
    public static InvestmentDirection[] investmentDirection = {InvestmentDirection.Stock,InvestmentDirection.Bond,InvestmentDirection.Meta,InvestmentDirection.Stock,InvestmentDirection.Bond,InvestmentDirection.Meta,InvestmentDirection.Stock,InvestmentDirection.Bond,InvestmentDirection.Meta,InvestmentDirection.Bond};
    public static String[] comment = {"Bad service","Bond perfect","Illegal service","Secure is vell","Goverment paper","Analytics","Yellow banana","Mops","Bumajka","Java"};
}
