/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class EAPPane implements EAPWidget{
    double x1,x2;
    double y1,y2;
    String color;
    String type;
    //private Pane tempPane;
    private Rectangle temp;
    public EAPPane(double x1, double y1, double x2, double y2, String color) {
        this.type = "Pane";
        this.x1=x1;
        this.y1=y1;
        this.x2=x2 - x1;
        this.y2=y2 - y1;
        this.color=color;
    }
    
    @Override
    public void paint(Pane p) {
        //tempPane = new Pane();
        //tempPane.setLayoutX(Math.min(x1, x2));
        //tempPane.setLayoutY(Math.min(y1, y2));
        temp = new Rectangle(x1, y1, x2, y2);
        temp.setStyle("-fx-fill: " + color + "; -fx-stroke: #000000; -fx-stroke-width:2;");
        //tempPane.getChildren().add(temp);
        p.getChildren().add(temp);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(temp);
    }
    
    public void setHeight(double height) {
        this.y2 = height;
        temp.setHeight(height);
        //tempPane.setMaxHeight(height);
    }
    
    public void setWidth(double width) {
        this.x2 = width;
        //tempPane.setMaxWidth(width);
        temp.setWidth(width);
    }
    
    public void setX(double x) {
        x1 = x;
        temp.setX(x);
        //tempPane.setLayoutX(x);
        
    }
    
    public void setY(double y) {
        y1 = y;
        temp.setY(y);
        //tempPane.setLayoutY(y);
        
    }
    
    public double getX() { return x1; }
    public double getY() { return y1; }
    public double getWidth() { return x2; }
    public double getHeight() { return y2; }
    
    @Override
    public boolean contains(double x, double y) {
        return temp.contains(x,y);
    }
    
    @Override
    public String toString() {
        return ("EAPPane " 
                + this.x1 + " "
                + this.y1 + " "
                + this.x2 + " "
                + this.y2 + " "
                + this.color);
    }
    
    @Override
    public String getWidgetType() {
        return type;
    }
    
}
