package ru.sfedu.securityservice;

public class Constants {
    public static final String ENV_PROP_KEY = "environment";
//    public static final String ENV_PROP_VALUE = "src/main/resources/environment.properties";
    public static final String ENV_PROP_VALUE = "environment.properties";
    public static final String DEFAULT_ACTOR = "system";

    public static final String MONGO_CONNECT = "mongodb://127.0.0.1:27017";
    public static final String MONGO_DATABASE = "securityService";
    public static final String MONGO_COLLECTION = "historyLog";

    public static final String BOND = "BOND_CSV";
    public static final String COMMENT = "COMMENT_CSV";
    public static final String ETF_CSV = "EXCHANGE_TRADED_FUNCTION_CSV";
    public static final String SECURITY = "SECURITY_PAPER_CSV";
    public static final String STOCK = "STOCK_CSV";
    public static final String USER = "USER_CSV";

    public static final String BOND_XML = "BOND_XML";
    public static final String COMMENT_XML = "COMMENT_XML";
    public static final String ETF_XML = "EXCHANGE_TRADED_FUNCTION_XML";
    public static final String SECURITY_XML = "SECURITY_PAPER_XML";
    public static final String STOCK_XML = "STOCK_XML";
    public static final String USER_XML = "USER_XML";

    public static final String ID_NOT_EXISTS = "Object[%d] is not exists";
    public static final String SELECT_ALL = "SELECT * FROM %s WHERE Id=%d";
    public static final String DB_CONNECT = "db_url";
    public static final String DB_USER = "db_user";
    public static final String DB_PASS = "db_pass";


    public static final String ADD_SECURITYPAPER = "Add securitypaper";
    public static final String INSERT_SECURITYPAPER = "INSERT INTO \"SECURITYPAPER\" (name,cost) VALUES ('%s', %d);";

    public static final String DEL_SECURITYPAPER = "Delete securitypaper";
    public static final String DELETE_SECURITYPAPER = "DELETE FROM \"SECURITYPAPER\" WHERE Id=%d;";

    public static final String UPD_SECURITYPAPER= "UPDATE securitypaper";
    public static final String UPDATE_SECURITYPAPER = "UPDATE \"SECURITYPAPER\" SET name = '%s',cost = %d WHERE id=%d;";

    public static final String SECURITYPAPER_ID = "id";
    public static final String SECURITYPAPER_NAME = "name";
    public static final String SECURITYPAPER_COST = "cost";


    public static final String ADD_BOND = "Add bond";
    public static final String INSERT_BOND = "INSERT INTO \"BOND\" (name,cost,nominalValue,type) VALUES ('%s', %d,%d,'%s');";

    public static final String DEL_BOND = "Delete bond";
    public static final String DELETE_BOND = "DELETE FROM \"BOND\" WHERE Id=%d;";

    public static final String UPD_BOND= "UPDATE bond";
    public static final String UPDATE_BOND = "UPDATE \"BOND\" SET name = '%s',cost = %d,nominalValue = %d, type = '%s' WHERE id=%d;";

    public static final String BOND_VALUE = "nominalValue";
    public static final String BOND_TYPE = "type";


    public static final String ADD_ETF = "Add etf";
    public static final String INSERT_ETF = "INSERT INTO \"EXCHANGETRADEDFUNCTION\" (name,cost,direction) VALUES ('%s', %d,'%s');";

    public static final String DEL_ETF = "Delete etf";
    public static final String DELETE_ETF = "DELETE FROM \"EXCHANGETRADEDFUNCTION\" WHERE Id=%d;";

    public static final String UPD_ETF= "UPDATE etf";
    public static final String UPDATE_ETF = "UPDATE \"EXCHANGETRADEDFUNCTION\" SET name = '%s',cost = %d,direction = '%s' WHERE id=%d;";

    public static final String ETF_DIRECTION = "direction";

    public static final String ADD_COMMENT = "Add comment";
    public static final String INSERT_COMMENT = "INSERT INTO \"COMMENT\" (userId,paper,content) VALUES (%d,%d,'%s');";

    public static final String DEL_COMMENT = "Delete comment";
    public static final String DELETE_COMMENT = "DELETE FROM \"COMMENT\" WHERE Id=%d;";

    public static final String UPD_COMMENT= "UPDATE comment";
    public static final String UPDATE_COMMENT = "UPDATE \"COMMENT\" SET userId = %d,paper = %d,content= '%s' WHERE id=%d;";

    public static final String COMMENT_ID = "id";
    public static final String COMMENT_USER_ID = "userId";
    public static final String COMMENT_PAPER = "paper";
    public static final String COMMENT_CONTENT = "content";


