package Entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Principal.Game;

public class Oponente1 extends Entity{
	
	public int dx = 0;
	public int size = Game.sizeIni;
	public int stepY = 16;
	public int contAtira = 0, contAtiraMax = 120+ Game.rand.nextInt(120);
	public BufferedImage[] spriteAtk;
	public int timer = 0, timerMax = 5, index = 0;
	public boolean atirando = false;
	
	public Oponente1(int x, int y, int width, int height, int dx, double speed) {
		super(x, y, width, height, dx, speed);
		//CARREGA OS SPRITES DO OPONENTE1
		spriteAtk = new BufferedImage[13];
		for(int i = 0; i < 13;i++){spriteAtk[i] = Game.sheet.getSprite(32*i,32, 32, 32);}
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
		//DESENHA O OPONENTE1
		g.drawImage(spriteAtk[index], getX(), getY(), getWidth(), getHeight(), null);
		
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
					Game.entidades.add(new Bullet(getX()+(getWidth()/2)-(Bullet.bulletSize/2),getY()+ getHeight(),Bullet.bulletSize,Bullet.bulletSize,1,10));
					index = 0;
					atirando = false; //PARA QUE A ANIMÇÃO NÃO ENTRE EM LOOP / SÓ QUANDO ATIRAR
				}
			}
		}
	}
	
	public void chegouFinal() {
		for(int j = 0;j < Game.inimigos.size();j++) {
			if(Game.inimigos.get(j).getY() >= Game.player.getY()) {
				Game.estado = "GameOver";
			}
		}
	}

}
