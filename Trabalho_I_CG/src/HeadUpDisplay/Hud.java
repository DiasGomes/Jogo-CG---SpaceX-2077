package HeadUpDisplay;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import Principal.Game;

public class Hud {
	
	public String pts = "0000";
	public BufferedImage[] vidas, moeda;
	public int index = 0,sizeX = 46*3,sizeY = 11*3;
	public int indexMoeda = 0, timer = 0, timerMax = 5;
	public static Font fontePersonalizada;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Fontes/Orbitron-Black.ttf");
	
	
	public Hud() {
		//RESEBENDO SPRITES QUE MOSTRAM AS VIDAS E A MOEDA
		vidas = new BufferedImage[4];
		for(int i = 0;i<4;i++) {vidas[i] = Game.sheet.getSprite(300+(46*i), 0, 46, 11);}
		moeda = new BufferedImage[7];
		for(int i = 0; i < 7; i++) {moeda[i] = Game.sheet.getSprite(512+(32*i), 256, 32, 32);} 
		//fontes personalizadas:
		try {
			fontePersonalizada = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		//FORMATANDO A APRESENTAÇÃO DA PONTUAÇÃO
		if(Game.player.pontos > 0) {
			if(Game.player.pontos < 100){
				pts = "00" + Game.player.pontos;
			}else if(Game.player.pontos < 1000){
				pts = "0" + Game.player.pontos;
			}else {
				pts = Integer.toString(Game.player.pontos);
			}
		}else {
			pts = "0000";
		}
		//TROCA O SPRITE A PARTIR DA VIDA QUE POSSUI
		index = Game.player.vida;
		//ANIMA MOEDA
		animaMoeda();
	}
	
	public void render(Graphics g) {
		if(Game.estado == "Jogo") {
			//BACKGROUND DO HUD
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(50,50,50,80));
			g2.fillRect(0, Game.HEIGHT-60, Game.WIDTH, 60);
			//DESENHANDO A PONTUAÇÃO DO JOGADOR
			g.setColor(Color.YELLOW);
			g.setFont(fontePersonalizada);
			g.drawString(pts, 40, Game.HEIGHT-20);
			//DESENHADO QUANTIDADE DE VIDAS DO PLAYER
			g.drawImage(vidas[index],Game.WIDTH-sizeX-40,Game.HEIGHT-sizeY-15,sizeX,sizeY,null);
			//DESENHANDO DINHEIRO DO PLAYER
			g.drawString(Integer.toString(Game.player.dinheiro), 220, Game.HEIGHT-20);
			g.drawImage(moeda[indexMoeda],170, Game.HEIGHT-56,48,48,null);
		}
	}
	
	public void animaMoeda() {
		timer++;
		if(timer >= timerMax) {
			timer = 0;
			if(indexMoeda < 6) {
				indexMoeda++;
			}else {
				indexMoeda = 0;
			}
		}
	}

}
