package HeadUpDisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import Principal.Game;

public class Menu {
	
	public boolean up = false, down = false,enter = false;
	public boolean right = false, left = false;
	public String estado = "Menu";
	public int index = 0, indexControles = 0;
	public int step = 40, poY = Game.HEIGHT/2, poX= (Game.WIDTH/2)-50;
	public static Font fontePersonalizada;
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Fontes/space age.ttf");
	public BufferedImage fundoMenu;
	
	
	public Menu() {
		//fontes personalizadas:
				try {
					fontePersonalizada = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
				} catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
				
				try {
					fundoMenu = ImageIO.read(getClass().getResource("/Imagens/fundoMenu.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
	}
	
	public void tick() {
		if(estado == "Menu"){
			//CONTROLA AS OPÇÕES DISPONÍVEIS DO MENU
			if(up) {
				up = false;
				if(index == 0) {
					index = 3;
				}else {
					index--;
				}
			}else if(down) {
				down = false;
				if(index == 3) {
					index = 0;
				}else {
					index++;
				}
			}
			
			if(enter) {	//FAZ A AÇÃO PEDIDA PELO USUÁRIO
				enter = false;
					switch(index) {
						case 0: Game.resetar();	break;
						case 1: estado = "Controles"; break;
						case 2: estado = "Creditos"; break;
						case 3: System.exit(1);	break;
				}
			}
			
		}else if(estado == "Creditos"){	//FAZ COMO QUE MEUS CRÉDITOS APAREÇAM
			if(enter) {
				enter = false;
				estado = "Menu";
			}
		}else if(estado == "Controles"){	//FAZ COMO QUE OS CONTROLES APAREÇAM
			if(right) {
				right = false;
				if(indexControles < 1) {
					indexControles++;
				}
			}else if(left) {
				left = false;
				if(indexControles > 0) {
					indexControles--;
				}
			}
			
			if(enter) {
				if(indexControles == 1){
					Game.controle = "Mouse";
				}else {
					Game.controle = "Teclado";
				}
				enter = false;
				estado = "Menu";
			}
		}
	}
	
	public void render(Graphics g) {
		//BACKGROUND
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.drawImage(fundoMenu, 0, 0, Game.WIDTH, Game.HEIGHT, null);
		//TÍTULO:
		g.setColor(Color.WHITE);
		g.setFont( fontePersonalizada);
		g.setFont(g.getFont().deriveFont(80.0f));
		g.drawString("SpaceX",30, 100);
		g.drawString("2077",100, 150);
		//DESENHANDO "BOTÕES"
		g.setFont(new Font("comic sans ms",Font.BOLD,30));
		//g.setFont(new Font("bahnschrift",Font.BOLD,30));
				if(estado == "Menu") {
					switch(index) {
					case 0: 
						g.setColor(Color.YELLOW);
						g.setFont(g.getFont().deriveFont(40.0f));
						g.drawString("Jogar", poX, poY);	
						g.setColor(Color.WHITE);
						g.setFont(g.getFont().deriveFont(30.0f));
						g.drawString("Controles", poX, poY+step);
						g.drawString("Créditos", poX, poY+(step*2));
						g.drawString("Sair", poX, poY+(step*3));
						break;
					case 1: 
						g.setColor(Color.YELLOW);
						g.setFont(g.getFont().deriveFont(40.0f));
						g.drawString("Controles", poX, poY+step);	
						g.setColor(Color.WHITE);
						g.setFont(g.getFont().deriveFont(30.0f));
						g.drawString("Jogar", poX, poY);
						g.drawString("Créditos", poX, poY+(step*2));
						g.drawString("Sair", poX, poY+(step*3));
						break;
					case 2: 
						g.setColor(Color.YELLOW);
						g.setFont(g.getFont().deriveFont(40.0f));
						g.drawString("Créditos", poX, poY+(step*2));
						g.setColor(Color.WHITE);
						g.setFont(g.getFont().deriveFont(30.0f));
						g.drawString("Jogar", poX, poY);
						g.drawString("Controles", poX, poY+step);
						g.drawString("Sair", poX, poY+(step*3));
						break;
					case 3:	
						g.setColor(Color.YELLOW);
						g.setFont(g.getFont().deriveFont(40.0f));
						g.drawString("Sair", poX, poY+(step*3));
						g.setColor(Color.WHITE);
						g.setFont(g.getFont().deriveFont(30.0f));
						g.drawString("Jogar", poX, poY);
						g.drawString("Controles", poX, poY+step);
						g.drawString("Créditos", poX, poY+(step*2));
						break;
				}
			}else if(estado == "Creditos"){
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0,150));
				g2.fillRect(0, 200, Game.WIDTH, 350);
				g.setColor(Color.WHITE);
				g.setFont(g.getFont().deriveFont(20.0f));
				g.drawString("DESENVOLVIDO POR:", poX-60, poY-(step*2));
				g.setFont(g.getFont().deriveFont(25.0f));
				g.drawString("> João Victor Dias Gomes", poX-120, poY-step);
				g.drawString("> Pedro Henrique Maia Duarte", poX-120, poY);
				g.drawString("> Pedro Costa Calazans", poX-120, poY+step);
				g.drawString("> Thales Henrique Bastos Neves", poX-120, poY+(step*2));
				g.setFont(g.getFont().deriveFont(20.0f));
				g.drawString("IMAGENS OBTIDAS EM:", poX-60, poY+(step*3));
				g.setFont(g.getFont().deriveFont(8.0f));
				g.drawString("https://www.reddit.com/r/PixelArt/comments/e1j9yt/i_need_some_space/?utm_medium=android_app&utm_source=share", 20, poY+((step/2)*7));
				g.drawString("https://watlerje.tumblr.com/post/186405487925/space-background-for-ss13", 20, poY+((step/2)*8));
				g.setFont(g.getFont().deriveFont(12.0f));
				g.drawString("[SPACE] ou [ENTER] para voltar", poX-50, poY+(step*5));
			}else if(estado == "Controles") {
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0,150));
				g2.fillRect(0, 200, Game.WIDTH, 350);
				
				if(indexControles == 0){ //teclado
					g.setColor(Color.YELLOW);
					g.setFont(g.getFont().deriveFont(30.0f));
					g.drawString("<TECLADO>", poX-30, poY-(step*2));
					g.setColor(Color.GRAY);
					g.setFont(g.getFont().deriveFont(20.0f));
					g.drawString("<MOUSE>", poX+160, poY-(step*2));
					g.setColor(Color.WHITE);
					g.setFont(g.getFont().deriveFont(20.0f));
					g.drawString("[SETAS]   >   mover", poX-50, poY-step);
					
				}else if(indexControles == 1){ //mouse
					g.setColor(Color.YELLOW);
					g.setFont(g.getFont().deriveFont(30.0f));
					g.drawString("<MOUSE>", poX-20, poY-(step*2));
					g.setColor(Color.GRAY);
					g.setFont(g.getFont().deriveFont(20.0f));
					g.drawString("<TECLADO>", poX-150, poY-(step*2));
					g.setColor(Color.WHITE);
					g.setFont(g.getFont().deriveFont(20.0f));
					g.drawString("[MOUSE]  >   mover", poX-50, poY-step);
				}
				g.setColor(Color.WHITE);
				g.setFont(g.getFont().deriveFont(20.0f));
				g.drawString("[ESPAÇO]  >   atirar", poX-50, poY);
				g.drawString("    [P]    >   pausar", poX-50, poY+step);
				g.drawString("    [R]    >   reiniciar", poX-50, poY+(step*2));
				g.drawString("  [ESC]   >   fechar", poX-50, poY+(step*3));
				g.setFont(g.getFont().deriveFont(12.0f));
				g.drawString("[SPACE] ou [ENTER] para confirmar", poX-50, poY+(step*5));
				
			}
	}

}
