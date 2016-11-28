/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EAPLine implements EAPWidget{
    double x1,x2,y1,y2;
    private Line line;
    
    String type; 
    
    public EAPLine(double x1,double y1,double x2,double y2, String type) {
        this.type = type;
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
    
    public void setStart(double x, double y) {
        this.x1 = x;
        this.y1 = y;
        line.setStartX(x1);
        line.setStartY(y1);
    }
    
    public void setEnd(double x, double y) {
        this.x2 = x;
        this.y2 = y;
        line.setEndX(x2);
        line.setEndY(y2);
    }
    
    public double getStartX() { return x1; }
    public double getStartY() { return y1; }
    public double getEndX() { return x2; }
    public double getEndY() { return y2; }
    
    
    
    @Override
    public void paint(Pane p) {
        line = new Line(x1, y1, x2, y2);
        line.setStyle("-fx-stroke-width:2;");
        p.getChildren().add(line);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(line);
    }
    
     @Override
    public boolean contains(double x, double y) {
        return line.contains(x, y);
    }
    
    @Override
    public String toString() {
        return (this.type + " "
                + this.x1 + " "
                + this.y1 + " "
                + this.x2 + " "
                + this.y2 );
    }
    
    
    @Override
    public String getWidgetType() {
        return type;
    }
   
}
