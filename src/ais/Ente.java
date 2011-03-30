package ais;

import java.util.ArrayList;
import apoMario.ai.ApoMarioAI;
import apoMario.ai.ApoMarioAIConstants;
import apoMario.ai.ApoMarioAIEnemy;
import apoMario.ai.ApoMarioAILevel;
import apoMario.ai.ApoMarioAIPlayer;

public class Ente extends ApoMarioAI{
	
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
		
		int x = (int)(player.getX()+0.5);
		int y = player.getType() > 0 ? (int)(player.getY()+1.5) : (int)(player.getY()+0.9);

		int direction = search(x, y, level, player);
		
		player.runRight();
		player.runFast();
		
		if (direction == 3) {
			player.jump();
			player.runRight();
			player.runFast();
		}
		else if (direction == 2) {
			player.runRight();
			player.runFast();
		}
		else if (direction == 1) {
			player.runRight();
			player.runFast();
		}
		else if (direction == 4) {
//			player.runStand();
			player.jump();
			player.drawRect(x, 3, 1, 1, true, 5000);
		}
		
		player.addMessage("Richtung:" + String.valueOf(direction));
		player.drawRect(x, y, 1, 1, false, 20);
			
	}

	
	public static int search(int x, int y, ApoMarioAILevel level, ApoMarioAIPlayer player) {
		
		int z = x + 8;
		int j = player.getVecY() > 0.02 ? -4 : player.getVecY() > 0.01 ? -3 : player.getVecY() > 0 ? -1 : player.getVecY() == 0 ? 5 : player.getVecY() > -0.01 ? 2 : player.getVecY() > -0.015 ? 3 : player.getVecY() > -0.02 ? 4 : 5;
		
		int up = j <= 0 ? 1000 : rec(j>3 ? x : x+1, y-1, z, j-1, level)+1;
		int forward = !collision(x+1, y+1, level) || collision(x+1, y, level) ? 1000 : rec(x+1, y, z, j, level)+1;
		int down = collision(x+1, y, level) ? 1000 : rec(x+1, y+1, z, j > 0 ? 0 : j-1, level)+1;
		player.addMessage(String.valueOf(j));
		if (y < 2) return 1;
		if (collision(x,y,level) || up >= 1000 && forward >= 1000 && down >= 1000) return 4;
//		if (level.getLevelArray()[14][x+1] == ApoMarioAIConstants.LEVEL_EMPTY) return 3;
//		if (level.getLevelArray()[14][x+2] == ApoMarioAIConstants.LEVEL_EMPTY) return 3;
		
		return (up < forward) && (up < down) ? 3 : (forward < down) ? 2 : 1;
	}
	
	private static int rec(int x, int y, int z, int j, ApoMarioAILevel level) {
		
		if (x >= z) 
			return 1;
		else if (isEnemy(x, y, level))
			return 1000;
		else if (collision(x, y, level) || y <= 2 || y >= 14)
			return 1000;
		else {
			j = collision(x, y+1, level) ? 5 : j;
			int up = j <= 0 || collision(x, y-2, level) ? 1000 : rec(x+1, y-1, z, j-1, level)+1;
			int forward = !collision(x+1, y+1, level) || collision(x+1, y, level) ? 1000 : rec(x+1, y, z, j, level)+1;
			int down = collision(x+1, y, level) ? 1000 : rec(j<-4 ? x : x+1, y+1, z, j > 0 ? 0 : j-1, level)+1;
			
			return (up < forward) && (up < down) ? up : (forward < down) ? forward : down;
		}
	}
	
	private static boolean collision(int x, int y, ApoMarioAILevel level) {
		
		return 
			level.getLevelArray()[y][x] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y-1][x] != ApoMarioAIConstants.LEVEL_EMPTY 
//			level.getLevelArray()[y-1][x+1] != ApoMarioAIConstants.LEVEL_EMPTY ||
//			level.getLevelArray()[y][x+1] != ApoMarioAIConstants.LEVEL_EMPTY
			? true : false;
	}

	private static boolean isEnemy(int x, int y, ApoMarioAILevel level) {
		
		ArrayList<ApoMarioAIEnemy> list = level.getEnemies(); 
		
		for (int i = 0; i < list.size(); i++)
			if ((int)list.get(i).getX()-x <= 1 && (int)list.get(i).getX()-x >= -1 && (int)list.get(i).getY()-y <= 1 && (int)list.get(i).getY()-y >= -1 && !list.get(i).isFireballType()) return true;
		
		return false;
	}
}