
public class Block {
	private float[][] matrice;
	
	//coin en haut a gauche
	private int x;
	private int y;
	
	private int hauteur;
	private int largeur;
	
	public Block(float[][] matrice, int hauteur, int largeur, int x, int y) {
		this.x = x;
		this.y = y;
		this.matrice = matrice;
		this.hauteur = hauteur;
		this.largeur = largeur;
	}
	
	
	public float getValue(int i, int j)throws IndexOutOfBoundsException{

		return matrice[x+i][y+j];
	}
	
	public float MAD(Block block){
		
		if(block.hauteur != this.hauteur || block.largeur != this.largeur){
			return -1;
		}
		float res = 0;
		for (int i = 0; i<this.hauteur ; i++){
			for(int j = 0; j<this.largeur; j++){
				res += Math.abs(block.getValue(i, j) - this.getValue(i, j));
			}
		}
		return (res/(hauteur*largeur)) ;
	}
	
	//On ne prend qu'un pixel sur quatre ici : 
	public float SousEchantillonMAD(Block anOtherBlock) throws IndexOutOfBoundsException{
		if(anOtherBlock.hauteur != this.hauteur || anOtherBlock.largeur != this.largeur){
			return -1;
		}
		float res = 0;
		for (int i = 0; i<this.hauteur ; i += 2){
			for(int j = 0; j<this.largeur; j+= 2){
				res += Math.abs(anOtherBlock.getValue(i, j) - this.getValue(i, j));
			}
		}
		return (res/(hauteur*largeur)) ;
	}
	
	@SuppressWarnings("non utilisé")
	public float MSE(Block block){
		if(block.hauteur != this.hauteur || block.largeur != this.largeur){
			return -1;
		}
		float res = 0;
		for (int i = 0; i<this.hauteur ; i++){
			for(int j = 0; j<this.largeur; j++){
				res += Math.pow(block.getValue(i, j) - this.getValue(i, j),2);
			}
		}
		return (res/(hauteur*largeur)) ;
	}
	
	

}
