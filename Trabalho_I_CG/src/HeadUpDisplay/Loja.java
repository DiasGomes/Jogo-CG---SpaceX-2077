package HeadUpDisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Principal.Game;

public class Loja {
	
	public boolean up = false, down = false, enter = false, comprou = false;
	public int index = 0;
	public int indexMoeda = 0, timer = 0, timerMax = 5;
	public int[] itens = {15,20,10,25,0};
	public String[] descricao = {"Mais uma vida",
			"Inimigos lentos por uma rodada",
			"Aumenta velocidade da Nave",
			"Diminui tempo de disparo",
			"Saída"};
	public BufferedImage vida,speedIni,speedPlayer,speedBullet, saida, fundo;
	public BufferedImage[] moeda;
	public int step = 80,size = 64;
	public int px = (Game.WIDTH/2)-(size), py = 120;
	
	public Loja() {
		try {
			fundo = ImageIO.read(getClass().getResource("/Imagens/Loja.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vida = Game.sheet.getSprite(520, 9, 13, 13);
		speedIni = Game.sheet.getSprite(0, 32, 32, 32);
		speedPlayer = Game.sheet.getSprite(0, 0, 32, 32);
		speedBullet = Game.sheet.getSprite(497, 6, 14, 14);
		saida = Game.sheet.getSprite(539, 9, 15, 15);
		moeda = new BufferedImage[7];
		for(int i = 0; i < 7; i++) {moeda[i] = Game.sheet.getSprite(512+(32*i), 256, 32, 32);}
	}
	
	public void tick() {
		//LÓGICA DAS OPÇÕES DA LOJA
		if(up) {
			up = false;
			if(index > 0) {
				index--;
			}
		}else if(down){
			down = false;
			if(index < itens.length-1) {
				index++;
			}
		}
		// LÓGICA DA COMPRA/CONFIRMAÇÃO
		if(enter) { //SE CONFIRMOU
			enter = false;
			if(Game.player.dinheiro >= itens[index]) {//SE TEM DINHEIRO
				
				switch(index) {
				case 0: //concede vida ao meu player
					if(Game.player.vida < 3) {
						Game.player.vida++;
						Game.player.dinheiro -= itens[0];
					}
				break;
				
				case 1: //evita que meus inimigosa fiquem mais rápidos no proximo round
					if(Game.aumentaVelocidade) {
						Game.aumentaVelocidade = false;
						Game.player.dinheiro -= itens[1];
					}
				break;
				
				case 2: //AUMENTA A VELOCIDADE DO PLAYER
					Game.player.setSpd(Game.player.getSpd()+0.5);
					Game.player.dinheiro -= itens[2];
				break;
				
				case 3: //DIMINUI O TEMPO DE UM DISPARO E OUTRO
					if(Game.player.cooldown > 10) {
						Game.player.cooldown--;
						Game.player.dinheiro -= itens[3];
					}
				break;
				
				case 4: //FECHA A LOJA E VAI PARA A PROXIMA FASE
					index = 0; //Game.nextLevel(); 
					Game.estado = "Fase";
				break; 
			}
				
			}
			
			
		}
		//ANIMA MOEDA
		animaMoeda();
	}
	
	public void render(Graphics g) {
		//BACKGROUND
		g.drawImage(fundo, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(px-(size/2), py, (size/2)*7, size*(itens.length+1));
		g2.fillRect(0, Game.HEIGHT-size, Game.WIDTH, size);
		//TÍTULO:
		g.setColor(Color.YELLOW);
		g.setFont(new Font("comic sans ms", Font.BOLD, 80));
		g.drawString("Loja",160, 80);
		//DESENHANDO DINHEIRO DO PLAYER
		g.setColor(Color.ORANGE);
		g.setFont(g.getFont().deriveFont(40.0f));
		g.drawString(Integer.toString(Game.player.dinheiro), 20+size, 70);
		g.drawImage(moeda[indexMoeda],20,20,size,size,null);
		//DESENHANDO OPÇÕES
			//VIDA
			g.drawImage(vida,px,py,size,size,null);
			g.drawImage(moeda[indexMoeda],px+size,py,size,size,null);
			g.drawString(Integer.toString(itens[0]), px+(size*2),py+50);
			//LENTIDÃO DO INIMIGO
			g.drawImage(speedIni,px,py+step,size,size,null);
			g.drawImage(moeda[indexMoeda],px+size,py+step,size,size,null);
			g.drawString(Integer.toString(itens[1]), px+(size*2),py+50+step);
			//VELOCIDADE NAVE
			g.drawImage(speedPlayer,px,py+(step*2),size,size,null);
			g.drawImage(moeda[indexMoeda],px+size,py+(step*2),size,size,null);
			g.drawString(Integer.toString(itens[2]), px+(size*2),py+50+(step*2));
			//VELOCIDADE BULLET
			g.drawImage(speedBullet,px,py+(step*3),size,size,null);
			g.drawImage(moeda[indexMoeda],px+size,py+(step*3),size,size,null);
			g.drawString(Integer.toString(itens[3]), px+(size*2),py+50+(step*3));
			//SAÍDA
			g.drawImage(saida,px,py+(step*4),size,size,null);
		//DESNHANDO ESCOLHA
		g.setColor(Color.YELLOW);
		g.drawRect(px, py+(step*index), size, size);
		//DESENHANDO DINHEIRO DO PLAYER
		g.setFont(g.getFont().deriveFont(20.0f));
		g.drawString("> " + descricao[index], 40,  Game.HEIGHT-(size/2));
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(12.0f));
		g.drawString("[ENTER] PARA CONFIRMAR", 170,  Game.HEIGHT-100);
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
