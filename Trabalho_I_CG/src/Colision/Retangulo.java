package Colision;

public class Retangulo {
	
	public int x, y, width, height;
	
	public Retangulo(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean interceptando(Retangulo r) {
		if(r.x + r.width > x && r.x < x+width && r.y < y+height && r.y+r.height > y){
			return true; //AVALIA SE UM RETÂNGULO POSSUI COORDENADAS DENTRO DAS CORDENADAS DO OUTRO
		}
		return false;
	}

}
