
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.html.ImageView;

import estim.mouvement.MainProgramme;
import estim.mouvement.Point;



/**
 * Affiche une fenetre
 * @author masseran
 *
 */
public class Window extends JFrame implements ActionListener{
	
	private ImageView imageView;
	private JPanel control;
	private JButton suivant, action;
	public ReadYUV ryuv ;
	private float[][] referenceImage ;
	private float[][] currentImage ;
	//private BlockMatching blockParameter;
	
	public Window(){
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Motion detection");
		
		control = new JPanel();
		control.setLayout(new GridLayout(1, 3));
		
		action = new JButton("Block Matching !");
		suivant = new JButton("Image suivante");
		
		action.addActionListener(this);
		suivant.addActionListener(this);
		
		control.add(action);
		control.add(suivant);
		imageView = new ImageView();
		this.setLayout(new BorderLayout());
		this.getContentPane().add(imageView, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
		this.setVisible(true);
		
	//	blockParameter = new BlockMatching(32, 32, 8);
	}
	
	public ImageView getImageView(){
		return this.imageView ;
	}
	
	/** Lit une video YUV et extrait les images
	 * 
	 * @param fichier
	 */
	public void readYUVVideo(String fichier){
		ryuv = new ReadYUV(352, 288); //read the qcif yuv
		ryuv.startReading(fichier);
		
		imageView.setImage(ryuv.nextImageYOnly(), imageView.REFERENCE);
		//ryuv.setLuminance();
		//referenceImage = ryuv.matriceLuminance ;
		ryuv.nextImageYOnly();
		imageView.setImage(ryuv.nextImageYOnly(), imageView.CURRENT);
		
	
		
		//currentImage = ryuv.matriceLuminance ;
		//if(referenceImage.equals(currentImage)) System.out.println("Probleme");
		// Calcule la différence entre les 2 images
//		try {
//			imageView.setImage(ImageTransform.imageDifference(imageView.getImage(imageView.REFERENCE), imageView.getImage(imageView.CURRENT)), imageView.DIFFERENCE);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		ryuv.endReading();
		affiche();
	}
	
	private void affiche(){
		imageView.repaint();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == action){
			
			BufferedImage imageref = imageView.getImage(imageView.REFERENCE);
			BufferedImage imagecur = imageView.getImage(imageView.CURRENT);
			
			float[][] luminanceRef = new float[imageref.getHeight()][imageref.getHeight()] ;
			float[][] luminanceCur = new float[imageref.getHeight()][imageref.getHeight()] ;
			
			
			
			
			
			for(int i = 0; i<ryuv.height ;i++){
				for(int j = 0 ; j< ryuv.width ; j++){
					luminanceRef[i][j] = rgbToLum(imageref.getRGB(i, j));
					luminanceCur[i][j] = rgbToLum(imagecur.getRGB(i, j));
				}
			}
			
			Point[][] resultat = MainProgramme.BlockMatching(ryuv.height, ryuv.width, 16, 16, 50, 50, luminanceRef, luminanceCur );
			
			
		}
		if(e.getSource() == suivant){
			imageView.next();
		}
		affiche();
	}
	
	public float rgbToLum(int rgb){
		int b = rgb%256 ;
		rgb = rgb-b ;
		int g = rgb%256;
		rgb = rgb - g;
		int r = rgb%256 ; 
		return (float)(0.299 *r + 0.587 *g + 0.114 *b);
	}
	


}
