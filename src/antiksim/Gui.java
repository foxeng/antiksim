/*
 * Gui.java
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

//Class to create and show the gui, handling events
//TODO: Add menu item to display the user licence
package antiksim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Gui
{	
	public Gui()
	{
		//Initialize frame, set default close operation and set a white background
		final JFrame frame=new JFrame("Antikythera Mechanism Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create menubar and set it to the frame
		JMenuBar menubar=new JMenuBar();
		frame.setJMenuBar(menubar);
		
		//Create menus and add them to the menubar
		JMenu actions_menu=new JMenu("Actions");
		JMenu help_menu=new JMenu("Help");
		menubar.add(actions_menu);
		menubar.add(help_menu);
		
		//Create menu items, set tooltips and add them to the menus
		final JMenuItem start_button=new JMenuItem("Start");
		final JMenuItem pause_button=new JMenuItem("Pause");
		final JMenuItem reset_button=new JMenuItem("Reset");
		JMenuItem speed_menu=new JMenu("Change speed");
		final JMenuItem exit_button=new JMenuItem("Exit");
		JMenuItem controls_button=new JMenuItem("Controls");
		JMenuItem description_button=new JMenuItem("Description");
		JMenuItem about_button=new JMenuItem("About");
		start_button.setToolTipText("Start animation");
		pause_button.setToolTipText("Pause animation");
		reset_button.setToolTipText("Reset animation");
		speed_menu.setToolTipText("Select animation speed");
		exit_button.setToolTipText("Exit application");
		controls_button.setToolTipText("Simulation controls");
		description_button.setToolTipText("About the Mechanism");
		about_button.setToolTipText("About this application");
		actions_menu.add(start_button);
		actions_menu.add(pause_button);
		actions_menu.add(reset_button);
		actions_menu.add(speed_menu);
		actions_menu.addSeparator();
		actions_menu.add(exit_button);
		help_menu.add(controls_button);
		help_menu.add(description_button);
		help_menu.add(about_button);
		
		//Create radio buttons and buttongroup for speed selection and add them to the menu
		ButtonGroup rbgroup=new ButtonGroup();
		final JRadioButtonMenuItem rbutton1=new JRadioButtonMenuItem("Slow");
		final JRadioButtonMenuItem rbutton2=new JRadioButtonMenuItem("Normal");
		final JRadioButtonMenuItem rbutton3=new JRadioButtonMenuItem("Fast");
		rbutton2.setSelected(true);
		rbgroup.add(rbutton1);
		rbgroup.add(rbutton2);
		rbgroup.add(rbutton3);
		speed_menu.add(rbutton1);
		speed_menu.add(rbutton2);
		speed_menu.add(rbutton3);
		
		//Create simulation panel and add it to the frame
		final Simulation sim=new Simulation();
		frame.add(sim,BorderLayout.CENTER);
		
		//Create action listeners for all menu items
		start_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.startAnimation();
					start_button.setEnabled(false);
					pause_button.setEnabled(true);
				}});
		pause_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.pauseAnimation();
					pause_button.setEnabled(false);
					start_button.setEnabled(true);
				}});
		reset_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					if(pause_button.isEnabled())
						sim.pauseAnimation();
					sim.resetAnimation();
					if(start_button.isEnabled())
						start_button.doClick();
					else
						sim.startAnimation();
					rbutton2.setSelected(true);
				}});
		exit_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					System.exit(0);
				}});
		controls_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					JOptionPane.showMessageDialog(frame,new JLabel("<html><center><b><font size=\"5\" color=\"red\">CONTROLS</font></b></center>"+
						"<br><b>Left/right mouse click:</b> start/pause animation.<br><b>Middle mouse click:"+
						"</b> reset animation.<br><b>Actions -> Change speed:</b> change animation's speed.</html>"),"Controls",JOptionPane.PLAIN_MESSAGE);
				}});
		description_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.changeIndicatorColors();
					JOptionPane.showMessageDialog(frame,new JLabel("<html><div align=center><b><font size=\"5\" color=\"red\">About the Mechanism</font></b><br>"+
						"<br>The Antikythera Mechanism is an ancient astronomical calculator<br>found in a shipwreck off the greek island of Antikythera.<br>"+
						"<br><font color=rgb(255,185,15)><b>Golden</b></font> indicator: <b>Sun</b><br><font color=rgb(128,128,128)><b>Gray</b></font> indicator: <b>Moon</b><br>"+
						"<font color=rgb(0,0,205)><b>Blue</b></font> indicator: <b>Metonic Cycle</b><br><font color=rgb(139,28,98)><b>Purple</b></font> indicator: "+
						"<b>Callippic Cycle</b><br><font color=rgb(0,100,0)><b>Green</b></font> indicator: <b>Olympiad</b><br><font color=rgb(255,0,0)><b>Red</b></font> "+
						"indicator: <b>Saros</b><br><font color=rgb(0,0,0)><b>Black</b></font> indicator: <b>Exeligmos</b><br><br><b>More information:<br>Official website:</b> "+
						"<font color=\"blue\">www.antikythera-mechanism.gr</font><br><b>Wikipedia article:</b> "+
						"<font color=\"blue\">en.wikipedia.org/wiki/Antikythera_mechanism</font></div></html>"),"Description",JOptionPane.PLAIN_MESSAGE);
					sim.changeIndicatorColors();
				}});
		about_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					JOptionPane.showMessageDialog(frame,new JLabel("<html><div align=center><b><font size=\"6\" color=\"blue\">Antikythera Mechanism Simulation</font></b><br>"+
						"<font size=\"5\">A simple, minimalistic simulation of the<br>Antikythera Mechanism's main functions.</font><br><br><br>"+
						"<font size=\"3\">Copyright 2013 Fotis Xenakis<br>This is free software published under<br>the Apache Licence, Version 2.0<br>"+
						"A copy of the licence is distributed with the application.</font></div></html>"),"About",JOptionPane.PLAIN_MESSAGE);
				}});
		rbutton1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.setAnimationSpeed(0);
				}});
		rbutton2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.setAnimationSpeed(1);
				}});
		rbutton3.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					sim.setAnimationSpeed(2);
				}});
		
		//Add mouselistener to control the simulation with the mouse
		sim.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me)
			{
				if(me.getButton()==MouseEvent.BUTTON2)
				{
					reset_button.doClick();
				}
				else
				{
					if(start_button.isEnabled())
						start_button.doClick();
					else
						pause_button.doClick();
				}
			}
		});
		
		//Use pack() to fit widgets exactly
		frame.pack();
		
		//Set frame's on-screen location and show it
		frame.setLocationRelativeTo(null);	//show window in the center
		//frame.setLocationByPlatform(true);	//let the window manager place the window as it does with all new ones
		frame.setVisible(true);
		
		//Start the animation
		start_button.doClick();
	}
}
