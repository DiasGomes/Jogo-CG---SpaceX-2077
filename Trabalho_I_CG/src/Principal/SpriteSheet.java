package Principal;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteSheet {
	
private BufferedImage spritesheet;
	
	//valida e recebe a imagem:
	public SpriteSheet(String path) {
		try {spritesheet = ImageIO.read(getClass().getResource(path));}
		catch (IOException e) {e.printStackTrace();}
	}
	
	//retorna o sprite desejado da imagem:
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}

}
