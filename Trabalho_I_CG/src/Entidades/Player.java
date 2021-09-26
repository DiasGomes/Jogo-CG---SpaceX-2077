package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Principal.Game;

public class Player extends Entity{
	
	

	public boolean right = false, left = false;
	public boolean shoot = false, hit = false, pisca = false;
	public int count  = 30, cooldown = 30;
	public int timer = 0, timerMax = 5, index = 3;
	public int fogo = 0, fogoMax = 5, indexFogo = 0;
	public int contDest = 0, contDestMax = 10, indexDest = 0;
	public boolean idaFogo = true;
	public int timerHit = 0, timerHitMax = 120;
	public int timerPisca = 0, timerPiscaMax = 3;
	public BufferedImage[] spriteMov, spriteFogo, spriteDestruido;
	public int vida = 3;
	public int pontos = 0;
	public double speedBullet = 10.0;
	public boolean destruido = false;
	public int dinheiro = 0;

	public Player(int x, int y, int width, int height, int dx, double speed) {
		super(x, y, width, height, dx, speed);
		//CARREGA OS SPRITES DO PLAYER
		spriteMov = new BufferedImage[7];
		for(int i = 0;i < 7;i++) {spriteMov[i] = Game.sheet.getSprite(32*i, 0, 32, 32);}
		spriteFogo = new BufferedImage[4];
		for(int n = 0;n < 4;n++) {spriteFogo[n] = Game.sheet.getSprite(300+(16*n), 12, 16, 16);}
		spriteDestruido = new BufferedImage[9];
		for(int j = 0;j < 9;j++) {spriteDestruido[j] = Game.sheet.getSprite(416+(32*j), 32, 32, 32);}
	}
	
	public void tick(){
		if(!destruido) {
			//MOVIMENTANDO O MINHA NAVE
			movePlayer();
			//ATIRANDO:
			playerAtira();
			//lÓGICA DE ANIMAÇÃO
			animaPlayer();
			//ANIMA O FOGO DO PLAYER
			animaFogo();
			//VERIFICANDO SE MINHA VIDA ZEROU
			estouVivo();
			//VERIFICA SE VENCEU/ DESTRUIU TODOS OS INIMIGOS
			venceu();
			//LÓGICA DO MEU PLAYER PISCAR AO SER ATINGIDO
			atingido();
		}else {
			//ANIMA O PLAYER DESTRUIDO
			animaDestruido();
		}
		
	}
	
	public void render(Graphics g) {
		//DESENHA MEU PLAYER
		if(!pisca) {
			g.drawImage(spriteMov[index], getX(), getY(), getWidth(), getHeight(), null);
			if(!destruido) {
				g.drawImage(spriteFogo[indexFogo], getX()+(getWidth()/4), getY()+getHeight(), getWidth()/2, getHeight()/2, null);
			}
		}
		if(destruido) {
			index = 3; //posição padrão da nave
			g.drawImage(spriteDestruido[indexDest], getX(), getY(), getWidth(), getHeight(), null);
		}
		
	}
	
	public void estouVivo() {
		//VERIFICA SE POSSUI VIDAS
		if(vida <= 0) {
			//Game.entidades.remove(this);
			destruido = true;
		}
	}
	
	public void playerAtira() {
		if(count < 30) {
			count++; 	//para o contador não ir para infinito
		}
		
		if(count >= cooldown && shoot) { //DEFINE UM DELAY ENTRE UM DISPARO E O OUTRO
			count = 0;
			//GERANDO UMA BULLET E ADICIONANDO A LISTA DE ENTIDADES
			Game.entidades.add(new Bullet(getX()+(Game.playerSize/2)-(Bullet.bulletSize/2),getY(),Bullet.bulletSize,Bullet.bulletSize,-1,speedBullet));
		}
	}
	
	public void atingido() {
		if(hit) {
			timerPisca++;
			timerHit++;
			
			if(timerHit >= timerHitMax) { //FAZ COMO QUE MEU PLAYER PISQUE NO INTERVALO
				timerHit = 0;
				hit = false;
			}
			
			if(timerPisca >= timerPiscaMax) {
				timerPisca = 0;
				if(pisca) { 
					pisca = false; //FAZ MEU PLAYER SER RENDERIZADO
				}else {
					pisca = true;	//FAZ MEU PLAYER NÃO SER RENDERIZADO
				}
			}
		}
	}
	
	public void venceu() {
		if(Game.inimigos.size() == 0) {
			Game.numFase++;
			Game.estado = "Loja";
		}
	}
	
	public void movePlayer() {
		if(x > 0 && left) {	//LIMITANDO PARA ESQUERDA
			setDx(-1);
		}else if(x < Game.WIDTH - Game.playerSize && right) { //LIMITANDO PARA DIREITA
			setDx(1);
		}else {	
			setDx(0); //EVITANDO DE ANDAR PARA SEMPRE
		}
		x += (speed*getDx()); // incrementando a posição
	}
	
	public void animaPlayer() {
		//OS VALORES DE CONTROLE ESTÃO RELACIONADO COM AS POSIÇÕES DAS IMAGENS E COM O TAMANHO DO SPRITE SHHET
		//ANIMANDO INDO PARA A ESQUERDA
				if(left) {
					timer++;
					if(timer >= timerMax) {
						timer = 0;
						if(index > 0) {
							index--;
						}
					}
				}else if(index < 3) {
					timer++;
					if(timer >= timerMax) {
						timer = 0;
						index++;
					}
				}
		//ANIMANDO INDO PARA A DIREITA		
				if(right) {
					timer++;
					if(timer >= timerMax) {
						timer = 0;
						if(index < 6) {
							index++;
						}
					}
				}else if(index > 3) {
					timer++;
					if(timer >= timerMax) {
						timer = 0;
						index--;
					}
				}
	}
	
	public void animaFogo() {
		fogo++;
		if(fogo >= fogoMax) {
			fogo = 0;
			
			if(idaFogo && indexFogo == 3) {
				idaFogo = false;
			}else if(!idaFogo && indexFogo == 0){
				idaFogo = true;
			}
			
			if(idaFogo) {
				indexFogo++;
			}else {
				indexFogo--;
			}
		}
	}
	
	public void animaDestruido() {
		contDest++;
		if(contDest >= contDestMax) {
			contDest = 0;
			if(indexDest < 8) {
				indexDest++;
			}else {
				indexDest = 5;
			}
		}
	}

}
