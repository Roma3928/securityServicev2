package ru.sfedu.securityservice;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import ru.sfedu.securityservice.api.*;
import ru.sfedu.securityservice.cli.*;
import ru.sfedu.securityservice.utils.ConfigurationUtil;

import java.io.IOException;
import java.util.concurrent.Callable;

@Command(
        description = "Security papers application",
        subcommands = {
                SecurityController.class,
                CalculatePaperCost.class,
                CommandLine.HelpCommand.class
        }
)
public class Client implements Callable<Integer> {
    public static void main(String[] args) {
        java.util.logging.LogManager.getLogManager().reset();
        System.out.println("Initializing...\n");
        DataProvider dataProvider;
        try {
            dataProvider = new DataProviderCsv();
        } catch (Exception e){
            System.out.println("Error to init CSV data provider!\n");
        }
        try {
            dataProvider = new DataProviderJdbc();
        } catch (Exception e){
            System.out.println("Error to init JDBC data provider!\n");
        }
        try {
            dataProvider = new DataProviderXML();
        } catch (Exception e){
            System.out.println("Error to init XML data provider!\n");
        }
        try{
            ConfigurationUtil.getConfigurationEntry(Constants.ETF_CSV);
        } catch (IOException e) {
            System.out.println("Error to find environment.properties!\n");
        }
        int exitCode = new CommandLine(new Client()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        System.err.println("Please, use subcommands!");
        CommandLine.usage(this, System.out);
        return 0;
    }

    public Client(){
    }
}