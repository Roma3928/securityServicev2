package ru.sfedu.securityservice.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import ru.sfedu.securityservice.Result;
import ru.sfedu.securityservice.api.*;
import ru.sfedu.securityservice.models.enums.Outcomes;

@Command (name = "securityController", description = "Find security paper")
public class SecurityController implements Runnable {

    @Parameters(index = "0", paramLabel = "Provider", description = "CSV, XML or JDBC")
    private DataProviderType dp;
    @Parameters(index = "1", paramLabel = "PaperName", description = "Name of security paper need to found out")
    private String paperName;
    @Option(names = "-e", paramLabel = "ExtendMethod", description = "Call of extend method in format:" +
            " command:userId\nExample:ADDFAV:1488 or DELFAV:228")
    private String command;

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
        String[] com = " :0".split(":");
        if (command!=null) com = command.split(":");
        Result res = dataProvider.securityController(paperName, com[0], Long.parseLong(com[1]));
        if (res.getStatus()==Outcomes.Complete) {
            System.out.println("Operation complete!");
            System.out.println(res.getData());
        }
        else System.out.println("Error! Please, enter correct parameters.");
    }
}
