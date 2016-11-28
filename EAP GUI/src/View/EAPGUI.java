/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Hao
 */
public class EAPGUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Toolbar toolbar = new Toolbar();
        AnchorPane mainPane = new AnchorPane();
        WorkPane workPane = new WorkPane(primaryStage, toolbar);
        
        AnchorPane.setTopAnchor(toolbar, 0.0);
        AnchorPane.setLeftAnchor(toolbar, 0.0);
        AnchorPane.setRightAnchor(toolbar, 0.0);
        
        AnchorPane.setTopAnchor(workPane, 30.0);
        AnchorPane.setLeftAnchor(workPane, 0.0);
        AnchorPane.setRightAnchor(workPane, 0.0);
        AnchorPane.setBottomAnchor(workPane, 0.0);
        
        mainPane.getChildren().addAll(toolbar, workPane);
        Scene scene = new Scene(mainPane, 800,600);
        primaryStage.setTitle("Easy As Paint GUI Design Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
