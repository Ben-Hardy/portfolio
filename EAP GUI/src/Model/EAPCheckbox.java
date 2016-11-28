/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Hao
 */
public class EAPCheckbox implements EAPWidget{
    double x;
    double y;
    String color;
    String txt;
    Font font;
    private Rectangle temp;
    Label tempLabel;
    String type;
    
    public EAPCheckbox(double x, double y, String color, String txt, Font font) {
        this.type = "Check Box";
        this.x=x;
        this.y=y;
        this.color=color;
        this.txt=txt;
        this.font=font;
    }
    
    @Override
    public void paint(Pane p) {
        temp = new Rectangle(x, y, 15, 15);
        temp.setStyle("-fx-fill: " + color + "; -fx-stroke: #000000; -fx-stroke-width:2;");
        p.getChildren().add(temp);
        tempLabel = new Label("Label");
        tempLabel.setFont(font);
         
        if (!txt.isEmpty()) {
            tempLabel.setText(txt);
        }
                        
        tempLabel.setLayoutX(x+ 20);
        tempLabel.setLayoutY(y - 2);
        p.getChildren().add(tempLabel);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(temp,tempLabel);
    }
    
     @Override
    public boolean contains(double x, double y) {
        return temp.contains(x,y);
    }
    
    @Override
    public String toString() {
        return ("EAPCheckbox " 
                + this.x + " "
                + this.y + " "
                + this.color + " "
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

    public double getWidth() {
        return 15;
    }

    public double getY() {
        return y;
    }

    public double getHeight() {
        return 15;
    }
    
    public void setX(double x) {
        this.x = x;
        temp.setX(x);
        tempLabel.setLayoutX(x + 20);
    }
    
    public void setY(double y) {
        this.y = y;
        temp.setY(y);
        tempLabel.setLayoutY(y - 2);
    }
    
}
