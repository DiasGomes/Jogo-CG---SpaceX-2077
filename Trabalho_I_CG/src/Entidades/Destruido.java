package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Principal.Game;

public class Destruido extends Entity{
	
	
	public int count = 0;
	public BufferedImage[] spriteBullet, spriteOponente1,spriteOponente2;
	public int timer = 0, timerMax = 3;
	public int index = 0;

	public Destruido(int x, int y, int width, int height, int dx, double speed) {
		super(x, y, width, height, dx, speed);
		//speed é equivalente ao número de sprites
		//dx é qual vetor de BufferedImage;
		//0 é uma bullet
		//CARREGA OS SPRITES DA MINHA BULLET
		spriteBullet = new BufferedImage[4];
		for(int i = 0;i < 4;i++) {spriteBullet[i] = Game.sheet.getSprite(265 + (8*i), 11, 8, 12);}
		spriteOponente1 = new BufferedImage[8];
		for(int i = 0;i < 8;i++) {spriteOponente1[i] = Game.sheet.getSprite(32*i, 96, 32, 32);}
		spriteOponente2 = new BufferedImage[11];
		for(int i = 0;i < 11;i++) {spriteOponente2[i] = Game.sheet.getSprite(256+(32*i), 96, 32, 32);}
	}
	
	public void tick() {
		
		animaDestruido();
			
	}
	
	public void render(Graphics g) {
		//DESENHA MINHA BULLET
		switch(getDx()) {
		case 0: g.drawImage(spriteBullet[index], getX(), getY(), 8, 12, null); //DESENHA A BULLET SENDO DESTRUIDA
			break;
		case 1: g.drawImage(spriteOponente1[index], getX(), getY(), getWidth(), getHeight(), null); //DESENHA A BULLET SENDO DESTRUIDA
			break;
		case 2: g.drawImage(spriteOponente2[index], getX(), getY(), getWidth(), getHeight(), null); //DESENHA A BULLET SENDO DESTRUIDA
			break;
		}
	}
	
	public void animaDestruido() {
		timer++;
		if(timer >= timerMax) {
			timer = 0;
			if(index < getSpd()-1) {
				index++;	//TROCA OS SPRITES
			}else {
				Game.entidades.remove(this); //REMOVE A ENTIDADE DEPOIS DE FAZER A ANIMAÇÃO
			}
		}
	}

}
