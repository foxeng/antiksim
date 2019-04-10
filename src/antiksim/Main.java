/*
 * Main.java
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

//Application's entry point, create Gui
package antiksim;

import javax.swing.*;

class Main
{
	public static void main(String args[])
	{
		//Try to use the native system's look and feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (UnsupportedLookAndFeelException e){
			System.out.println("Failed to set native system's look and feel with exception:"+e);}
		catch (ClassNotFoundException e){
			System.out.println("Failed to set native system's look and feel with exception:"+e);}
		catch (InstantiationException e){
			System.out.println("Failed to set native system's look and feel with exception:"+e);}
		catch (IllegalAccessException e){
			System.out.println("Failed to set native system's look and feel with exception:"+e);}
		
		//Run the gui in the event dispatch thread
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					new Gui();
				}});
	}
}
