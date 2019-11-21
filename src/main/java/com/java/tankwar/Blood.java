package com.java.tankwar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Blood {

	private int x, y;

	private final Image image;

	private boolean live = true;

	public Rectangle getRectangle() {
		return new Rectangle(x,y,image.getWidth(null),image.getHeight(null));
	}

	public Blood(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.image = Tools.getImage("blood.png");
	}

	void draw(Graphics g) {
		g.drawImage(image, x, y, null);
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

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

}
