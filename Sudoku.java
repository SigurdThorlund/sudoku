import java.io.*;
import java.util.*;

public class Sudoku {
	final static int n = 9; //Sudoku pladens antal raekker og soejler
	final static int SQU = 3; //antal raekker og soejler i et kvadrat
	final private int lengthInput = 3; //laengden af brugerens input, hvor evt. " " er slettet.

	private int[][] spillePlade = new int[n][n]; //initialiserer spillepladen
	private String move; // streng som gemmer brugerens input i den paagaeldende runde
	private Scanner input = new Scanner(System.in); //scanner som tager i mod input fra brugeren

	public static void main(String[] args) {
		try {
			Sudoku sudoku = new Sudoku("Sudoku001.svg");
		} catch (FileNotFoundException e) {
			System.out.print("File not found");
		}
			
	}

	public Sudoku(String filnavn) throws FileNotFoundException { 
		Scanner s = new Scanner(new File (filnavn)); //opretter scanner som laeser filen
		readFile(s); //metode der gemmer filen i et int 2D array
		printer(spillePlade); //metode: printer spilleplade
		if(invalidSudoku()) { //tjekker om filen er inconsistent
			System.out.println("Inconsistent sudoku puzzle.");
		} else {
			while(!completeSudoku(spillePlade)) {//while loop koerer saa laenge sudokuen ikke er faerdig
				System.out.println("Next move, please (row, column, value)");
				if (!input.hasNextLine()) {//hvis brugeren ikke giver naeste traek terminater programmet
					System.exit(0);
				}
				this.move = input.nextLine(); //opdaterer den private streng
				takeInput(invalidSudoku());//metode: opdaterer sudoku plade hvis regler er opfyldt, ellers printer "Illegal input."
			}
			System.out.println("Congratulations!"); 
		}
		s.close();
	}

	public void readFile(Scanner s) {	
		for (int i = 0; i < n; i++) { //ydre loop: koerer igennem raekkerne
			String linje = s.nextLine();
			for (int j = 0; j < n; j++) { // indre loop: koerer igennem de enkelte elementer i den specifikke linje
				char testChar = linje.charAt(j); // tildeler den midlertidige var "testChar" det paagaeldede element
				if (testChar == '.') { // hvis "testChar" er '.' skal den tildele elementet vaerdien 0.
					this.spillePlade[i][j] = 0;
				} else { // ellers skal elementet tildeles den negative vaerdi af elementet. Paa denne maade kan sudokuens oprindelige tal senere genkendes.
					this.spillePlade[i][j] = -Character.getNumericValue(linje.charAt(j));
				}
			}			
		}
	}

	public static void printer(int[][] spillePlade) {
		for (int i = 0; i < n; i++) { // ydre loop: koerer igennem raekkerne
			if ( i % SQU == 0 && i != 0) { // soerger for at der kommer "---"
				System.out.println("----------------------");
			}
			for (int j = 0; j < n; j++) { // indre loop: koerer igennem soejlerne
				if (j % SQU == 0 && j != 0) { // soerger for at der kommer " |"
					System.out.print(" |");
				}
				if(spillePlade[i][j] == 0) {//hvis tallet er 0, printer den et "." i stedet.
					System.out.print(" .");
				} else {
					System.out.print(" "+Math.abs(spillePlade[i][j]));	// printer den numeriske vaerdi.
				}

			}			
			System.out.println();
		}
	}

