package com.java.tankwar;

import java.awt.Image;

import javax.swing.ImageIcon;


public class Tools {

	public static Image getImage(String imageName) {
		return new ImageIcon("assets/images/"+imageName).getImage();
	}
}
