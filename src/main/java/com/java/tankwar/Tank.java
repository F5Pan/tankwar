package com.java.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {

	private static final int MOVE_SPEED = 5;

	private int x;

	private int y;

	private boolean enemy;

	private boolean live = true;

	private static final int MAX_HP = 100;

	private int hp = MAX_HP;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isEnemy() {
		return enemy;
	}

	private Direction direction;

	public Tank(int x, int y, Direction direction) {
		this(x, y, false, direction);
	}

	public Tank(int x, int y, boolean enemy, Direction direction) {
		super();
		this.x = x;
		this.y = y;
		this.enemy = enemy;
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	void move() {
		if (this.stopped) {
			return;
		}
		x += direction.xFactor * MOVE_SPEED;
		y += direction.yFactor * MOVE_SPEED;

	}

	Image getImage() {
		String prefix = enemy ? "e" : "";
		return direction.getImage(prefix + "tank");
	}
	
	boolean  isDying() {
		return this.hp <= MAX_HP*0.2;
	}

	void draw(Graphics g) {

		int oldX = x, oldY = y;
		if (!this.enemy) {
			this.determineDirection();
		}
		this.move();

		if (x < 0) {
			x = 0;
		} else if (x > 800 - getImage().getHeight(null)) {
			x = 800 - getImage().getHeight(null);
		}
		if (y < 0) {
			y = 0;
		} else if (y > 600 - getImage().getHeight(null)) {
			y = 600 - getImage().getHeight(null);
		}

		Rectangle rec = this.getRectangle();
		for (Wall wall : GameClient.getInstance().getWalls()) {
			if (rec.intersects(wall.getRectangle())) {
				x = oldX;
				y = oldY;
				break;
			}
		}

		for (Tank tank : GameClient.getInstance().getEnemyTanks()) {
			if (tank != this && rec.intersects(tank.getRectangle())) {
				x = oldX;
				y = oldY;
				break;
			}
		}

		if (this.enemy && rec.intersects(GameClient.getInstance().getPlayerTank().getRectangle())) {
			x = oldX;
			y = oldY;
		}

		if (!enemy) {
			Blood blood = GameClient.getInstance().getBlood();
			if (blood.isLive() && rec.intersects(blood.getRectangle())) {
				this.hp = MAX_HP;
				Tools.playAudio("revive.wav");
				GameClient.getInstance().getBlood().setLive(false);
			}

			g.setColor(Color.WHITE);
			g.fillRect(x, y - 10, this.getImage().getWidth(null), 10);

			g.setColor(Color.RED);
			int width = hp * this.getImage().getWidth(null) / 100;
			g.fillRect(x, y - 10, width, 10);

			Image petImage = Tools.getImage("pet-camel.gif");
			g.drawImage(petImage, this.x - petImage.getWidth(null) - DISTAMCE_TO_PET, this.y, null);
		}
		g.drawImage(this.getImage(), this.x, this.y, null);
	}

	private static final int DISTAMCE_TO_PET = 4;

	public Rectangle getRectangle() {
		if (enemy) {
			return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
		} else {
			Image petImage = Tools.getImage("pet-camel.gif");
			int delta = petImage.getWidth(null) + DISTAMCE_TO_PET;
			return new Rectangle(x - delta, y, getImage().getWidth(null) + delta, getImage().getHeight(null));
		}
	}

	public Rectangle getRectangleForHitDetection() {
		return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
	}

	private boolean up, down, left, right;

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;

		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_F2:
			GameClient.getInstance().restart();
			break;
		}

		this.determineDirection();
	}

	private void fire() {
		Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6, y + getImage().getHeight(null) / 2 - 6,
				enemy, direction);
		GameClient.getInstance().getMissiles().add(missile);

		Tools.playAudio("shoot.wav");
	}

	private void superFire() {
		for (Direction direction : Direction.values()) {
			Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6, y + getImage().getHeight(null) / 2 - 6,
					enemy, direction);
			GameClient.getInstance().add(missile);
		}
		String audioFile = new Random().nextBoolean() ? "supershoot.aiff" : "supershoot.wav";
		Tools.playAudio(audioFile);

	}

	private boolean stopped;

	private void determineDirection() {
		if (!up && !left && !down && !right) {
			this.stopped = true;
		} else {
			if (up && left && !down && !right) {
				this.direction = Direction.LEFT_UP;
			} else if (up && !left && !down && right) {
				this.direction = Direction.RIGHT_UP;
			} else if (up && !left && !down && !right) {
				this.direction = Direction.UP;
			} else if (!up && !left && down && !right) {
				this.direction = Direction.DOWN;
			} else if (!up && left && down && !right) {
				this.direction = Direction.LEFT_DOWN;
			} else if (!up && !left && down && right) {
				this.direction = Direction.RIGHT_DOWN;
			} else if (!up && left && !down && !right) {
				this.direction = Direction.LEFT;
			} else if (!up && !left && !down && right) {
				this.direction = Direction.RIGHT;
			}
			this.stopped = false;
		}

	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		}
		this.determineDirection();
	}

	private final Random random = new Random();

	private int step = random.nextInt(12) + 3;

	public void actRandomly() {
		Direction[] dirs = Direction.values();
		if (step == 0) {
			step = random.nextInt(12) + 3;
			this.direction = dirs[new Random().nextInt(dirs.length)];
			if (new Random().nextBoolean()) {
				this.fire();
			}
		}
		step--;
	}

}
