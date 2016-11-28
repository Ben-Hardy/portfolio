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

public class EAPButton implements EAPWidget{
    double x1;
    double y1;
    double x2;
    double y2;
    String color;
    String txt;
    Font font;
    private Rectangle rect;
    String type;
    Label tempLabel;
    

    
    public EAPButton(double x1, double y1, double x2, double y2, String color, String txt, Font font) {
        this.type = "Button";
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.color=color;
        this.txt=txt;
        this.font=font;
    }
    
    public void setHeight(double height) {
        this.x2 = height;
    }
    
    public void setWidth(double width) {
        this.y2 = width;
    }
    
    public void setX(double x) {
        x1 = x;
        rect.setX(x);
        tempLabel.setLayoutX(x + 7);
    }
    
    public void setY(double y) {
        y1 = y;
        rect.setY(y);
        tempLabel.setLayoutY(y + 7);
    }
    
    @Override
    public void paint(Pane p) {
        rect = new Rectangle(x1,y1,x2,y2);
        rect.setStyle("-fx-fill: " + color + "; -fx-stroke: #000000; -fx-stroke-width:2;");
        p.getChildren().add(rect);
        
        tempLabel = new Label("Label");
        tempLabel.setFont(font);
        if (!txt.isEmpty()) {
            tempLabel.setText(txt);
        }
        tempLabel.setLayoutX(x1+ 7);
        tempLabel.setLayoutY(y1+7);
        p.getChildren().add(tempLabel);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(rect,tempLabel);
    }
    
     @Override
    public boolean contains(double x, double y) {
        return rect.contains(x, y);
    }

    @Override
    public String toString() {
        return ("EAPButton " 
                + this.x1 + " "
                + this.y1 + " "
                + this.x2 + " "
                + this.y2 + " "
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
        return x1;
    }


    public double getY() {
        return y1;
    }

    public double getHeight() {
        return y2;
    }
    
    public double getWidth() {
        return x2;
    }
    
    public String getColor() { return color; }
   
    public Font getFont() { return font; }
    
    public String getText() { return txt; }
}

