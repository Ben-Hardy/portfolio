/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

//import static Controller.actions.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.Optional;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.dialog.*;

/**
 * It is old design that saved as backup and should not be used.
 * @author Hao
 */
public class Toolbar extends HBox{
    
    Button font;
    
    NewMenubar menubar;
    
    //for using it, example: Color color = colorPicker.getValue();
    ColorPicker colorPicker;
    
    private void initializeFontButton() {
        font = new Button("Set Font");
        font.setFont(Font.font("Times New Roman"));
        font.setMinHeight(30);
        font.setMaxHeight(30);

        font.setOnAction(e -> {
            Optional<Font> response = Dialogs.create()
                .owner(null)
                .masthead("Choose what you like")
                .showFontSelector(font.getFont());
            font.setFont(response.get());
        });
    }
    
    public Toolbar () {
        super();
        
        this.setHeight(30);
        
        menubar = new NewMenubar();
        
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.WHITE);
        colorPicker.setMinHeight(30);
        colorPicker.setMaxHeight(30);
        
        initializeFontButton();
        this.getChildren().addAll(menubar, font, colorPicker);
    }
}
