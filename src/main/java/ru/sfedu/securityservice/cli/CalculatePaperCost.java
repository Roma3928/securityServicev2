package ru.sfedu.securityservice.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.api.*;
import ru.sfedu.securityservice.models.enums.Outcomes;

@Command (name = "calculatePaperCost", description = "Calculate cost of security paper")
public class CalculatePaperCost implements Runnable {

    @Parameters(index = "0", paramLabel = "Provider", description = "CSV, XML or JDBC")
    private DataProviderType dp;
    @Option(names = "-t", paramLabel = "SecurityPaperType", description = "Type of security paper need to calculate")
    private String securityPaperType;

    @Override
    public void run() {
        DataProvider dataProvider=null;
        switch (dp){
            case CSV:
                dataProvider = new DataProviderCsv();
                break;
            case XML:
                dataProvider = new DataProviderXML();
                break;
            case JDBC:
                dataProvider = new DataProviderJdbc();
                break;
        }
        String tp="";
        if (securityPaperType!=null) tp = securityPaperType;
        Result res = dataProvider.calculateAllPaperCost(tp);
        if (res.getStatus()==Outcomes.Complete) {
            System.out.println("Operation complete!");
            System.out.println(res.getData());
        }
        else System.out.println("Error! Please, enter correct parameters.");
    }
}
