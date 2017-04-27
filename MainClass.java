package snke_the_game;

import java.awt.Dimension;

import javax.imageio.ImageIO;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
//import javax.swing.JOptionPane;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class MainClass {
	
	public static void main(String[] args) throws IOException{
		
		JFrame frame = new JFrame("SNKE The Game!");
		
		
		frame.setContentPane(new GamePanel());
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
	
		frame.setIconImage(ImageIO.read(ResourceLoader.load("SNKEImage.png")));
		frame.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		

		//BackGround Soundtrack	
		//File Music = new File("SoundTrackLoop.wav");

		
		boolean Musicloop = true;
		do{
			new MainClass().PlayAud("/SoundTrackLoop.wav");
			try {
				Thread.sleep(47500,1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		while(Musicloop == true);
		}

		
	private void PlayAud(String locate) {
		try {
			InputStream music = getClass().getResourceAsStream(locate);
			AudioStream stream = new AudioStream(music);
			AudioPlayer.player.start(stream);

		} catch (Exception e) {
		}	        
	}
}

//InputStream Music = (ResourceLoader.load("./SoundTrackLoop.wav"));
//
//
//static void PlayAud(InputStream music){
//try {
//	Clip clip = AudioSystem.getClip();
//	clip.open(AudioSystem.getAudioInputStream(music));
//	clip.start();
//	Thread.sleep(clip.getMicrosecondLength()/1000);
//	} 
//catch (Exception e){}
//InputStream mysoundtrack = getClass().getResourceAsStream("/SoundTrackLoop.wav");


	


