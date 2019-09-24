package com.java.tankwar;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Tank {

	private int x;

	private int y;

	private Direction direction;

	public Tank(int x, int y, Direction direction) {
		super();
		this.x = x;
		this.y = y;
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

	Image getImage() {
		switch (direction) {
		case UP:
			return new ImageIcon("assets/images/tankU.gif").getImage();
		case DOWN:
			return new ImageIcon("assets/images/tankD.gif").getImage();
		case LEFT:
			return new ImageIcon("assets/images/tankL.gif").getImage();
		case RIGHT:
			return new ImageIcon("assets/images/tankR.gif").getImage();

		}
		return null;
	}

	void move() {
		switch (direction) {
		case UP:
			y -= 5;
			break;

		case DOWN:
			y += 5;
			break;

		case LEFT:
			x -= 5;
			break;

		case RIGHT:
			x += 5;
			break;
		}
	}
}