    public static final String ADD_STOCK = "Add stock";
    public static final String INSERT_STOCK = "INSERT INTO \"STOCK\" (name,cost,normalValue,category) VALUES ('%s', %d,%d,'%s');";

    public static final String DEL_STOCK = "Delete stock";
    public static final String DELETE_STOCK = "DELETE FROM \"STOCK\" WHERE Id=%d;";

    public static final String UPD_STOCK= "UPDATE stock";
    public static final String UPDATE_STOCK = "UPDATE \"STOCK\" SET name = '%s',cost = %d,normalValue = %d, category = '%s', WHERE id=%d;";

    public static final String STOCK_NORMALVALUE = "normalValue";
    public static final String STOCK_CATEGORY = "category";


    public static final String ADD_USER = "Add user";
    public static final String INSERT_USER = "INSERT INTO \"USER\" (name) VALUES ('%s');";

    public static final String DEL_USER = "Delete user";
    public static final String DELETE_USER = "DELETE FROM \"USER\" WHERE Id=%d;";

    public static final String UPD_USER= "UPDATE user";
    public static final String UPDATE_USER = "UPDATE \"USER\" SET name = '%s' WHERE id=%d;";

    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";

    public static final String GETUSERSECURITYPAPER = "SELECT * FROM USERSECURITYPAPER WHERE USERID=%d";
    public static final String USER_SECURITYPAPER = "DROP TABLE IF EXISTS USERSECURITYPAPER CASCADE";
    public static final String SECURITYPAPERID = "SECURITYPAPERID";
    public static final String USER_SECURITYPAPER_DELETE = "delete securty paper";
    public static final String INSERT_USER_SECURITYPAPER = "INSERT INTO USERSECURITYPAPER(userid,securitypaperid) VALUES (%d, %d);";
    public static final String ADD_USER_SECURITYPAPER = "add user securitypaper";

    public static final String DROP ="DROP TABLE IF EXISTS \"%s\" CASCADE";

    public static final String DROP_NAME="Drop Table";
    public static final String BOND_CREATE = "bond create table";
    public static final String CREATE_BOND ="create table BOND\n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    NAME           VARCHAR(40),\n" +
            "    COST           INTEGER,\n" +
            "    NOMINALVALUE   INTEGER,\n" +
            "    TYPE           VARCHAR(40) \n"  +
            ");";
    public static final String COMMENT_CREATE = "comment create table";
    public static final String CREATE_COMMENT = "create table COMMENT\n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    userId         INTEGER,\n" +
            "    paper          INTEGER,\n" +
            "    content        VARCHAR(40)\n" +
            ");";
    public static final String ETF_CREATE = "etf create table";
    public static final String CREATE_ETF = "create table EXCHANGETRADEDFUNCTION\n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    NAME           VARCHAR(40),\n" +
            "    COST           INTEGER,\n" +
            "    DIRECTION      VARCHAR(40)\n" +
            ");";
    public static final String SECURITY_PAPER_CREATE = "securitypaper create table";
    public static final String CREATE_SECURITYPAPER = "create table SECURITYPAPER \n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    NAME           VARCHAR(40),\n" +
            "    COST           INTEGER\n" +
            ");";
    public static final String STOCK_CREATE = "stock create table";
    public static final String CREATE_STOCK = "create table STOCK \n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    NAME           VARCHAR(40),\n" +
            "    COST           INTEGER,\n" +
            "    NORMALVALUE    INTEGER,\n" +
            "    CATEGORY       VARCHAR(40)\n" +
            ");";
    public static final String USER_CREATE = "user create table";
    public static final String CREATE_USER = "create table USER \n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    NAME           VARCHAR(40)\n" +
            ");";
    public static final String USER_SECURITYPAPER_CREATE = "create connect table";
    public static final String CREATE_USER_SECURITYPAPER = "create table USERSECURITYPAPER \n" +
            "(\n" +
            "    ID BIGINT auto_increment primary key,\n" +
            "    USERID           INTEGER,\n" +
            "    SECURITYPAPERID  INTEGER\n" +
            ");";

    public static final String DB_SELECT_ALL_SECURITY_PAPER="SELECT * FROM SECURITYPAPER";
    public static final String DB_SELECT_ALL_BOND="SELECT * FROM BOND";
    public static final String DB_SELECT_ALL_STOCK="SELECT * FROM STOCK";
    public static final String DB_SELECT_ALL_ETF="SELECT * FROM EXCHANGETRADEDFUNCTION";

}
