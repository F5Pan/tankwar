package com.java.tankwar;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameClient extends JComponent{
	
	private GameClient() {
		this.setPreferredSize(new Dimension(800,600));
	}
	
		
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(new ImageIcon("assets/images/tankD.gif").getImage(),400,100,null);
	}



	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("坦克大戰!");
		frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
		GameClient client = new GameClient();
		client.repaint();
		frame.add(client);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	

}
