package Entidades;

import java.awt.Graphics;
import Colision.Retangulo;

public class Entity {
		
		public double x;
		public double y;
		protected double width;
		protected double height;
		public int dx;
		public double speed;
		
		public Entity(int x, int y, int width, int height, int dx, double speed) {
			this.x = x;
			this.y = y; 
			this.width = width; 
			this.height = height; 
			this.speed = speed;
			this.dx = dx;
			}
		
		//METODOS PARA TER ACESSO AS CORDENASDAS/TAMANHO DAS ENTIDADES

		public int getX() {return (int) this.x;}
		
		public void setX(int newX) {this.x = newX;}
		
		public int getY() {return (int) this.y;}
		
		public void setY(int newY) {this.y = newY;}
		
		public int getWidth() {return (int) this.width;}
		
		public void setWidth(int newWidth) {this.width = newWidth;}
		
		public int getHeight() {return (int) this.height;}
		
		public void setHeight(int newHeight) {this.height = newHeight;}
		
		public int getDx() {return (int) this.dx;}
		
		public void setDx(int newDx) {this.dx = newDx;}
		
		public double getSpd() {return this.speed;}
		
		public void setSpd(double newSpd) {this.speed = newSpd;}
		
		
		public static boolean isColliding(Entity e1, Entity e2) {
			Retangulo rectE1 = new Retangulo(e1.getX(),e1.getY(),e1.getWidth(),e1.getHeight()); //Cria um retangulo para a primeira entidade 
			Retangulo rectE2 = new Retangulo(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight()); //Cria um retangulo para a segunda entidade
			if(rectE1.interceptando(rectE2)) {	//AVALIA SE UM RETÃNGULO INTERSEPTA O OUTRO
				return true;
			}
			return false;
		}
		
		public void tick() {}
		
		public void render(Graphics g) {}
		
		
		
		
}


	

