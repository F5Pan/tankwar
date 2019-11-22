package com.java.tankwar;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Save {
	
	private boolean gameContinued;
	
	private Position playerPosition;
	
	private List<Position> enemyPosition;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Position {

		private int x, y;
		private Direction direction;
	}
	
}
