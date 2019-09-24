package com.java.tankwar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameClient extends JComponent {

	private Tank playerTank;

	private GameClient() {
		this.playerTank = new Tank(400, 100, Direction.DOWN);
		this.setPreferredSize(new Dimension(800, 600));
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.playerTank.getImage(), this.playerTank.getX(), this.playerTank.getY(), null);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("坦克大戰!");
		frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
	 final GameClient client = new GameClient();
		client.repaint();
		frame.add(client);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					client.playerTank.setY(client.playerTank.getY() - 5);
					break;
				case KeyEvent.VK_DOWN:
					client.playerTank.setY(client.playerTank.getY() + 5);
					break;
				case KeyEvent.VK_LEFT:
					client.playerTank.setX(client.playerTank.getX() - 5);
					break;
				case KeyEvent.VK_RIGHT:
					client.playerTank.setX(client.playerTank.getX() + 5);
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

		});
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		while(true) {
			client.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

}
