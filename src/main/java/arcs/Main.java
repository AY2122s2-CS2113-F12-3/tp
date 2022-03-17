package arcs;

import arcs.commands.Command;
import arcs.commands.CommandResult;
import arcs.data.RouteManager;
import arcs.parser.Parser;
import arcs.storage.RouteFileManager;
import arcs.ui.Ui;

import java.io.IOException;


public class Main {
    private Ui ui;
    private RouteManager routeManager;
    private Parser parser;
    private RouteFileManager routeFileManager;

    /**
     * Main entry-point for the ARCS application.
     */
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        ui = new Ui();
        parser = new Parser();
        loadData();
        Command command;
        ui.showWelcomeMessage();


        do {
            ui.showDivider();
            String userCommandText = ui.getUserCommand();
            command = parser.parseCommand(userCommandText);
            command.setData(routeManager);
            CommandResult result = command.execute();
            ui.showResultToUser(result);
        } while (!command.isExit());

        saveData();
        ui.showExitMessage();
    }

    public void loadData() {
        routeFileManager = new RouteFileManager();
        try {
            routeManager = new RouteManager(routeFileManager.loadData());
        } catch (IOException e) {
            ui.showToUser(e.getMessage());
            routeManager = new RouteManager();
        }
    }

    public void saveData() {
        try {
            routeFileManager.saveData(routeManager.getAllRoutes());
        } catch (IOException e) {
            ui.showToUser(e.getMessage());
        }
    }
}