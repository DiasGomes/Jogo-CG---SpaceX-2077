package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Principal.Game;

public class Oponente2 extends Entity{
	
	public int dx = 0;
	public int size = Game.sizeIni;
	public int stepY = 16;
	public int contAtira = 0, contAtiraMax = 120+ Game.rand.nextInt(120);
	public BufferedImage[] sprite;
	public int timer = 0, timerMax = 4, index = 0;
	public boolean atirando = false;
	
	public Oponente2(int x, int y, int width, int height, int dy, double speed) {
		super(x, y, width, height, dy, speed);
		//CARREGA OS SPRITES DO OPONENTE2
		sprite = new BufferedImage[13];
		for(int i = 0; i < 13;i++){sprite[i] = Game.sheet.getSprite(32*i,64, 32, 32);}
	}
	
	public void tick() {
		if(!Game.player.destruido) {
		//FAZENDO MEUS OPONENTES IREM PARA BAIXO AO TOCAR NA PAREDE
			changeDirection();
			//FAZENDO O OPONENETE ATIRAR E ANIMANDO ESSA AÇÃO
			atiraInimigo();
			atiraAnima();
			//MOVIMENTANDO MEU OPONENTE PARA OS LADOS:
			x += (getDx()*speed); 
			//VERIFICA SE CHEGOU NO FINAL DA TELA
			chegouFinal();
		}
	}
	
	public void render(Graphics g) {
		//DESENHA O OPONENTE2
		g.drawImage(sprite[index], getX(), getY(), getWidth(), getHeight(), null);
		
	}
	
	public void changeDirection() {
		if(x <= 0){	//UM DOS OPONENTESCHEGARAM NA ESQUERDA DA TELA
			for(int i = 0;i < Game.inimigos.size();i++) {	//LOOP PARA QUE AS AÇÕES AFETEM TODOS OS INIMIGOS
				Game.inimigos.get(i).setDx(1);	//TODAS OS INIMIGOS VÃO PASSAR A IR PARA DIREITA
				Game.inimigos.get(i).setY(Game.inimigos.get(i).getY() + stepY); //TODAS AS ENTIDADES VÃO ANDAR PARA FRENTE
			}
		}else if(x + size >= Game.WIDTH){	//UM DOS OPONENTESCHEGARAM NA DIREITA DA TELA
			for(int n = 0;n < Game.inimigos.size();n++) {	//LOOP PARA QUE AS AÇÕES AFETEM TODOS OS INIMIGOS
				Game.inimigos.get(n).setDx(-1);	//TODAS OS INIMIGOS VÃO PASSAR A IR PARA ESQUERDA
				Game.inimigos.get(n).setY(Game.inimigos.get(n).getY() + stepY);	//TODAS AS ENTIDADES VÃO ANDAR PARA FRENTE
			}
		}
		
	}
	
	public void atiraInimigo(){
		contAtira++;
		if(contAtira >= contAtiraMax) {
			contAtira = 0;
			if(Game.rand.nextInt(10) < 2){
				atirando = true;	//ALGUMAS DAS ENTIDADES ATIRARAM ALEATORIAMENTE
			}
			
		}
	}
	
	public void atiraAnima() {
		if(atirando){
			timer++;
			if(timer >= timerMax) {
				timer = 0;
				if(index < 12){
					index++;
				}else{
					//GERA A BALA NO ÚLTIMO INDEX PARA CONCILIAR COM A ANIMAÇÃO DOS SPRITES
					Game.entidades.add(new Kamikazi(getX()+(getWidth()/2)-(Bullet.bulletSize/2),getY()+ getHeight(),Bullet.bulletSize,Bullet.bulletSize,0,10));
					index = 0;
					atirando = false; //PARA QUE A ANIMÇÃO NÃO ENTRE EM LOOP / SÓ QUANDO ATIRAR
				}
			}
		}
	}
	
	public void chegouFinal() {
		for(int j = 0;j < Game.inimigos.size();j++) {
			if(Game.inimigos.get(j).getY() >= Game.player.getY() && Game.inimigos.get(j).getDx() != 2) {
				Game.estado = "GameOver";
			}
		}
	}

}
