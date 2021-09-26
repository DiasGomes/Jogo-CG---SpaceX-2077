package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Principal.Game;

public class Bullet extends Entity{
	
	public static int bulletSize = 8;
	public BufferedImage[] spriteB;
	public int index = 0;
	public int timer = 0, timerMax = 5;
	
	public Bullet(int x, int y, int width, int height, int dx, double speed) {
		super(x, y, width, height, dx, speed);
		//CARREGA OS SPRITES DA MINHA BULLET
		spriteB = new BufferedImage[4];
		for(int i = 0;i < 4;i++) {spriteB[i] = Game.sheet.getSprite(224 + (8*i), 10, 8, 12);}
		
	}

	public void tick() {
		y+=(speed*getDx());	//MOVENDO A BULLET
		
		if(y < 0 || y > Game.HEIGHT) {
			Game.entidades.remove(this); 	//DESTRUINDO A BULLET DEPOIS DE SAIR DA TELA
		}
		if(getDx() == -1 && Game.player.destruido == false) {	//SE A BALA ESTÁ SUBINDO É PQ O PLAYER ATIROU
			destroiOponente1();	//VERIFICA SE MINHA BULLET ESTÁ EM COLISÃO COM UM OPONENETE1
			destroiOponente2();	//VERIFICA SE MINHA BULLET ESTÁ EM COLISÃO COM UM OPONENETE2
			destroiKamikazi();	////VERIFICA SE MINHA BULLET ESTÁ EM COLISÃO COM UM KAMIKAZI
		}else if(getDx() == 1) {	//SE A BALA ESTÁ DESCENDO É PQ O OPONENTE1 ATIROU
			danoPlayer(); //VERIFICA SE ABULLET ESTÁ EM COLISÃO COM O PLAYER
		}
		//VERIFICA SE ACERTOU OUTRA BULLET
		destroiBullet();
		//LÓGICA DE ANIMAÇÃO DA BALA
		animaBullet();
	}
	
	public void render(Graphics g) {
		//DESENHA MINHA BULLET
		if(getDx() == 1) {	//ATIRADA PELO INIMIGO
			g.drawImage(spriteB[index], getX(), getY()+12, 8, -12, null); //DESENHA A IMAGEM INVERTIDA PARA DAR A IDEIA DE IR PARA BAIXO
		}else if(getDx() == -1) { // ATIRADA PELO PLAYER
			g.drawImage(spriteB[index], getX(), getY(), 8, 12, null);
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
	
	public void destroiOponente1() {
		//VERIFICA SE ATINGIU O OPONENTE1
		for(int i = 0;i < Game.entidades.size();i++) {
			Entity e = Game.entidades.get(i);
			if(e instanceof Oponente1) { //VERIFICANDO O TIPO DA MINHA ENTIDADE
				if(isColliding(this, e)) {
					Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));
					Game.entidades.add(new Destruido(e.getX(),e.getY(),e.getWidth(),e.getHeight(),1,8));
					Game.entidades.remove(this); //DESTRUINDO A BULLET
					//DESTRUINDO OPONENTE1
					Game.player.pontos += e.getY()*2; //O NÚMERO DE PONTOS É A POSIÇÃO DO Y MULTIPLICADA POR UM PESO
					Game.player.dinheiro++; //player ganha dinheiro
					Game.inimigos.remove(e);
					Game.entidades.remove(e);
				}
			}
		}
	}
	
	public void destroiOponente2() {
		//VERIFICA SE ATINGIU O OPONENTE1
		for(int i = 0;i < Game.entidades.size();i++) {
			Entity e = Game.entidades.get(i);
			if(e instanceof Oponente2) { //VERIFICANDO O TIPO DA MINHA ENTIDADE
				if(isColliding(this, e)) {
					Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));
					Game.entidades.add(new Destruido(e.getX(),e.getY(),e.getWidth(),e.getHeight(),2,11));
					//DESTRUINDO A BULLET
					Game.entidades.remove(this); 
					//O NÚMERO DE PONTOS É A POSIÇÃO DO Y MULTIPLICADA POR UM PESO
					Game.player.pontos += e.getY()*3; 
					Game.player.dinheiro++; //player ganha dinheiro
					//CRIANDO UM OPONENTE1
					Oponente1 o = new Oponente1(e.getX(),e.getY(),e.getWidth(),e.getHeight(),e.getDx(),Game.speedIni);
					Game.inimigos.add(o);
					Game.entidades.add(o);
					//DESTRUINDO OPONENTE2
					Game.inimigos.remove(e);
					Game.entidades.remove(e);
				}
			}
		}
	}
	
	public void destroiKamikazi() {
		//VERIFICA SE ATINGIU O OPONENTE1
		for(int i = 0;i < Game.entidades.size();i++) {
			Entity e = Game.entidades.get(i);
			if(e instanceof Kamikazi) { //VERIFICANDO O TIPO DA MINHA ENTIDADE
				if(isColliding(this, e)) {
					Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));
					Game.entidades.remove(this); //DESTRUINDO A BULLET
					//DESTRUINDO KAMIKAZI
					Game.player.pontos += e.getY()*2; //O NÚMERO DE PONTOS É A POSIÇÃO DO Y MULTIPLICADA POR UM PESO
					Game.player.dinheiro++; //player ganha dinheiro
					Game.inimigos.remove(e);
					Game.entidades.remove(e);
				}
			}
		}
	}
	
	public void danoPlayer() {
		//VERIFICA SE COLIDIU COM O PLAYER && SE MEU PLAYER NÃO FOI ATINGIDO RECENTEMENTE
		if(isColliding(this, Game.player) && Game.player.hit == false) { 
			Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));
			Game.player.hit = true; //INDICA QUE MEU PLAYER FOI ATINGIDO
			Game.entidades.remove(this);//REMOVE A BULLET
			Game.player.vida--;	//RETIRA UM DE VIDA DO PLAYER
		}
	}
	
	public void destroiBullet() {
		//VERIFICA SE COLIDIU COM UMA OUTRA BULLET
		for(int i = 0;i < Game.entidades.size();i++) {
			Entity e = Game.entidades.get(i);
			if(e instanceof Bullet) { //VERIFICANDO O TIPO DA MINHA ENTIDADE
				if(isColliding(this, e)) {
					if(this != e) {	//VERIFICA SE NÃO É A PROPRIA BULLET
						//GERANDO O EFITO DE DESTRUIDO
						Game.entidades.add(new Destruido(getX(),getY(),getWidth(),getHeight(),0,4));
						Game.entidades.add(new Destruido(e.getX(),e.getY(),e.getWidth(),e.getHeight(),0,4));
						//DESTRUINDO AS BULLETs
						Game.entidades.remove(this); 
						Game.entidades.remove(e);
					}
				}
			}
		}
	}

}
