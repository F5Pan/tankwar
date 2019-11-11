package com.java.tankwar;

import java.awt.Graphics;

public class Explosion {

	private int x, y;
	
	

	public Explosion(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	private int step = 0;

	private boolean live = true;

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	void draw(Graphics g) {
		if (step > 10) {
			this.setLive(false);
			return;
		}
		g.drawImage(Tools.getImage(step++ + ".gif"), x, y, null);
	}

}
