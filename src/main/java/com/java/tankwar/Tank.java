package com.java.tankwar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

import javafx.scene.media.*;

public class Tank {

	private int x;

	private int y;

	private boolean enemy;

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
		switch (direction) {
		case UP:
			y -= 5;
			break;
		case LEFT_UP:
			y -= 5;
			x -= 5;
			break;
		case RIGHT_UP:
			y -= 5;
			x += 5;
			break;
		case DOWN:
			y += 5;
			break;
		case LEFT_DOWN:
			y += 5;
			x -= 5;
			break;
		case RIGHT_DOWN:
			y += 5;
			x += 5;
			break;
		case LEFT:
			x -= 5;
			break;

		case RIGHT:
			x += 5;
			break;
		}
	}

	Image getImage() {
		String prefix = enemy ? "e" : "";
		return direction.getImage(prefix+"tank");
	}

	void draw(Graphics g) {
		int oldX = x, oldY = y;
		this.determineDirection();
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
			if (rec.intersects(tank.getRectangle())) {
				x = oldX;
				y = oldY;
				break;
			}
		}

		g.drawImage(this.getImage(), this.x, this.y, null);
	}

	public Rectangle getRectangle() {
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
		}
		this.determineDirection();
	}

	private void fire() {
		Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6, y + getImage().getHeight(null) / 2 - 6,
				enemy, direction);
		GameClient.getInstance().getMissiles().add(missile);

		playAudio("shoot.wav");
	}
	
	private void superFire() {
		for(Direction direction :Direction.values()) {
			Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6, y + getImage().getHeight(null) / 2 - 6,
					enemy, direction);
			GameClient.getInstance().getMissiles().add(missile);	
		}
		String audioFile = new Random().nextBoolean()?"supershoot.aiff":"supershoot.wav";
		playAudio(audioFile);
		
	}


	private void playAudio(String fileName) {
		Media sound = new Media(new File("assets/audios/"+fileName).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
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
	}
}
