/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EAPScrollablePane implements EAPWidget{
    double x1,width;
    double y1,height;
    String color;
    String type;
    
    /**
     * Draws a pane with scrollbars starting at x1,y1 and ending at x2,y2
     * @param x1 - the first x coordinate the button will be drawn at
     * @param y1 - the first y coordinate the button will be drawn at
     * @param width - the second x coordinate the button will be drawn at
     * @param height - the second y coordinate the button will be drawn at
     * @param color - the color in string to describe the color of the body
     * @param copyStage - simply used so that we can make our text prompts be separated 
     * @precon the width, or x1-x1 and the height, or y1-y2 should be not less than 30
     */
    public EAPScrollablePane(double x1, double y1, double width, double height, String color, Stage copyStage) {
        if (Math.abs(x1 -width) < 90 || Math.abs(y1 - height) < 60) {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(copyStage);
            VBox vbox = new VBox(10);
            vbox.getChildren().add(new Label("Dimensions too small! Can't Make a window that tiny!"));
            Button b = new Button("OK");
            b.setOnAction((ActionEvent event1) -> {
                dialog.close();
            });
            vbox.getChildren().add(b);
            vbox.alignmentProperty().setValue(Pos.CENTER);
            Scene dialogScene = new Scene(vbox, 350, 100);
            dialog.setScene(dialogScene);
            dialog.show();
        }
        else {
            this.x1=x1;
            this.y1=y1;
            this.width=width - x1;
            this.height=height - y1;
            this.color=color;
            this.type = "Scrollable Pane";
        }
    }
    Rectangle vert;
    Rectangle hori;
    Rectangle body;
    Rectangle arrowupbutton;
    Rectangle arrowdownbutton;
    Rectangle arrowleftbutton;
    Rectangle arrowrightbutton;
    Line line;
    Line line2;
    Line line3;
    Line line4;
    Line line5;
    Line line6;
    Line line7;
    Line line8;
    Line line9;
    Line line10;
    Line line11;
    Line line12;

    
    @Override
    public void paint(Pane p) {
        //Pane tempPane = new Pane();
        //tempPane.setLayoutX(Math.min(x1, width));
        //tempPane.setLayoutY(Math.min(y1, height));

        vert = new Rectangle(x1 + width - 30, y1 + 30 , 30 , height -30);
        hori = new Rectangle(x1 + 30, y1 + height -30, width - 90, 30);

        body = new Rectangle(x1, y1,  width, height);

        arrowupbutton = new Rectangle(x1 + width - 30, y1, 30, 30);
        arrowdownbutton = new Rectangle(x1 + width - 30, y1 + height - 30, 30, 30);
        arrowleftbutton = new Rectangle(x1,y1 + height - 30, 30, 30);
        arrowrightbutton = new Rectangle(x1 + width - 60, y1 + height - 30, 30, 30);

        // triangle for the up arrow
        line = new Line(x1 + width - 15, y1 + 7, x1 + width - 5, y1 + 22 );
        line2 = new Line(x1 + width - 15, y1 + 7, x1 + width - 25, y1 + 22  );
        line3 = new Line(x1 + width - 25, y1 + 22,  x1 + width - 5, y1 + 22  );

        // triangle for the down arrow
        line4 = new Line(x1 + width - 25, y1 + height - 22,  x1 + width - 5, y1 + height - 22  );
        line5 = new Line(x1 + width - 15, y1 + height - 7, x1 + width - 25, y1 + height -22  );
        line6 = new Line(x1 + width - 15, y1 + height - 7, x1 + width - 5, y1 + height - 22 );

        // triangle for the right arrow
        line7 = new Line(x1 + width - 52, y1 + height - 25,  x1 + width - 52, y1 + height - 5  );
        line8 = new Line(x1 + width - 52, y1 + height - 25, x1 + width - 37, y1 + height -15  );
        line9 = new Line(x1 + width - 52, y1 + height - 5, x1 + width - 37, y1 + height - 15 );

        // triangle for the left arrow
        line10 = new Line(x1 + 22, y1 + height - 25,  x1 + 22, y1 + height - 5  );
        line11 = new Line(x1 + 22, y1 + height - 25, x1 + 7, y1 + height -15  );
        line12 = new Line(x1 + 22, y1 + height - 5, x1 + 7, y1 + height - 15 );

        line.setStyle("-fx-stroke-width:2;");
        line2.setStyle("-fx-stroke-width:2;");
        line3.setStyle("-fx-stroke-width:2;");
        line4.setStyle("-fx-stroke-width:2;");
        line5.setStyle("-fx-stroke-width:2;");
        line6.setStyle("-fx-stroke-width:2;");
        line7.setStyle("-fx-stroke-width:2;");
        line8.setStyle("-fx-stroke-width:2;");
        line9.setStyle("-fx-stroke-width:2;");
        line10.setStyle("-fx-stroke-width:2;");
        line11.setStyle("-fx-stroke-width:2;");
        line12.setStyle("-fx-stroke-width:2;");
        body.setStyle("-fx-fill: " + color + "; -fx-stroke: #000000; -fx-stroke-width:2;");
        vert.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        hori.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        arrowupbutton.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        arrowdownbutton.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        arrowleftbutton.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");
        arrowrightbutton.setStyle("-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width:2;");

        //tempPane.getChildren().addAll(vert, hori, body, arrowupbutton, arrowdownbutton, arrowleftbutton, arrowrightbutton);
        //tempPane.getChildren().addAll(line, line2, line3, line4, line5, line6, line7
              //                           ,line8, line9, line10, line11, line12);
        p.getChildren().addAll(body, vert, hori, arrowupbutton, arrowdownbutton, arrowleftbutton, arrowrightbutton, 
                line, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12);
    }
    
    @Override
    public void removeFromPane(Pane p) {
        p.getChildren().removeAll(body, vert, hori, arrowupbutton, arrowdownbutton, arrowleftbutton, arrowrightbutton, 
                line, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12);
    }
    
    public void moveleftButton(double dx, double dy) {
        arrowleftbutton.setX(dx - width);
        arrowleftbutton.setY(dy + height - 30);
        line10.setStartX(dx - width + 22);
        line11.setStartX(dx - width + 22);
        line12.setStartX(dx  - width + 22);
        line10.setStartY(dy + height - 25);
        line11.setStartY(dy + height - 25);
        line12.setStartY(dy + height - 5);
        line10.setEndX(dx - width + 22);
        line11.setEndX(dx - width + 7);
        line12.setEndX(dx - width + 7);
        line10.setEndY(dy + height - 5);
        line11.setEndY(dy + height - 15);
        line12.setEndY(dy + height - 15);
    }
    
    public void moverightButton(double dx, double dy) {
        arrowrightbutton.setX(dx - 60);
        arrowrightbutton.setY(dy + height - 30);
        line7.setStartX(dx - 52);
        line8.setStartX(dx  - 52);
        line9.setStartX(dx  - 52);
        line7.setStartY(dy + height - 25);
        line8.setStartY(dy + height - 25);
        line9.setStartY(dy + height - 5);
        line7.setEndX(dx - 52);
        line8.setEndX(dx - 37);
        line9.setEndX(dx - 37);
        line7.setEndY(dy + height - 5);
        line8.setEndY(dy + height - 15);
        line9.setEndY(dy + height - 15);
    }
    
    public void movedownButton(double dx, double dy) {
        arrowdownbutton.setX(dx - 30);
        arrowdownbutton.setY(dy + height - 30);
        line4.setStartX(dx - 25);
        line5.setStartX(dx  - 15);
        line6.setStartX(dx  - 15);
        line4.setStartY(dy + height - 22);
        line5.setStartY(dy + height - 7);
        line6.setStartY(dy + height - 7);
        line4.setEndX(dx - 5);
        line5.setEndX(dx - 25);
        line6.setEndX(dx - 5);
        line4.setEndY(dy + height - 22);
        line5.setEndY(dy + height - 22);
        line6.setEndY(dy + height - 22);
    }
    
    public void moveupButton(double dx, double dy) {
        arrowupbutton.setX(dx - 30);
        arrowupbutton.setY(dy);
        line.setStartX(dx - 15);
        line2.setStartX(dx  - 15);
        line3.setStartX(dx  - 25);
        line.setStartY(dy + 7);
        line2.setStartY(dy + 7);
        line3.setStartY(dy + 22);
        line.setEndX(dx -5);
        line2.setEndX(dx - 25);
        line3.setEndX(dx - 5);
        line.setEndY(dy + 22);
        line2.setEndY(dy + 22);
        line3.setEndY(dy + 22);
    }
    
    public void setVertPos(double x, double y) {
        vert.setX(x + width - 30);
        vert.setY(y + 30);
    }
    
    public void setHoriPos(double x, double y) {
        hori.setX(x + 30);
        hori.setY(y + height - 30);
    }
    
    public void setHeight(double height) {
        this.height = height;
        body.setHeight(height);
        vert.setHeight(height - 60);
        //tempPane.setMaxHeight(height);
    }
    
    public void setWidth(double width) {
        this.width = width;
        //tempPane.setMaxWidth(width);
        body.setWidth(width);
        hori.setWidth(width - 90);
    }
    
    public void setX(double x) {
        x1 = x;
        body.setX(x1);
        hori.setX(x1 + 30);
        
        //tempPane.setLayoutX(x);
        
    }
    
    public void setY(double y) {
        y1 = y;
        body.setY(y1);
        vert.setY(y1 + 30);
        //tempPane.setLayoutY(y);
        
    }
    
    public double getX() { return x1; }
    public double getY() { return y1; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    @Override
    public boolean contains(double x, double y) {
        return body.contains(x, y) || hori.contains(x, y) || vert.contains(x, y);
    }
    
    
    @Override
    public String toString() {
        return ("EAPScrollablePane " 
                + this.x1 + " "
                + this.y1 + " "
                + this.width + " "
                + this.height + " "
                + this.color);
    }
    
    
    @Override
    public String getWidgetType() {
        return type;
    }
    
}
