package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Principal.Game;

public class Kamikazi extends Entity{
	
	public BufferedImage[] spriteB, spriteOponente;
	public int index = 0;
	public int timer = 0, timerMax = 4;
	
	public double distanciaX, distanciaY;
	public double dirX, dirY;
	public int xFinal, yFinal,xInicial,yInicial, xDistancia, yDistancia;
	public double angle;

	public Kamikazi(int x, int y, int width, int height, int dx, double speed) {
		super(x, y, width, height, dx, speed);
		//DX É QUAL VETOR DE BUFFEREDIMAGES 
		//0 é uma bullet
		//1 é o oponente 
		//CARREGA OS SPRITES DA MINHA BULLET
		spriteB = new BufferedImage[4];
		for(int i = 0;i < 4;i++) {spriteB[i] = Game.sheet.getSprite(224 + (8*i), 10, 8, 12);}
		spriteOponente = new BufferedImage[7];
		for(int n = 0;n < 7;n++) {spriteOponente[n] = Game.sheet.getSprite(32*n, 32, 32, 32);}
		//CALCULA A DISTANCIA DO PLAYER
		distancia();
	}
	
	public void distancia() {
		//DEFINE O PERCURSO ATÉ A POSIÇÃO DO PLAYER NO INSTANTE DA DECISÃO
		xInicial = getX() +(getWidth()/2);
		yInicial = getY() + (getHeight()/2);
		yFinal = (Game.player.getY()+(Game.player.getHeight()/2));
		xFinal = (Game.player.getX()+(Game.player.getWidth()/2));
		xDistancia = xFinal - xInicial;
		yDistancia = yFinal - yInicial;
		angle = Math.atan2(yFinal-(y+(getHeight()/2)), xFinal-(x+(getWidth()/2)));
		dirX = Math.cos(angle);
		dirY = Math.sin(angle);
	}
	
	public void rasante() {
		//FAZ MINHA ENTIDADE ANDAR
		x += getSpd()*dirX; 
		y += getSpd()*dirY; 
	}
	
	public void destroi() {
		//VERIFICA SE COLIDIU COM MEU PLAYER E QUE ELE NÃO FOI ATINGIDO RECENTEMENTE
		if(isColliding(this, Game.player) && Game.player.hit == false) {
			Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));	//CRIA UMA ANIMAÇÃO DE DESTRUIÇÃO
			Game.entidades.remove(this);	//DESTROI O KAMIKAZI
			Game.player.vida--;	//TIRA A VIDA DO MEU PLAYER
			Game.player.hit = true; //INDICA QUE MEU PLAYER FOI ATINGIDO
		}
		//DESTROI ESSA ENTIDADE SE PASSAR DA TELA
		if(y > Game.HEIGHT || y+getHeight() < 0 || x + getWidth() < 0 || x > Game.WIDTH) {
			Game.entidades.remove(this);
		}
	}
	
	public void tick() {
		rasante();
		destroi();
		switch(getDx()){
		case 0: animaBullet(); break;
		case 1: animaOponente();break;
	}
	}
	
	public void render(Graphics g) {
		
		switch(getDx()){
			case 0: 
				g.drawImage(spriteB[index], getX(), getY()+12, 8, -12, null); //DESENHA A IMAGEM INVERTIDA PARA DAR A IDEIA DE IR PARA BAIXObreak;
				break;
			case 1: 
				g.drawImage(spriteOponente[index], getX(), getY(), getWidth(), getHeight(), null);
				break;
		}
		
	}
	
	public void animaBullet() {
		timer++;
		if(timer >= timerMax) {
			timer = 0;
			if(index < 3) {
				index++;
			}
		}
	}
	
	public void animaOponente() {
		timer++;
		if(timer >= timerMax) {
			timer = 0;
			if(index < 6) {
				index++;
			}
		}
	}


}