	public boolean invalidSudoku() { 
		/*Den foerste test i denne metode tjekker om et tal optraeder i en raekke eller soejle to gange.
		 * Testen tjekker paa samme tid raekker og soejler.
		 */
		for (int i = 0; i < n; i++) { // ydre loop: koerer igennen alle raekker/soejler
			for (int j = 0; j < n; j++) { // midter loop: koerer igennem de enkelte tal paa i den paagaeldende raekke/soejle
				for (int k = j+1; k < n; k++) { // indre loop: tjekker om et tal optraeder flere steder i den paagaeldende raekke/soejle
					if (Math.abs(spillePlade[i][j]) == Math.abs(spillePlade[i][k]) && spillePlade[i][j] != 0) { //tjekker raekker
						return true;
					}else if(Math.abs(spillePlade[j][i]) == Math.abs(spillePlade[k][i]) && spillePlade[j][i] != 0) {//tjekker soejler
						return true;
					}
				}
			}

		}
		//Tjekker om det et tal optraeder to gange i et kvadrat.

		//oensker at lave et midlertidigt et-dimensionelt array der indeholder tal i det paagaeldende kvadrat
		int kvadrat[] = new int[SQU*SQU]; //indeholder alle 9 tal i et kvadrat
		int counter = 0; //counter til at placere tallet i det en-dimensionelle array 

		for (int g = 0; g < SQU; g++) { //antallet af kvadrater vertikalt
			for (int h = 0; h < SQU; h++) { //antallet af kvadrater horisontalt - nu har vi indsnaevret os til et kvadrat
				for (int i = 0+(g*SQU); i < SQU+(g*SQU); i++) { //gaar igennem de tre raekker 
					for (int j = 0+(h*SQU); j < SQU+(h*SQU); j++) {//gaar igennem de tre tal i hver raekke
						kvadrat[counter] = spillePlade[i][j]; //tildeler det midlertidige array vaerdien.
						counter++; //efter hvert definerede tal gaar man videre til naeste slot i array
					}
				}
				//nu et int array kvadrat blevet tildelt vaerdier.
				for (int i = 0; i < SQU*SQU; i++) { //tester squaren. - samme metode der bruges til at tjekke en linje i den samlede spilleplade
					for (int j = i+1; j < SQU*SQU; j++) {
						if (Math.abs(kvadrat[i]) == Math.abs(kvadrat[j]) && kvadrat[i] != 0) {
							return true;
						}
					}
				} //faerdig med squaren
				//gaar videre til naeste.
				counter=0;
			}
		}	
		return false;		
	}

	public void takeInput(boolean invalidSuduko) {
		int InputArray[] = new int[lengthInput]; //initialiserer nyt int array til brugerens input 
		int placeHolder = -10; //intialiserer placeholder til gammel vaerdi af tal
		String userInput = move.replace(" ", "");//sletter evt. " "
		boolean ugyldigtInput = false;

		if (userInput.length() != 3 ) { //hvis laengden af input ikke er lig 3, saettes ugyldigtInput til at vaere true.
			ugyldigtInput = true;
		} else {
			for (int i = 0; i < lengthInput; i++) { //omdanner input strengen til et int array med de numeriske vaerdier
				char tal = userInput.charAt(i);
				if (Character.isDigit(tal) && Character.getNumericValue(tal) > 0) {//Er inputtet et tal mellem 1 og 9 saettes det ind i arrayet.
					InputArray[i]=Character.getNumericValue(tal);
				} else if (userInput.charAt(2) == 'x' || userInput.charAt(2) == 'X') {//Hvis inputtet er x saettes denne vaerdi lig 0, hvilket
					InputArray[i]=0;												  //svarer til at slette noget fra spillepladen
				} else {
					ugyldigtInput = true;
				}
			}
		}

		if (!ugyldigtInput) {//Er inputtet ikke ugyldigt koeres raekke/soejleoperationerne.
			placeHolder = spillePlade[InputArray[0]-1][InputArray[1]-1]; //gemmer den gamle vaerdi af tallet
			spillePlade[InputArray[0]-1][InputArray[1]-1] = InputArray[2]; //saetter input ind som vaerdi

			if (placeHolder >= 0 && InputArray[2] == 0) { //hvis det det gamle tal er stoerre er lig med 0, samt at brugerens tredje tal er 'X'
				spillePlade[InputArray[0]-1][InputArray[1]-1] = 0; //tildeles sudokufeltet vaerdien 0
				printer(spillePlade);
			} else if (InputArray[2] < 0 //tester om ny plade indeholder fejl. Hvis ny vaerdi er mindre end 0
					|| InputArray[2] > n //ny vaerdistoerre end n		
					|| invalidSudoku() //nye plade har en fejl i raekker/soejler eller kvadrat
					|| placeHolder != 0) { //gamle tal ikke er 0 - altsaa at man "overwriter" et tal

				spillePlade[InputArray[0]-1][InputArray[1]-1] = placeHolder; //restore den gamle plade
				System.out.println("Illegal input.");
			} else {
				printer(spillePlade);
			}

		} else {
			System.out.println("Illegal input.");
		}
	}


	public boolean completeSudoku(int[][] spillePlade) {
		/*naar pladen er udfyldt korrekt antager vi at der ingen 0'er/"." er tilbage paa pladen. 
		 * det betyder at vi nu blot kan koere pladen igennem og tjekke om der er 0'er tilbage.
		 */
		for (int i = 0; i < n; i++) { // ydre loop: koerer igennen alle linjer
			for (int j = 0; j < n; j++) { // midter loop: koerer igennem de talle i den enkelte linje
				if (spillePlade[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

}

