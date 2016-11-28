/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * contain commands that may be frequently used
 * @author Hao
 */
public class actions {
    
    /**
     * provide deepclone
     * @param <T> the class of source
     * @param source the object need to be deepcloned
     * @return a deepclone of source 
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public static <T> T cloneTo(T source) throws RuntimeException, IOException, ClassNotFoundException {
        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
        ObjectOutputStream oOutStream = new ObjectOutputStream(bOutStream);
        oOutStream.writeObject(source);
        ByteArrayInputStream bInStream = new ByteArrayInputStream(bOutStream.toByteArray());
        ObjectInputStream oInputStream = new ObjectInputStream(bInStream);
        return (T) oInputStream.readObject();
    }
    
    /**
     * overwrite the file with given string
     * @param content - the string contains information that needed to be written
     * @param file - the file to be overwritten
     */
    public static void saveFile(String content, File file){
        try {
            try (FileWriter fileWriter = new FileWriter(file,false)) {
                fileWriter.write(content);
                fileWriter.close();
            }
        } catch (IOException ex) {
            
        }
    }
    
    /**
     * Creates a pop up window and prompts the user for input for  the text for a label
     * @param type the widget type that the user is being prompted about
     * @param copyStage simply used so that we can make our text prompts be separated 
     * @return a string that either is containing the text the user entered or nothing
     * if the user opted to not enter anything
     */
    public static String promptInput(String type, Stage copyStage){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(copyStage);
        VBox vbox = new VBox(10);
        vbox.getChildren().add(new Label("Enter " + type + " Text"));
        TextField tf = new TextField();
        tf.setMaxWidth(150);
        vbox.getChildren().add(tf);
        Button b = new Button("OK");
             
        b.setOnAction((ActionEvent event1) -> {
            if (tf.getText().isEmpty())
                System.out.println("Will go with default label");
            dialog.close();
            
        });
        vbox.getChildren().add(b);
        vbox.alignmentProperty().setValue(Pos.CENTER);
        Scene dialogScene = new Scene(vbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
        
        return tf.getText(); 
    }
}
