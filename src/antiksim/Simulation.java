/*
 * Simulation.java
 * This is part of the Antiksim (Antikythera Mechanism Simulation) project
 * 
 * Copyright 2013 Fotis Xenakis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//Everything about the actual animation, subclassing JPanel to draw graphics
package antiksim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;

class Simulation extends JPanel implements ActionListener
{
	private Timer timer;
	private boolean fancy_indicator_colors;
	private short sun_angle;
	private double moon_angle;
	private double metonic_angle;
	private double saros_angle;
	private double callippic_angle;
	private double olympiad_angle;
	private double exeligmos_angle;
	
	//Ratios of indicators, referring to the sun
	private static final double MOON_RATIO=13.368;
	private static final double METONIC_RATIO=0.263;
	private static final double SAROS_RATIO=0.222;
	private static final double CALLIPPIC_RATIO=0.0132;
	private static final double OLYMPIAD_RATIO=0.25;
	private static final double EXELIGMOS_RATIO=0.018;
	
	//Animation speed definitions
	private static final int SLOW=9*5;
	private static final int NORMAL=3*5;
	private static final int FAST=1*5;
	
	//Font size for the letters
	private float font_size;
	
	public Simulation()
	{
		//Initialize timer, set panel's preferred size and layout
		timer=new Timer(NORMAL,this);
		setPreferredSize(new Dimension(500,350));
		setLayout(null);
		
		//Initialize indicators to normal color and arbitrary positions
		fancy_indicator_colors=false;
		sun_angle=270;
		moon_angle=0;
		metonic_angle=684*METONIC_RATIO;
		saros_angle=0;
		callippic_angle=18182*CALLIPPIC_RATIO;
		olympiad_angle=270;
		exeligmos_angle=1666*EXELIGMOS_RATIO;
		
		//Determine appropriate font size according to platform
		if(UIManager.getLookAndFeel().getName().toLowerCase().contains("gtk") || System.getProperty("os.name").toLowerCase().contains("linux"))
			font_size=(float)0.2; //font size of 20 when panel's width=500 looks satisfying in gtk/cairo/pango
		else
			font_size=(float)0.23;	//font size of 23 when panel's width=500 looks satisfying in Windows
	}
	
	void startAnimation()
	{
		timer.start();
	}
	
	void pauseAnimation()
	{
		timer.stop();
	}
	
	void changeIndicatorColors()
	{
		fancy_indicator_colors=!fancy_indicator_colors;
		repaint();
	}
	
	void resetAnimation()
	{
		//Reset indicator positions
		sun_angle=270;
		moon_angle=0;
		metonic_angle=684*METONIC_RATIO;
		saros_angle=0;
		callippic_angle=18182*CALLIPPIC_RATIO;
		olympiad_angle=270;
		exeligmos_angle=1666*EXELIGMOS_RATIO;
		
		//Reset animation speed
		timer.setDelay(NORMAL);
	}
	
	void setAnimationSpeed(int speed)
	{
		if(speed==0)
			timer.setDelay(SLOW);
		else if(speed==1)
			timer.setDelay(NORMAL);
		else if(speed==2)
			timer.setDelay(FAST);
	}
	
	//Handle the indicators' positions to pace the animation, without overflowing memory
	public void actionPerformed(ActionEvent ae)
	{
		if(sun_angle==359)	//reset sun indicator 
			sun_angle=0;
		else
			++sun_angle;
		
		if(moon_angle==MOON_RATIO*1156)	//reset moon indicator
			moon_angle=0;
		else
			moon_angle+=MOON_RATIO;
			
		if(metonic_angle==METONIC_RATIO*1367)	//reset metonic indicator
			metonic_angle=0;
		else
			metonic_angle+=METONIC_RATIO;
		
		if(saros_angle==SAROS_RATIO*119998)	//reset saros indicator
			saros_angle=0;
		else
			saros_angle+=SAROS_RATIO;
		
		if(callippic_angle==CALLIPPIC_RATIO*599998)	//reset callippic indicator
			callippic_angle=0;
		else
			callippic_angle+=CALLIPPIC_RATIO;
			
		if(olympiad_angle==OLYMPIAD_RATIO*1439)	//reset olympiad indicator
			olympiad_angle=0;
		else
			olympiad_angle+=OLYMPIAD_RATIO;
		
		if(exeligmos_angle==EXELIGMOS_RATIO*59998)	//reset exeligmos indicator
			exeligmos_angle=0;
		else
			exeligmos_angle+=EXELIGMOS_RATIO;
		
		repaint();
	}
	
	private Rectangle calculateDimensions()
	{
		int old_width=getWidth(),old_height=getHeight(),new_width,new_height;
		double panel_cur_ratio=(double)old_width/old_height;
		
		if(panel_cur_ratio>(5/3.5))	//panel's width larger than it should
		{
			new_width=(int)((5/3.5)*old_height);
			return new Rectangle((old_width-new_width)/2,0,new_width,old_height);
		}
		else	//panel's height larger than it should
		{
			new_height=(int)((3.5/5)*old_width);
			return new Rectangle(0,(old_height-new_height)/2,old_width,new_height);
		}
	}
	
	//Override JPanel's method to draw our graphics on it
	public void paintComponent(Graphics g)
	{
		//Calculate where the animation should go on the window
		Rectangle rec=calculateDimensions();
		
		//Divide (imaginarily) the drawing area in equal cells and work with them
		double x=rec.width/5.0,x0=rec.x,x1=x0+x,x2=x1+x,x3=x2+x,x4=x3+x,x5=x4+x,rad=x/2.0;
		double y=rec.height/6.0,y0=rec.y,y1=y0+y,y2=y1+y,y3=y2+y,y4=y3+y,y5=y4+y,y6=y5+y;
		
		//Convert indicators' angles from degrees to radians
		double sunr=Math.toRadians(sun_angle);
		double moonr=Math.toRadians(moon_angle);
		double metonicr=Math.toRadians(metonic_angle);
		double sarosr=Math.toRadians(saros_angle);
		double callippicr=Math.toRadians(callippic_angle);
		double olympiadr=Math.toRadians(olympiad_angle);
		double exeligmosr=Math.toRadians(exeligmos_angle);
		
		//Call the JPanel's method first
		super.paintComponent(g);
		
		//Create a Graphics2D object to do all the drawing
		Graphics2D g2=(Graphics2D)g;
		
		//Set graphics and text antialiasing on
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//Draw background (white)
		g2.setColor(Color.white);
		g2.fill(new Rectangle2D.Double(0,0,getWidth(),getHeight()));
		
		//Draw text on top (black)
		g2.setColor(Color.black);
		g2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,(int)(x*font_size)));
		g2.drawString("Front side",(float)(x1-(x*0.07)),(float)(y2/3));
		g2.drawString("Rear side",(float)(x3-(x*0.05)),(float)(y2/3));
		
		//Draw rectangles (mechanism's front - rear) (black) (make rectanges protrude from big circles 2% of window width)
		g2.fill(new Rectangle2D.Double(x1-(x*0.02),y1,x+(x*0.04),4*y));
		g2.fill(new Rectangle2D.Double(x3-(x*0.02),y1,x+(x*0.04),4*y));
		
		//Draw big circles (mechanism's main dials) (white)
		g2.setColor(Color.white);
		g2.fill(new Ellipse2D.Double(x1,y3-rad,2*rad,2*rad));
		g2.fill(new Ellipse2D.Double(x3,y2-rad,2*rad,2*rad));
		g2.fill(new Ellipse2D.Double(x3,y4-rad,2*rad,2*rad));
		
		//Draw small circles (mechanism's secondary dials) (orange) (put centers 10% of window height higher than big dials to avoid indicators falling on each other)
		g2.setColor(new Color(255,128,0));
		g2.fill(new Ellipse2D.Double(x3+(x/4)-(rad/3),y2-(rad/3)-(y*0.1),2*(rad/3),2*(rad/3)));
		g2.fill(new Ellipse2D.Double(x4-(x/4)-(rad/3),y2-(rad/3)-(y*0.1),2*(rad/3),2*(rad/3)));
		g2.fill(new Ellipse2D.Double(x4-(x/4)-(rad/3),y4-(rad/3)-(y*0.1),2*(rad/3),2*(rad/3)));
		
		//Draw indicators (black or with fancy colors when the description dialog is displayed)
		if(!fancy_indicator_colors)
		{
			g2.setStroke(new BasicStroke((float)1.8));
			g2.setColor(Color.black);
			g2.draw(new Line2D.Double(x1+rad,y3,x1+rad+Math.cos(sunr)*rad,y3+Math.sin(sunr)*rad));
			g2.draw(new Line2D.Double(x1+rad,y3,x1+rad+Math.cos(moonr)*(rad/1.3),y3+Math.sin(moonr)*(rad/1.3)));
			g2.draw(new Line2D.Double(x3+rad,y2,x3+rad+Math.cos(metonicr)*rad,y2+Math.sin(metonicr)*rad));
			g2.draw(new Line2D.Double(x3+rad,y4,x3+rad+Math.cos(sarosr)*rad,y4+Math.sin(sarosr)*rad));
			g2.draw(new Line2D.Double(x3+(rad/2),y2-(y*0.1),x3+(rad/2)+Math.cos(callippicr)*(rad/3),y2-(y*0.1)+Math.sin(callippicr)*(rad/3)));
			g2.draw(new Line2D.Double(x4-(rad/2),y2-(y*0.1),x4-(rad/2)+Math.cos(olympiadr)*(rad/3),y2-(y*0.1)+Math.sin(olympiadr)*(rad/3)));
			g2.draw(new Line2D.Double(x4-(rad/2),y4-(y*0.1),x4-(rad/2)+Math.cos(exeligmosr)*(rad/3),y4-(y*0.1)+Math.sin(exeligmosr)*(rad/3)));
		}
		else
		{
			g2.setStroke(new BasicStroke((float)2.5));
			g2.setColor(new Color(255,185,15));
			g2.draw(new Line2D.Double(x1+rad,y3,x1+rad+Math.cos(sunr)*rad,y3+Math.sin(sunr)*rad));
			g2.setColor(Color.gray);
			g2.draw(new Line2D.Double(x1+rad,y3,x1+rad+Math.cos(moonr)*(rad/1.3),y3+Math.sin(moonr)*(rad/1.3)));
			g2.setColor(new Color(0,0,205));
			g2.draw(new Line2D.Double(x3+rad,y2,x3+rad+Math.cos(metonicr)*rad,y2+Math.sin(metonicr)*rad));
			g2.setColor(Color.red);
			g2.draw(new Line2D.Double(x3+rad,y4,x3+rad+Math.cos(sarosr)*rad,y4+Math.sin(sarosr)*rad));
			g2.setColor(new Color(139,28,98));
			g2.draw(new Line2D.Double(x3+(rad/2),y2-(y*0.1),x3+(rad/2)+Math.cos(callippicr)*(rad/3),y2-(y*0.1)+Math.sin(callippicr)*(rad/3)));
			g2.setColor(new Color(0,100,0));
			g2.draw(new Line2D.Double(x4-(rad/2),y2-(y*0.1),x4-(rad/2)+Math.cos(olympiadr)*(rad/3),y2-(y*0.1)+Math.sin(olympiadr)*(rad/3)));
			g2.setColor(Color.black);
			g2.draw(new Line2D.Double(x4-(rad/2),y4-(y*0.1),x4-(rad/2)+Math.cos(exeligmosr)*(rad/3),y4-(y*0.1)+Math.sin(exeligmosr)*(rad/3)));
		}
	}
}
