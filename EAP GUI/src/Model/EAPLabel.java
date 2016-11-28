/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class EAPLabel implements EAPWidget{
    double x;
    double y;
    String txt;
    Font font;
    private Label tempLabel;
    String type;

    public EAPLabel(double x, double y, String txt, Font font) {
        this.type = "Label";
        this.x=x;
        this.y=y;
        this.txt=txt;
        this.font=font;
    }

    public void setX(double x) {
        this.x = x;
        tempLabel.setLayoutX(x);
    }
    
    public void setY(double y) {
        this.y = y;
        tempLabel.setLayoutY(y);
    }
    
    @Override
    public void paint(Pane p) {
        tempLabel = new Label("Label ");
        tempLabel.setLayoutX(x);
        tempLabel.setLayoutY(y);
        tempLabel.setFont(font);
        
        if (!txt.isEmpty()) {
            tempLabel.setText(txt);
        }
                        
        p.getChildren().add(tempLabel);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(tempLabel);
    }
    
    //TO DO
     @Override
    public boolean contains(double x, double y) {
        return x > this.x && x < this.x + 30 && y > this.y && y < this.y + 20;
    }
    
    @Override
    public String toString() {
        return ("EAPLabel " 
                + this.x + " "
                + this.y + " "
                + this.txt + " "
                + this.font.getSize() + "\n"
                + this.font.getName());
    }
    
    @Override
    public String getWidgetType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
