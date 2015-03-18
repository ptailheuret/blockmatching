
public class Test {
	
	public static float fonction(double i, double j) {
		float res =(float) (100000 * Math.exp(-((i * i) + (j * j)) / 10000));
		return res;
	}
	
	public static float fonction2(float i, float j){
		float res = (float) (Math.cos(i/100)+Math.cos(j/100)*(fonction(i,j)));
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Les images en arguments :
		int hauteurMatrice = 640 ;
		int largeurMatrice = 480 ;
		
		float[][] imageCur = new float[hauteurMatrice][largeurMatrice];
		float[][] imageRef = new float[hauteurMatrice][largeurMatrice];
		//Matrice Resultat inutile pour l'instant
		//String[][] imageRes = new String[hauteurMatrice][largeurMatrice];
		
		/*
		 * TEST  
		 */
		// Matrice 1 -> gaussienne centrée en ..
		// Matrice 2 -> gaussienne centrée en ...

				// Centrage de la gaussienne dans l'image 1 :
				int xImage1 = hauteurMatrice / 2;
				int yImage1 = largeurMatrice / 2;

				// Dans l'image 2 :
				int xImage2 = xImage1 + 40;
				int yImage2 = yImage1 + 1;

				// Initialisation des images
				for (int i = 0; i < hauteurMatrice; i++) {
					for (int j = 0; j < largeurMatrice; j++) {
						imageRef[i][j] = fonction(i - xImage1, j - yImage1);
						imageCur[i][j] = fonction(i - xImage2, j - yImage2);

						
					}
				}
		/*
		 * FIN TEST
		 */
		long time = System.currentTimeMillis();
		//La taille du Block en argument :
		int hauteurBlock = 16;
		int largeurBlock = 16;
		
		
	//int xDepart = 320 ;
		//int yDepart = 240 ;
		
		
		
		//Première recherche = Initialisation 
		
		for (int yDepart = 200; yDepart < 400; yDepart++) {
			for (int xDepart = 200; xDepart < 400; xDepart++) {
				// Block de reference dans l'image de départ :
				Block blockRef = new Block(imageRef, hauteurBlock,
						largeurBlock, xDepart, yDepart);
				float min = Float.MAX_VALUE;
				int xMin = -1;
				int yMin = -1;
				int iRef = -1;
				int xMinTemp = 0;
				int yMinTemp = 0;
				
				
				// Pour les boucles for :
				int iDebutBoucle = 1;
				int iFinBoucle = 8;
				
				// Debut du hex Search :
				// Attention doublon en fin de liste !
				int[][] searchList = { { 0, 0 }, { -2, -1 }, { -2, 1 },
						{ 0, 2 }, { 2, 1 }, { 2, -1 }, { 0, -2 }, { -2, -1 } };
				
				int[][] searchList2 = { { -1, 0 }, { 0, 1 }, { 1, 0 },
						{ 0, -1 } };
				
				for (int i = 0; i < searchList.length - 1; i++) {

					int xNew = xDepart + searchList[i][0];
					int yNew = yDepart + searchList[i][1];

					Block blockCur = new Block(imageCur, hauteurBlock,
							largeurBlock, xNew, yNew);
					float mad = blockRef.MAD(blockCur);
					if (mad < min) {
						min = mad;
						xMin = xNew;
						yMin = yNew;
						iRef = i;
					}
				}
				xMinTemp = xMin;
				yMinTemp = yMin;
				// Premiere phase de recherche :
				boolean boucle = true;
				
				while (boucle) {
					if (iRef == 0) {
						// Recherche dans la liste 2
						for (int i = 0; i < searchList2.length; i++) {

							int xNew = xMin + searchList2[i][0];
							int yNew = yMin + searchList2[i][1];

							Block blockCur = new Block(imageCur, hauteurBlock,
									largeurBlock, xNew, yNew);
							float mad = blockRef.MAD(blockCur);
							if (mad < min) {
								min = mad;
								xMinTemp = xNew;
								yMinTemp = yNew;
								iRef = i;
							}
						}
						xMin = xMinTemp;
						yMin = yMinTemp;
						boucle = false;
					} else {
						iDebutBoucle = iRef - 1;
						iFinBoucle = iRef + 2;
						iRef = 0;
						for (int i = iDebutBoucle; i < iFinBoucle; i++) {

							int xNew = xMin + searchList[i][0];
							int yNew = yMin + searchList[i][1];

							Block blockCur = new Block(imageCur, hauteurBlock,
									largeurBlock, xNew, yNew);
							float mad = blockRef.MAD(blockCur);
							if (mad < min) {
								min = mad;
								xMinTemp = xNew;
								yMinTemp = yNew;
								iRef = i;
							}
						}
						xMin = xMinTemp;
						yMin = yMinTemp;
					}
				}
				//System.out.println(xMin-xDepart);
				//System.out.println(yMin-yDepart);
			}
		}
		System.out.println("Temps : (en ms) " + (System.currentTimeMillis() - time));
		

	}

}
