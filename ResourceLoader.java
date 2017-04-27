package snke_the_game;

import java.io.InputStream;


final public class ResourceLoader {

	public static InputStream load(String path){
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if (input == null){
			input = ResourceLoader.class.getResourceAsStream("/"+path);
		}
		return input;
	}
}


