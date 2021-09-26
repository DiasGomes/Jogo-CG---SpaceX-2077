package Principal;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import Entidades.Entity;
import Entidades.Kamikazi;
import Entidades.Oponente1;
import Entidades.Oponente2;
import Entidades.Player;
import HeadUpDisplay.Hud;
import HeadUpDisplay.Loja;
import HeadUpDisplay.Menu;

public class Game extends Canvas implements Runnable, KeyListener, MouseMotionListener{
	
	/**
	 * João Victor Dias Gomes
	 * Pedro Henrique Maia Duarte
	 * Pedro Costa Calazans
	 * Thales Henrique Bastos Neves
	 */
	
	//DECLARAÇAO
	private static final long serialVersionUID = 1L;
	public final static int WIDTH = 16*30;
	public final static int HEIGHT = 16*40; 
	public static Thread thread;
	public static boolean isRunning;
	public static int playerSize = 64;
	public static ArrayList<Entity> entidades;
	public static ArrayList<Entity> inimigos;
	public static SpriteSheet sheet;
	public static Random rand;
	public static Hud hud;
	public static Loja loja;
	public static String estado = "Menu", controle ="Teclado";
	public JFrame frame;
	public Image image; 
	public int contFrame = 0;
	public static int sizeIni = 48;
	public static Player player;
	public static double speedIni = 0.5;
	public static boolean aumentaVelocidade = true; 
	public BufferedImage fundo1, fundo2, fundoPlaneta;
	public static double fundoY = 0;
	public static double fundo2Y = -HEIGHT;
	public double fundoSpeed = 0.05;
	public static double fundoYP, fundoXP;
	public double planetaSpeed = 0.2;
	public Menu menu;
	public static int numFase = 1;
	public static int delay = 0, delayMax = 60*2;
	public int  escolhido;
	public static int contRasante = 0;
	public int contRasanteMax = 60*1;
	public static int mx, my;
	public static boolean click1 = false;
	public int cont = 0;
	
