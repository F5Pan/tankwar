package com.java.tankwar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameClient extends JComponent {

	public static final GameClient INSTANCE = new GameClient();

	public static GameClient getInstance() {
		return INSTANCE;
	}

	private Tank playerTank;
	private List<Tank> enemyTanks;
	private List<Wall> walls;
	private List<Missile> missiles;

	public List<Missile> getMissiles() {
		return missiles;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public List<Tank> getEnemyTanks() {
		return enemyTanks;
	}

	public void removeMissile() {
		// TODO Auto-generated method stub

	}

	public Tank getPlayerTank() {
		return playerTank;
	}

	private GameClient() {
		this.playerTank = new Tank(400, 100, Direction.DOWN);
		this.missiles = new ArrayList<>();
		this.walls = Arrays.asList(new Wall(200, 140, true, 15), new Wall(200, 520, true, 15),
				new Wall(100, 80, false, 15), new Wall(700, 80, false, 15));
		this.initEnemyTanks();

		this.setPreferredSize(new Dimension(800, 600));
	}

	private void initEnemyTanks() {
		this.enemyTanks = new ArrayList<>(12);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				this.enemyTanks.add(new Tank(200 + j * 120, 400 + 40 * i, true, Direction.UP));
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 600);
		playerTank.draw(g);
		enemyTanks.removeIf(t -> !t.isLive());
		if (enemyTanks.isEmpty()) {
			this.initEnemyTanks();
		}
		for (Tank tank : enemyTanks) {
			tank.draw(g);
		}
		for (Wall wall : walls) {
			wall.draw(g);
		}
		missiles.removeIf(m -> !m.isLive());
		for (Missile missile : missiles) {
			missile.draw(g);
		}
	}

	public static void main(String[] args) {
		com.sun.javafx.application.PlatformImpl.startup(() -> {
		});
		JFrame frame = new JFrame();
		frame.setTitle("坦克大戰!");
		frame.setIconImage(Tools.getImage("icon.png"));
		final GameClient client = GameClient.getInstance();
		client.repaint();
		frame.add(client);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				client.playerTank.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				client.playerTank.keyReleased(e);

			}

		});
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		while (true) {
			client.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

}
