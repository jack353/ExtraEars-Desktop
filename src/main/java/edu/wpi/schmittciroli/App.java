package edu.wpi.schmittciroli;

import edu.wpi.schmittciroli.controllers.mainScreenController;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

@Slf4j
public class App extends Application {
  private Stage primaryStage;
  private static App instance;
  public static App getInstance(){
    return instance;
  }
  private Server_v2 server = new Server_v2();

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Thread servThread = new Thread(server);
    servThread.start();

    instance = this;
    this.primaryStage = primaryStage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mainScreen.fxml"));
    Parent root = loader.load();

    // Set the server instance on the controller
    mainScreenController controller = loader.getController();
    controller.setServer(server);
    server.setMainScreenController(controller);

    Scene scene = new Scene(root);
    scene.getStylesheets().add("edu/wpi/schmittciroli/styles/mainScreenStyle.css");
    primaryStage.setScene(scene);
    primaryStage.show();

    //Closes all threads and stops server when app is closed.
    primaryStage.setOnCloseRequest(e -> {
      server.stop();
      stop();
    });
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