	public Game() {
		this.addKeyListener(this); //ADICIONANDO O TECLADO
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT)); //CONFIGURANDO O TAMANHO DA TELA
		initFrame();
		sheet = new SpriteSheet("/Imagens/Sprites.png");
		entidades = new ArrayList<Entity>(); //LISTA QUE CONTÉM TODAS AS ENTIDADES DO JOGO
		inimigos = new ArrayList<Entity>();	//LISTA QUE CONTÉM TODAS OS INIMIGOS DO JOGO
		image = new BufferedImage((int) (WIDTH),(int) (HEIGHT), BufferedImage.TYPE_INT_RGB);
		rand = new Random();
		hud = new Hud();
		menu = new Menu();
		loja = new Loja();
		adicionaFundo();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		new Thread(game).start(); //INICIANDO JOGO
	}
	
	@Override
	public void run() {
		
		requestFocus(); //FOCA NA TELA DO JOGO AO ENTRAR, NÃO NECESSITANDO CLICAR NELA
		//CRIANDO LOOP
		while(true) {
			tick(); //LOGICA DO JOGO
			render(); //RENDERIZAÇÃO DO JOGO
			try {
				Thread.sleep((int) (1000/60)); //FAZENDO O SISTEMA "DORMIR" A CADA 1/60 DE SEGUNDO
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void tick() {	//LÓGICA DO JOGO
		if(estado == "Menu") {
			menu.tick();
		}else if(estado == "Fase") {
			delayFase();
		}else if(estado == "Loja") {
			loja.tick();
		}else if(estado == "Jogo") {
			
			//FAZENDO A LOGICA DE TODAS AS ENTIDADES DO JOGO
			for(int i = 0;i < entidades.size();i++) {
				entidades.get(i).tick();
			}
			
			if(player.destruido) { //validando se o player n foi destruido e colocando um delay para o gameover
				cont++;
				if(cont > 240) {
					cont = 0;
					estado = "GameOver";
				}
			}else {
				//FAZENDO O FUNDO ANDAR:
				andaFundo();
				//ESCOLHENDO INIMIGO PARA DAR RASANTE
				escolheIniRasante();
				//Lógica do Head Up Display
				hud.tick(); 
			}
		}
		
	}
	
	public void render() {	//RENDERIZAÇÃO
		//CONFIGURANDO RENDERIZAÇÃO
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);return;
		}
		Graphics g = image.getGraphics();
		g.dispose();
		g = bs.getDrawGraphics();
		//Desenha meu jogo
		if(estado == "Menu") {
			menu.render(g);
		}else if(estado == "Loja") {
			loja.render(g);
		}else {
			desenhaJogo(g);
		}
		
		//------------
		bs.show();
	}
	
	public void initFrame() {
		//CONFIGURANDO MINHA TELINHA
		frame = new JFrame("SpaceX 2077");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null); //Posição meio da tela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		//icone do jogo:
		Image icone = null;
			try{
				icone = ImageIO.read(getClass().getResource("/Imagens/icone.png"));
			}catch (IOException e) {
				e.printStackTrace();
			}
		frame.setIconImage(icone);
	}
	
	public void adicionaFundo() {
		//ADICIONANDO AS IMAGENS DE FUNDO
		try {
			fundo1 = ImageIO.read(getClass().getResource("/Imagens/fundo.jpg"));	
			fundo2 = ImageIO.read(getClass().getResource("/Imagens/fundo.jpg"));
			fundoPlaneta = ImageIO.read(getClass().getResource("/Imagens/Planeta.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void andaFundo() {
		//Lógica da imagem do fundo andando
				if(fundoY >= HEIGHT) {
					fundoY = -HEIGHT; //O y da minha imagem volta para cima da tela
				}
				
				if(fundo2Y >= HEIGHT) {
					fundo2Y = -HEIGHT;	//O y da minha imagem volta para cima da tela
				}
				
				if(fundoYP >= HEIGHT) {	//O y da minha imagem volta para cima da tela
					fundoYP = -(HEIGHT/2);
					fundoXP = rand.nextInt(Game.WIDTH-47);
				}
				//INCREMENTANDO OS Ys DAS MINHA IMAGENS
				fundoY+=fundoSpeed;
				fundo2Y+=fundoSpeed;
				fundoYP+=planetaSpeed;
				
				//QUANDO UMA IMAGEM CHEGA NO FIM ELA VOLTA PARA BAIXO ENQUANTO A OUTRA SOBE
	}
	
	public static void spawnEnemy(int fileira, int coluna){
		//cria as fileiras de inimigos:
		int poX = 16;
		int poY = 16;
		int gapX = 16+sizeIni;
		int gapY = 16+sizeIni;
		int prob = 20*(numFase-1);
		if(prob > 99) {
			prob = 99;
		}
		for(int y = 0; y < fileira;y++) {
			for(int x = 0; x < coluna;x++) {
				if(rand.nextInt(100) < prob) { //se vai gerar um oponente1 ou oponente2
					inimigos.add(new Oponente2(poX+gapX*x,poY+gapY*y,sizeIni,sizeIni,1,speedIni));
				}else {
					inimigos.add(new Oponente1(poX+gapX*x,poY+gapY*y,sizeIni,sizeIni,1,speedIni));
				}
			}
		}
		entidades.addAll(inimigos); //ADICIONO MEUS INIMIGOS NAS ENTIDADES
	}
	
	public void escolheIniRasante() {
			contRasante++;
			if(contRasante >= contRasanteMax && inimigos.size() > 2) {
				contRasante = 0;
				//coloquei um timer para chamar um rasante
				//além de um tamnho minimo para que ele ocorra
				if(rand.nextInt(10) < 2) {	//uma chance de ocorrer ou não
					escolhido = rand.nextInt(Game.inimigos.size());	// sorteia um numero que será um index da lista de inimigos
					Entity e = inimigos.get(escolhido);	//seleciono o sorteado
					Kamikazi k = new Kamikazi(e.getX(),e.getY(),e.getWidth(),e.getHeight(),1,10); //gero uma entidade que vá em direção ao player
					inimigos.remove(e); //destruo a entidade atual
					entidades.remove(e);
					entidades.add(k); //adiciono a nova
				}
			}
	}

	public void desenhaJogo(Graphics g) {
			//DEFININDO BACKGROUND
			g.setColor(new Color(0,0,0));
			g.fillRect(0, 0, WIDTH, HEIGHT); //DEFAULT
			g.drawImage(fundo1,0,(int) fundoY, WIDTH,HEIGHT,null); //IMAGEM 1 DO FUNDO
			g.drawImage(fundo2,0, (int) fundo2Y,WIDTH,HEIGHT,null);	//IMAGEM 2 DO FUNDO
			g.drawImage(fundoPlaneta,(int) fundoXP,(int) fundoYP, 47,47,null);	//IMAGEM DO PLANETA DE FUNDO
			//RENDERIZANDO ENTIDADES:
			for(int i = 0;i < entidades.size();i++) {
				entidades.get(i).render(g);
			}
			//Lógica do Head Up Display
			hud.render(g); 
			//OBJETO QUE VAI ME PERMITIR DESENHAR IMAGENS COM TRANSPARENCIA
			Graphics2D g2 = (Graphics2D) g;
			//DESENHANDO CADA RESPECTIVO ESTADO DO JOGO - PAUSE - GAME OVER - TRANSIÇÃO DE FASE
			if(estado == "Pause") {	//SISTEMA DE PAUSE
				g2.setColor(new Color(0,0,0,100));
				g2.fillRect(0, 0, WIDTH, HEIGHT);
				g.setColor(Color.YELLOW);
				g.setFont(new Font("comic sans ms",Font.BOLD,32));
				g.drawString("Pause", (WIDTH/2)-43, HEIGHT/2);
			}else if(estado == "GameOver") {	//SISTEMA DE GAME OVER
				g2.setColor(new Color(0,0,0,100));
				g2.fillRect(0, 0, WIDTH, HEIGHT);
				g.setColor(Color.red);
				g.setFont(new Font("comic sans ms",Font.BOLD,48));
				g.drawString("GAME OVER", (WIDTH/2)-140, HEIGHT/2);
				g.setColor(Color.YELLOW);
				g.setFont(g.getFont().deriveFont(24.0f));
				g.drawString("SCORE:" + player.pontos,  (WIDTH/2)-75, (HEIGHT/2)+40);
				g.setColor(Color.white);
				g.setFont(g.getFont().deriveFont(12.0f));
				g.drawString("[R] para tentar de novo",  (WIDTH/2)-75, (HEIGHT/2)+60);
			}else if(estado == "Fase") {
				g2.setColor(new Color(0,0,0,100));
				g2.fillRect(0, 0, WIDTH, HEIGHT);
				g.setColor(Color.YELLOW);
				g.setFont(new Font("comic sans ms",Font.BOLD,48));
                g.drawString("Fase " + numFase, (WIDTH/2)-80, HEIGHT/2);
            }
		
	}

	public static void delayFase() {
		//GERA UM DELAY DE TEMPO ENTRE UMA FAZE E OUTRA
		delay++;
		if(delay >= delayMax) {
			delay = 0;
			nextLevel();
		}
	}
	
	public static void nextLevel() {
		//SALVANDO INFORMAÇÕES DA FASE ANTERIOR
		int tempPts = player.pontos;
		int tempVida = player.vida;
		int tempDinheiro = player.dinheiro;
		int tempCoolDown = Game.player.cooldown;
		double tempSpd = player.getSpd();
		//DESTRUO MINHAS ENTIDADES ANTIGAS
		inimigos.removeAll(inimigos);
		entidades.removeAll(entidades);
		//ADICIONANDO O PLAYER NA LISTA DE ENTIDADES
		player = new Player((WIDTH/2)-(playerSize/2),HEIGHT-(2*playerSize),playerSize,playerSize,0,tempSpd);
		entidades.add(player); 
		//CARREGANDO INFORMAÇÕES DE FASES ANTERIORES
		player.cooldown = tempCoolDown;
		player.pontos = tempPts;
		player.vida = tempVida;
		player.dinheiro = tempDinheiro;
		//AUMENTO A VELOCIDADE DOS INIMIGOS A CADA FASE PARA AUMAENTAR A DIFICULDADE
		if(aumentaVelocidade) {
			speedIni *= 1.3;
		}
		aumentaVelocidade = true;
		//CRIO NOVOS INIMIGOS E DIFICULTANDO 
		if(numFase < 5) {
			spawnEnemy(3,5);
		}else {
			spawnEnemy(4,5);
		}
		//RESETO VARIAVEL DE RASANTE
		contRasante = 0;
		//RESETO O FUNDO
		fundoY = 0;
		fundo2Y =-HEIGHT;
		fundoYP = 47;
		fundoXP = rand.nextInt(Game.WIDTH-47);
		//COLOCO O JOGO PARA VOLTAR A FUNCIONAR
		estado = "Jogo";
	}
	
	public static void resetar() {
		//RESETA FASE
		numFase = 1;
		//DESTRUO MINHAS ENTIDADES ANTIGAS
		inimigos.removeAll(inimigos);
		entidades.removeAll(entidades);
		//ADICIONANDO O PLAYER NA LISTA DE ENTIDADES
		player = new Player((WIDTH/2)-(playerSize/2),HEIGHT-(2*playerSize),playerSize,playerSize,0,4);
		entidades.add(player); 
		//CRIO NOVOS INIMIGOS
		speedIni = 0.5;
		spawnEnemy(3,4);
		//RESETA VARIAVEL DE RASANTE
		contRasante = 0;
		//RESETA O FUNDO
		fundoY = 0;
		fundo2Y =-HEIGHT;
		fundoYP = 47;
		fundoXP = rand.nextInt(Game.WIDTH-47);
		//COLOCA O JOGO PARA VOLTAR A FUNCIONAR
		estado = "Jogo";
	}

	@Override
	public void keyPressed(KeyEvent e) {	//VERIFICANDO SE UMA TECLA ESTÁ SENDO PRESSIONADA
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { //AVALIANDO TECLA ESC
			System.exit(1); //FECHA MEU PROGRAMA
		}
		
		if(estado == "Menu") {
			if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
				menu.enter = true;	//CONFIRMA A OPÇÃO DO MENU
			}else if(e.getKeyCode() == KeyEvent.VK_UP) {
				menu.up = true;	//SOBE NAS OPÇÕES DO MENU
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				menu.down = true; //DESCE NAS OPÇÕES DO MENU
			}if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				menu.right = true;	//INDICA QUE MEU PLAYER QUER IR PARA DIREITA
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				menu.left = true;	//INDICA QUE MEU PLAYER QUER IR PARA ESQUERDA
			}
		}else if(estado == "Loja") {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				loja.up = true;	//SOBE NAS OPÇÕES DO MENU
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				loja.down = true; //DESCE NAS OPÇÕES DO MENU
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				loja.enter = true;
			}
		}else{
			if(e.getKeyCode() == KeyEvent.VK_RIGHT  && controle == "Teclado") {
				player.right = true;	//INDICA QUE MEU PLAYER QUER IR PARA DIREITA
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT  && controle == "Teclado") {
				player.left = true;	//INDICA QUE MEU PLAYER QUER IR PARA ESQUERDA
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE ) {
				player.shoot = true; //INDICA QUE MEU PLAYER QUER ATIRAR
			}else if(e.getKeyCode() == KeyEvent.VK_P) {
				//INDICA QUE MEU PLAYER QUER ATIRAR
				if(estado == "Jogo") {
					estado = "Pause";
				}else if(estado == "Pause") {
					estado = "Jogo";
				} 
			}else if(e.getKeyCode() == KeyEvent.VK_R) {//RESETA MEU JOGO
				resetar();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {	//VERIFICANDO SE UMA TECLA NÃO ESTÁ SENDO PRESSIONADA
		if(estado == "Jogo") {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT  && controle == "Teclado") {
				player.right = false;
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT  && controle == "Teclado") {
				player.left = false;
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.shoot = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(controle == "Mouse"){
        	if(player.getX()+player.getWidth() < e.getX()){
        		player.right = true;
        		player.left = false;
        	}else if(player.getX() > e.getX()){
        		player.left = true;
        		player.right = false;
        	}else {
        		player.left = false;
        		player.right = false;
        	}
       }
		
	}

	
}
