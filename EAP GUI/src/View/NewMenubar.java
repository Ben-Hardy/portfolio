/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import static Controller.actions.saveFile;
import Model.EAPWidget;
import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author Hao
 */
public class NewMenubar extends MenuBar{
    
    Menu file, support, edit;
    MenuItem newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, exportMenuItem,printMenuItem,exitMenuItem;
    MenuItem undoMenuItem, redoMenuItem, cutMenuItem, copyMenuItem, pasteMenuItem, deleteMenuItem, clearMenuItem;
    MenuItem formatMenuItem,developerMenuItem, oracleMenuItem;
    
    // a list of all the widgets added to the scene
    ArrayList<EAPWidget> widgets;
    
    // keep the panes separate. We want widgets to have priority over panes
    ArrayList<EAPWidget> panes;
    
    //used for I/O, since a variable used by lambda can not be a local variable
    String savedInformation;
    
    File workfile;
    
    private void initializeFile() {
        file = new Menu("File");
        
        newMenuItem = new MenuItem("New Project");
        openMenuItem = new MenuItem("Open Project");
        
        saveMenuItem = new MenuItem("Save Project");
        saveMenuItem.setOnAction( (ActionEvent event) -> {
            savedInformation="";
            for (int i = panes.size() -1; i > -1; i--)
                savedInformation += panes.get(i).toString() + "\n";
            for (int i = widgets.size() -1; i > -1; i--)
                savedInformation += widgets.get(i).toString() + "\n";
            if (workfile==null){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Project As");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("EAP GUI Project", "*.eap")
                );
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                workfile = fileChooser.showSaveDialog(null);
            }
            if (workfile != null) 
                saveFile(savedInformation, workfile);
        });
        
        saveAsMenuItem = new MenuItem("Save As");
        saveAsMenuItem.setOnAction( (ActionEvent event) -> {
            savedInformation="";
            for (int i = panes.size() -1; i > -1; i--)
                savedInformation += panes.get(i).toString() + "\n";
            for (int i = widgets.size() -1; i > -1; i--)
                savedInformation += widgets.get(i).toString() + "\n";
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save The Project As");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("EAP GUI Project", "*.eap")
            );
            
            workfile = fileChooser.showSaveDialog(null);
            saveFile(savedInformation,workfile);
        });
        
        exportMenuItem = new MenuItem("Export As Image");
        
        printMenuItem = new MenuItem("Print");
        printMenuItem.setOnAction( (ActionEvent event) -> {
            PrinterJob print = PrinterJob.getPrinterJob();
            if (print != null && workfile != null) {
                if (print.printDialog()) {
                    try {
                        print.print((PrintRequestAttributeSet) workfile);
                    } catch (PrinterException ex) {
                        Logger.getLogger(NewMenubar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setMnemonicParsing(true);
        //we can set hotkey if we want, this is an halfway example. No hotkey has been related to the function
        exitMenuItem.setAccelerator(KeyCombination.NO_MATCH);
        exitMenuItem.setOnAction( (ActionEvent event) -> {
            Platform.exit();
        });
        
        file.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, exportMenuItem,printMenuItem,exitMenuItem);
    }
    
    private void initializeEdit(){
        edit = new Menu("Edit");
        
        undoMenuItem = new MenuItem("Undo");
        redoMenuItem = new MenuItem("Redo");
        redoMenuItem.setDisable(true);
        cutMenuItem = new MenuItem("Cut");
        copyMenuItem = new MenuItem("Copy");
        pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setDisable(true);
        deleteMenuItem = new MenuItem("Delete");
        clearMenuItem = new MenuItem("Clear");
        
        edit.getItems().addAll(undoMenuItem, redoMenuItem, cutMenuItem, copyMenuItem, pasteMenuItem, deleteMenuItem,clearMenuItem);
        
    }
    
    private void initializeSupport() {
        support = new Menu("Support");
        
        developerMenuItem = new MenuItem("About Developers");
        developerMenuItem.setOnAction( (ActionEvent event) -> {
           Stage stage = new Stage();
           Scene dialog = new Scene(new Group(new Text(
                     "\nEasy As Paint GUI Design Editor \n \n"
                    + "This application is powered by JavaFX \n"
                    + "By group: \n \n"
                    + "  Ben Hardy  BBH219 \n"
                    + "  Hao Li     Hal215 \n")));
           dialog.setFill(Color.GAINSBORO);
           stage.setScene(dialog);
           stage.show();
        });
        
        oracleMenuItem = new MenuItem("Oracle JavaFX Guide");
        oracleMenuItem.setOnAction((ActionEvent event) -> {
           try {
               Desktop.getDesktop().browse(new URI("http://docs.oracle.com/javase/8/javase-clienttechnologies.htm"));
               //URI of overview-summary: docs.oracle.com/javafx/2/api/overview-summary/html
           } catch (IOException | URISyntaxException e) {
           }
        });
        
        formatMenuItem = new MenuItem("Saved File Format");
        formatMenuItem.setOnAction((ActionEvent event) -> {
            //since the format explanation may be too long, read from file
            StringBuilder content = new StringBuilder();
            TextArea textArea = new TextArea();
            
            InputStream inputStream = getClass().getResourceAsStream("format.txt");
            
            if (inputStream == null) {
                System.out.println("input stream is null");
            } else {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                try  {
                    String newline;
                    while ((newline = bufferedReader.readLine()) != null) {
                        content.append(newline).append("\n");
                    } 
                } catch (IOException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
            textArea.appendText(content.toString());
            textArea.setEditable(false);
            
            Stage stage = new Stage();
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            textArea.prefHeightProperty().bind(stage.heightProperty());
            textArea.prefWidthProperty().bind(stage.widthProperty());
            Scene dialog = new Scene(new Group(textArea));
            dialog.setFill(Color.GAINSBORO);
            stage.setScene(dialog);
            stage.show();
        });
        
        support.getItems().addAll(formatMenuItem,developerMenuItem, oracleMenuItem);
    }
    
    public NewMenubar () {
        super();
        this.setMaxHeight(30);
        this.setMinHeight(30);
        this.setPadding(new Insets(0,0,0,0));
        
        initializeFile();
        initializeSupport();
        initializeEdit();
        
        workfile=null;
        widgets = new ArrayList<>();
        panes = new ArrayList<>();
        
        this.getMenus().addAll(file,edit,support);
    }
    
}
