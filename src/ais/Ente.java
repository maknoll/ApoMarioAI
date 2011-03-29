package ais;

//import java.util.ArrayList;

import java.util.ArrayList;

import apoMario.ai.ApoMarioAI;
//import apoMario.ai.ApoMarioAIConstants;
//import apoMario.ai.ApoMarioAIEnemy;
import apoMario.ai.ApoMarioAIConstants;
import apoMario.ai.ApoMarioAIEnemy;
import apoMario.ai.ApoMarioAILevel;
import apoMario.ai.ApoMarioAIPlayer;

public class Ente extends ApoMarioAI{
	
	static boolean jumps = false;
	
	@Override
	public String getTeamName() {
		
		return "Ente";
	}

	@Override
	public String getAuthor() {
		
		return "Martin Knoll";
	}

	@Override
	public void think(ApoMarioAIPlayer player, ApoMarioAILevel level) {
		
		int x = (int)player.getX();
		int y = player.getType() > 0 ? (int)(player.getY()+1) : (int)player.getY();

		int direction = search(x, y, level, player);
		
		player.runRight();
		player.runFast();
		
		if (direction == 3) {
			player.jump();
			Ente.jumps = true;
			player.runRight();
			player.runFast();
		}
		else if (direction == 2 && Ente.jumps) {
			player.jump();
			player.runRight();
			player.runFast();
			if (!player.canJump()) Ente.jumps = false;
		}
		else if (direction == 1) {
			Ente.jumps = false;
			player.runRight();
			player.runFast();
		}
		else if (direction == 4) {
//			player.runStand();
			player.jump();
			Ente.jumps = true;
		}
		
		player.addMessage(String.valueOf(direction));
			
	}

	
	public static int search(int x, int y, ApoMarioAILevel level, ApoMarioAIPlayer player) {
		
		int z = x + 8;
		int up = rec(x+1,y-1, z, level)+3;
		int forward = rec(x+1,y, z, level)+2;
		int down = rec(x+1,y+1, z, level)+1;
		
		if (y < 2) return 1;
		if (collision(x,y,level) || up >= 1000 && forward >= 1000 && down >= 1000) return 4;
		if (level.getLevelArray()[14][x+1] == ApoMarioAIConstants.LEVEL_EMPTY) return 3;
		if (level.getLevelArray()[14][x+2] == ApoMarioAIConstants.LEVEL_EMPTY) return 3;
		
		return (up < forward) && (up < down) ? 3 : (forward < down) ? 2 : 1;
	}
	
	private static int rec(int x, int y, int z, ApoMarioAILevel level) {
		
		if (x >= z) 
			return 1;
		else if (collision(x, y, level) || y <= 5 || y >= 14)
			return 1000;
		else {
			int up = rec(x+1,y-1, z, level)+3;
			int forward = rec(x+1,y, z, level)+2;
			int down = rec(x+1,y+1, z, level)+1;
			
			return (up < forward) && (up < down) ? up : (forward < down) ? forward : down;
		}
	}
	
	private static boolean collision(int x, int y, ApoMarioAILevel level) {

		ArrayList<ApoMarioAIEnemy> list = level.getEnemies(); 
		for (int i = 0; i < list.size(); i++) { 
			
			if ((int)list.get(i).getX()-x <= 2 && (int)list.get(i).getY()-y <= 2 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x && (int)list.get(i).getY() == y-1 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x && (int)list.get(i).getY() == y && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x && (int)list.get(i).getY() == y+1 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x && (int)list.get(i).getY() == y+2 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x+1 && (int)list.get(i).getY() == y-1 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x+1 && (int)list.get(i).getY() == y && !list.get(i).isFireballType()) return true; 
//			if ((int)list.get(i).getX() == x+1 && (int)list.get(i).getY() == y+1 && !list.get(i).isFireballType()) return true; 
//			if ((int)list.get(i).getX() == x+1 && (int)list.get(i).getY() == y+2 && !list.get(i).isFireballType()) return true; 
//			if ((int)list.get(i).getX() == x+2 && (int)list.get(i).getY() == y-1 && !list.get(i).isFireballType()) return true;
//			if ((int)list.get(i).getX() == x+2 && (int)list.get(i).getY() == y && !list.get(i).isFireballType()) return true;  
//			if ((int)list.get(i).getX() == x+2 && (int)list.get(i).getY() == y+1 && !list.get(i).isFireballType()) return true; 
//			if ((int)list.get(i).getX() == x+2 && (int)list.get(i).getY() == y+2 && !list.get(i).isFireballType()) return true; 
		} 
		
		return 
			level.getLevelArray()[y][x] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y][x] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y-1][x] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y-1][x+1] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y][x+1] != ApoMarioAIConstants.LEVEL_EMPTY
//			level.getLevelArray()[y][x] == ApoMarioAIConstants.LEVEL_CANNON ||
//			level.getLevelArray()[y-1][x] == ApoMarioAIConstants.LEVEL_CANNON ||
//			level.getLevelArray()[y-1][x+1] == ApoMarioAIConstants.LEVEL_CANNON ||
//			level.getLevelArray()[y+1][x+1] == ApoMarioAIConstants.LEVEL_CANNON ||
//			level.getLevelArray()[y][x] == ApoMarioAIConstants.LEVEL_QUESTIONMARKBOX ||
//			level.getLevelArray()[y-1][x] == ApoMarioAIConstants.LEVEL_QUESTIONMARKBOX||
//			level.getLevelArray()[y-1][x+1] == ApoMarioAIConstants.LEVEL_QUESTIONMARKBOX
			? true : false;
	}
}
