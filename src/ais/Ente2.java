package ais;

import java.util.ArrayList;

import apoMario.ai.ApoMarioAI;
import apoMario.ai.ApoMarioAIConstants;
import apoMario.ai.ApoMarioAIEnemy;
import apoMario.ai.ApoMarioAILevel;
import apoMario.ai.ApoMarioAIPlayer;

public class Ente2 extends ApoMarioAI{
	
	@Override
	public String getTeamName() {
		return "Ente2";
	}

	@Override
	public String getAuthor() {
		return "Martin Knoll";
	}

	@Override
	public void think(ApoMarioAIPlayer player, ApoMarioAILevel level) {
		
		int x = (int)(player.getX()+0.5);
		int y = player.getType() > 0 ? (int)(player.getY()+1.5) : (int)(player.getY()+0.9);
		
		int z = x + 8;
		
		int j = player.getVecY() > 0.02 ? -4 : player.getVecY() > 0.01 ? -3 : player.getVecY() > 0 ? -1 : player.getVecY() == 0 ? 5 : player.getVecY() > -0.01 ? 2 : player.getVecY() > -0.015 ? 3 : player.getVecY() > -0.02 ? 4 : 5;
		
		player.runRight();
		player.runFast();
		
		if (search(x, y, z, j, level))
			player.jump();
	}

	
	public static boolean search(int x, int y, int z, int j, ApoMarioAILevel level) {

		if (j <= 0)
			return false;

		return !(rec(x+1, y, z, j, level) || rec(x+1, y+1, z, j > 0 ? 0 : j-1, level));
	}
	
	public static boolean rec(int x, int y, int z, int j, ApoMarioAILevel level) {
		
		if (x >= z) 
			return true;
		
		if (collision(x,y,level) || collision(x,y-1,level) || isEnemy(x, y, level) || y <= 2 || y >= 14) 
			return false;
		
		j = collision(x, y+1, level) ? 5 : j;
		
		return (j <= 0 ? false : rec(j>3 ? x : x+1, y-1, z, j-1, level)) || (!collision(x+1, y+1, level) ? false : rec(x+1, y, z, j, level)) || rec(j<-4 ? x : x+1, y+1, z, j > 0 ? 0 : j-1, level);
	}
	
	private static boolean collision(int x, int y, ApoMarioAILevel level) {
		
		return 
			level.getLevelArray()[y][x] != ApoMarioAIConstants.LEVEL_EMPTY ||
			level.getLevelArray()[y-1][x] != ApoMarioAIConstants.LEVEL_EMPTY 
			? true : false;
	}

	private static boolean isEnemy(int x, int y, ApoMarioAILevel level) {
		
		ArrayList<ApoMarioAIEnemy> list = level.getEnemies(); 
		
		for (int i = 0; i < list.size(); i++)
			if ((int)list.get(i).getX()-x <= 1 && (int)list.get(i).getX()-x >= -1 && (int)list.get(i).getY()-y <= 1 && (int)list.get(i).getY()-y >= -1 && !list.get(i).isFireballType()) return true;
		
		return false;
	}
}