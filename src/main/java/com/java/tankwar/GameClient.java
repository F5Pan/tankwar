package com.java.tankwar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

public class GameClient extends JComponent {

	public static final GameClient INSTANCE = new GameClient();

	public static GameClient getInstance() {
		return INSTANCE;
	}

	private Tank playerTank;
	private List<Tank> enemyTanks;
	private List<Wall> walls;
	private List<Missile> missiles;
	private List<Explosion> explosions;
	private Blood blood;
	private final AtomicInteger enemyKilled = new AtomicInteger();

	void addExplosion(Explosion explosion) {
		explosions.add(explosion);
	}

	public List<Missile> getMissiles() {
		return missiles;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public List<Tank> getEnemyTanks() {
		return enemyTanks;
	}

	synchronized void add(Missile missile) {
		missiles.add(missile);
	}

	public Tank getPlayerTank() {
		return playerTank;
	}

	public Blood getBlood() {
		return blood;
	}

	public void setBlood(Blood blood) {
		this.blood = blood;
	}

	private GameClient() {
		this.playerTank = new Tank(400, 100, Direction.DOWN);
		this.missiles = new CopyOnWriteArrayList<>();
		this.explosions = new ArrayList<>();
		this.blood = new Blood(400, 250);
		this.walls = Arrays.asList(new Wall(280, 140, true, 12), new Wall(280, 540, true, 12),
				new Wall(100, 160, false, 12), new Wall(700, 160, false, 12));
		this.initEnemyTanks();
		this.setPreferredSize(new Dimension(800, 600));
	}

	private void initEnemyTanks() {
		this.enemyTanks = new CopyOnWriteArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				this.enemyTanks.add(new Tank(200 + j * 120, 400 + 40 * i, true, Direction.UP));
			}
		}
	}

	private final static Random RANDOM = new Random();

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 600);
		if (!playerTank.isLive()) {

			g.setColor(Color.RED);
			g.setFont(new Font(null, Font.BOLD, 100));
			g.drawString("GAME OVER", 100, 200);
			g.setFont(new Font(null, Font.BOLD, 60));
			g.drawString("PRESS F2 TO RESTART", 60, 360);

		} else {

			g.setColor(Color.WHITE);
			g.setFont(new Font(null, Font.BOLD, 16));
			g.drawString("Missiles: " + missiles.size(), 10, 50);
			g.drawString("explosions: " + explosions.size(), 10, 70);
			g.drawString("Player Tank HP: " + playerTank.getHp(), 10, 90);
			g.drawString("Enemy Left: " + enemyTanks.size(), 10, 110);
			g.drawString("Enemy Killed: " + enemyKilled.get(), 10, 130);
			g.drawImage(Tools.getImage("tree.png"), 720, 10, null);
			g.drawImage(Tools.getImage("tree.png"), 10, 520, null);

			playerTank.draw(g);
			if (playerTank.isDying() && RANDOM.nextInt(3) == 2) {
				blood.setLive(true);
			}
			if (blood.isLive()) {
				blood.draw(g);
			}

			int count = enemyTanks.size();

			enemyTanks.removeIf(t -> !t.isLive());
			enemyKilled.addAndGet(count - enemyTanks.size());
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
			explosions.removeIf(e -> !e.isLive());
			for (Explosion explosion : explosions) {
				explosion.draw(g);
			}
		}
	}

	public static void main(String[] args) {
		com.sun.javafx.application.PlatformImpl.startup(() -> {
		});
		JFrame frame = new JFrame();
		frame.setTitle("坦克大戰!");
		frame.setIconImage(Tools.getImage("icon.png"));
		final GameClient client = GameClient.getInstance();
		frame.add(client);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					client.save();
					System.exit(0);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to save Game", "Oops! Error",
							JOptionPane.ERROR_MESSAGE);
					System.exit(4);
				}
			}
		});
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
			try {
				client.repaint();
				if (client.playerTank.isLive()) {
					for (Tank tank : client.enemyTanks) {
						tank.actRandomly();
					}
				}

				Thread.sleep(50);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

    void save(String destination) throws IOException {
        Save save = new Save(playerTank.isLive(), playerTank.getPosition(),
            enemyTanks.stream().filter(Tank::isLive)
                .map(Tank::getPosition).collect(Collectors.toList()));
        FileUtils.write(new File(destination), JSON.toJSONString(save, true), StandardCharsets.UTF_8);
    }

    void save() throws IOException {
        this.save("game.sav");
    }

	public void restart() {
		if (!playerTank.isLive()) {
			playerTank = new Tank(400, 100, Direction.DOWN);
		}
		this.initEnemyTanks();
	}

}
