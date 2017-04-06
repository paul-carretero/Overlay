package routage;

/**
 * Classe de démonstration permettant de vérifier la validité du routage avec une matrice exemple.
 */
public class Main {
	/**
	 * @param args unused
	 */
	public static void main(String[] args) {
		int[][] matrix = new int[][] {
			new int[] {1,1,0,0,1,0},
			new int[] {1,1,1,0,1,0},
			new int[] {0,1,1,1,0,0},
			new int[] {0,0,1,1,1,1},
			new int[] {1,1,0,1,1,0},
			new int[] {0,0,0,1,0,1}
		};
		Routeur r = new Routeur(0, matrix);
		
		System.out.println(" -> 0 = " + r.getChannelName(0));
		System.out.println(" -> 1 = " + r.getChannelName(1));
		System.out.println(" -> 2 = " + r.getChannelName(2));
		System.out.println(" -> 3 = " + r.getChannelName(3));
		System.out.println(" -> 4 = " + r.getChannelName(4));
		System.out.println(" -> 5 = " + r.getChannelName(5));
	}

}
