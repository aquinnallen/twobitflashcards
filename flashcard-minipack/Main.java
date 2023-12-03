/*
The purpose of this software is to fulfill the week 1 assignment
of Computer Science Foundations spring 2022. I hope this goofy little flash card
game makes you smile. I smiled when I thought about you completing it!

Thanks to stack exchange and other resoruces for teaching me what java do, I haven't written a class of length before.

All ascii art is found and tailored. Author info was not cited where found. 
elements taken from:
http://www.asciiworld.com/-Robots,24-.html -robot, altered, author not referrenced
https://www.asciiart.eu/buildings-and-places/fences -gate, altered, author not referrenced
https://rb.gy/iruzhp -final animation, author not clearly sited at this location, 
looks machine generated though, could have made it myself using tools that exist..time saver

I consider this fair use as the purpose of this software is education and art.
*/



//imports for program functionality
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.util.Random;
//Main class, the only top level class in this program.
public class Main {
	
	//Main method
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		//instances self
		Main main = new Main();
	  
		//instances nested class QuizzMaster
		QuizzMaster quizzMaster = main.new QuizzMaster();
	 
		// runs Quizz() method of Main using the quizzMaster instance, which takes control and carries out rest of program.	
		main.Quizz(quizzMaster);
		
	}
	
	//Quizz method, controls action of program overall, executes quizz of user's knowledge of binary/decimal conversions and launches finale when complete
	void Quizz(QuizzMaster quizzMaster) throws InterruptedException, IOException{
		//calls quizzMaster method to setup object data called and update during quizz
		quizzMaster.InitiateQuizz();
		Scanner scan = new Scanner(System.in);
		
		//rolls random intArray to use ask for conversions from user
		int[] intArray = quizzMaster.RollRandomIntArr();
		String[] stringArray = quizzMaster.ConvertRandIntBin(intArray);
		quizzMaster.assignQuizzValues(intArray, stringArray);
		
		//while loops keeps it quizzing until it is done
		int count =0;
		boolean quizzing = true;
		boolean toggle = false;
		while (quizzing == true) {
			//prints current score
			quizzMaster.printer.Print("playscreen.txt");
			System.out.println("##########################################\n"+
					"##########################################\n"+
					"##########################################\n"+
					"########### PLAYER: "+quizzMaster.usrName+
					"   ##############\n############ SCORE: "+quizzMaster.usrScore+
					"   #################\n######### SCORE TO WIN: "+quizzMaster.favNum+
					"   ############\n#######################################"+
					"\n#######################################"+
					"\n\n\n");
			
			
			//toggle variable switches if blocks, so that program asks for binary->int and then int->binary
			if(!toggle) {
				int usrVal = 0;
				System.out.println("What is "+quizzMaster.rdBin0[count]+" converted to integer?");
				String response = scan.nextLine();
				
				//evaluates user input, increasing score if correct, decreasing if incorrect
				try {
					usrVal = Integer.parseInt(response);
				}
				catch (Exception InputMismatchException) {
					System.out.println("\n\nThat's not a number!!");
				}
				if (quizzMaster.rdInt0[count] == usrVal) {
					quizzMaster.IncScore();
					System.out.println("\n\nNice! That's correct!");
				}
				else {
					quizzMaster.DecScore();
					System.out.println("\n\nThat's not quite right..");
					System.out.println("\n\nThe actual value is:\n "+quizzMaster.rdInt0[count]);
					Thread.sleep(1500);
				}
				count++;
			}
			//same as previous block but oppsoite
			if(toggle) {
				String usrValB = "";
				System.out.println("What is "+quizzMaster.rdInt0[count]+" converted to binary?");
				String response = scan.nextLine();
				try {
					usrValB = response;
				}
				catch (Exception InputMismatchException) {
					System.out.println("\n\nThat's not a binary number!!");
				}
				//comparing string equality cannot be computed accurately with ==, must use custom method
				if (quizzMaster.rdBin0[count].contentEquals(usrValB)) {
					quizzMaster.IncScore();
					System.out.println("\n\nNice! That's correct!");
				}
				else {
					quizzMaster.DecScore();
					System.out.println("\n\nThat's not quite right..");
					System.out.println("\n\nThe actual value is:\n"+quizzMaster.rdBin0[count]);
					Thread.sleep(1500);
				}
				count++;
			}
			toggle = !toggle;
			if(count == quizzMaster.rdInt0.length-3){
				//RErolls random intArray to use ask for conversions from user
				intArray = quizzMaster.RollRandomIntArr();
				stringArray = quizzMaster.ConvertRandIntBin(intArray);
				quizzMaster.assignQuizzValues(intArray, stringArray);
				//resets count
				count = 0;
			}
	//triggers the Finale sequence by instancing a Finale object and running a method within that object, escapes while loop for quiz
			if(quizzMaster.usrScore >= quizzMaster.favNum) {
				Finale finale = new Finale();
				System.out.println("Congratulations!! You have successfully converted your favorite number of numbers!!\n\n\n\n\n");
				System.out.println("What will you do next?");
				try {
					finale.ExecuteFinale();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				quizzing = false;
			}
		}
		
		//once code gets here all things going on will have resolved, at end of sleep for 1min, system exits if user has not terminated
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	//object prints text files when fed path names
	public static class FilePrinter {
		FilePrinter()  {		
		}
		public void Print(String path) throws FileNotFoundException{
			File file = new File(path);
			Scanner input = new Scanner(file);	
			while (input.hasNextLine()){
			System.out.println(input.nextLine());
			}
			input.close();
		}
	}
	//class QuizzMaster stores and manipulates running data
	public class QuizzMaster {
		String usrName;
		String[] rdBin0;
		int[] rdInt0;
		int favNum;
		int usrScore;
		int usrDifficulty;
		FilePrinter printer = new FilePrinter();
		QuizzMaster(){			
			try {
				printer.Print("entry.txt");
				Thread.sleep(5000);
				printer.Print("entry0.txt");
				Thread.sleep(5000);
				printer.Print("entry1.txt");
				Thread.sleep(5000);
			} catch (FileNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//examples of helper methods, set and change instance variables in object
		public void SetName(String name){
			usrName=name;
		}
		public void SetNum(int num){
			favNum=num;
		}
		public void SetDifficulty(int num) {
			usrDifficulty=num;
		}
		public void IncScore() {
			usrScore++;
		}
		public void DecScore() {
			usrScore--;
		}
		//I was initially going to make this all operate as functions of the QuizzMaster object, but I forgot and later did not have further energy to refactor it to work that way..
		public void QuizzUser() {
			
		}
		
		//rolls a random byte and converts it to int and binary string
		//checks byte value to see that it is smaller than user requested max val, later used in quizz loop to shake values
		/*public void RollRandom() {
			boolean smallEnough = false;
			while (!smallEnough) {
				Random rd = new Random();
				byte[] arr = new byte[1];
				rd.nextBytes(arr);
				byte rdByte = arr[0];
				int rdInt = (rdByte & 0xFF);
				String rdBin = Integer.toBinaryString(rdInt);
				if (rdInt <= usrDifficulty) {
					rdInt0=rdInt;
					rdBin0=rdBin;
					smallEnough = true;
				}
			}
		}
		*/
		
		//felt the above recurring roll was lazy, so I rolled an array twice the size of score to win
		
		public int[] RollRandomIntArr() {
			
			int[] returnArray = {};
			boolean arrayFull = false;
			while (!arrayFull) {
				//make a rand
				Random rd = new Random();
				//make a byte holder
				byte[] arr = new byte[1];
				//bite off a byte
				rd.nextBytes(arr);
				byte rdByte = arr[0];
				// padding byte to return unsigned 0-255 as possible range
				int rdInt = (rdByte & 0xFF);
				if (rdInt <= usrDifficulty) {
					int newarr[] = new int[returnArray.length + 1];
					  
			        // insert the elements from
			        // the old array into the new array
			        // insert all elements till n
			        // then insert x at n+1
			        for (int i = 0; i < returnArray.length; i++)
			            newarr[i] = returnArray[i];
			  
			        newarr[returnArray.length] = rdInt;
					returnArray= newarr;
				}
				if (returnArray.length>favNum*2) {arrayFull=!arrayFull;}
			}
			return returnArray;
		}
		
		//makes an array with same values as BinString
		public String[] ConvertRandIntBin(int[] intArr) {
			String[] returnArray = {};
			int count = 0;
			for(int rdInt : intArr) {
			//pad out the string with zeros and add the space
			//displayes binary byte as two words
				String dummy = Integer.toBinaryString(rdInt);
				while (dummy.length()<8) {
					dummy = "0"+dummy;
				}
				dummy = dummy.substring(0,4)+" "+dummy.substring(4);				
				String newarr[] = new String[returnArray.length + 1];
				  
		        // insert the elements from
		        // the old array into the new array
		        // insert all elements till n
		        // then insert x at n+1
		        for (int i = 0; i < returnArray.length; i++)
		            newarr[i] = returnArray[i];
		  
		        newarr[returnArray.length] = dummy;
				returnArray= newarr;
				returnArray[count] = dummy;
				count++;
			}
			return returnArray;
		}
		
		//assigns values into object instance vars
		public void assignQuizzValues(int[] intArray, String[] stringArray) {
			rdInt0=intArray;
			rdBin0=stringArray;
		}
		
		
		
		//handles user input to set up quizz settings, informs user what is going on, called by Quizz method
		public void InitiateQuizz() throws FileNotFoundException {
			System.out.println("\n\nGreetings Traveller!\n\n\n\n\n");
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printer.Print("twobit.txt");
			System.out.println("\n\nMy Name is Two-Bit.");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Scanner scan = new Scanner(System.in);
			System.out.println("\n    What is YOUR Name?\n\n");
			String uName = scan.nextLine();
			System.out.println("\n\nAh! "+uName+"!! I had a production batch co-member named "+uName+" once.\n Once...\n\n");
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// 
				e.printStackTrace();
			}
			//Asks user favorite number in order to set score to win
			
			int favNumInt = -2;
			boolean intRec = false;
			while (!intRec) {    	
				try {
					printer.Print("twobit.txt");
					System.out.println("\nTell me, "+uName+". What is your favorite counting number? (1-9 are counting numbers) \n\n");
					String response = scan.nextLine();
					favNumInt = Integer.parseInt(response);
					if (favNumInt>10) {
						favNumInt = favNumInt%10;
						System.out.println("That's not a counting number.. But let's just take a modular approach to it!\n\n*initializing modulation of value*\n\n");
					}
					if (favNumInt<0) {
						favNumInt = Math.abs(favNumInt);
						System.out.println("That's a negative number.. But let's just take a positive approach to it!\n\n*initializing absolution of value*\n\n");
					}
					if (favNumInt==0) {
						static Random locRnd = new Random();
						favNumInt = locRnd.nextInt()%10;
						if(favNumInt == 0) {
							favNumInt = 2;
						}
						System.out.println("That's zero! Also my favorite number.. But we need a counting number..\n\n*choosing random number*\n\n");
					}
					String favNumBin = Integer.toBinaryString(favNumInt);
					String dummy = favNumBin;
					while (dummy.length()<8) {
						dummy = "0"+dummy;
					}
					dummy = dummy.substring(0,4)+" "+dummy.substring(4);
					System.out.println(dummy+"!! What a wonderful value!\n"+
					"I wonder if there are any other ways to represent it?\n"+
					"Lets figure it out!");
					}
				
				catch (Exception InputMismatchException) {
					System.out.println("That's not a number!!/n/n Try again!");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(favNumInt > -1 ) {intRec = true;}
			}
		
			//asks user what max number size that want to use, limiting difficulty of binary/integer conversion
			int usrDifficulty = -2;
			boolean diffRec = false;
			while (!diffRec) {    	
				try {
					printer.Print("twobit.txt");
					System.out.println("\nTell me, "+uName+". What is the largest positive number (up to 255) you would like to disucss?\n\n");
					String response = scan.nextLine();
					usrDifficulty = Integer.parseInt(response);
					if (usrDifficulty<1) {
						usrDifficulty = Math.abs(usrDifficulty);
					System.out.println("Not quite in range! But let's just take a positive approach to it!\n\n*initializing absolution of value*\n\n");
					}
					String usrDifficultyBin = Integer.toBinaryString(usrDifficulty);
					String dummy = usrDifficultyBin;
					while (dummy.length()<8) {
						dummy = "0"+dummy;
					}
					dummy = dummy.substring(0,4)+" "+dummy.substring(4);
					System.out.println(dummy+"!! What an ample value!\n"+
					"Let's get started!\n\n\n");
					Thread.sleep(1500);
					System.out.println("I will give you a value either in your counting system(decimal) or my counting system(binary.)\n\n"
					+ "Your job is to give me the value back in the other representation.\n\n");
					Thread.sleep(3000);
					}
				
				catch (Exception InputMismatchException) {
					System.out.println("That's not a number!!/n/n Try again!");
				}
					if(usrDifficulty > -1 ) {diffRec = true;}

			}
			SetNum(favNumInt);
			SetName(uName);
			SetDifficulty(usrDifficulty);
		}
	}
	//Finale class, carries out victory sequence
	public class Finale {
		BufferedInputStream is;
		Finale(){
			// create a data stream(midi) from a URL to prepare data for Finale class execution
			try {
				is = new BufferedInputStream(new URL("https://bitmidi.com/uploads/79829.mid").openStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void ExecuteFinale() throws URISyntaxException, MidiUnavailableException, InvalidMidiDataException, InterruptedException, IOException {
			//creates sequencer device
			Sequencer sequencer = MidiSystem.getSequencer();
			
			// Opens the sequencer device
	        sequencer.open();
	 			        
	        // Sets the sequence on which the sequencer operates, must be a MIDI stream
	        sequencer.setSequence(is);
	     
	        
	        // Starts playback of the MIDI data in the currently loaded sequence
	        sequencer.start();
	        Thread.sleep(5000);

	        // pops a gif in web browser ;)
	        URI uri= new URI("https://i.redd.it/w2n81iqx37p51.gif");
	        URI uri0= new URI("http://gforsythe.ca/wp-content/uploads/2012/12/big-rick.gif");
	        java.awt.Desktop.getDesktop().browse(uri);
	        java.awt.Desktop.getDesktop().browse(uri0);
	        
	        //prints celebration routine
	        FilePrinter printer = new FilePrinter();
	        for(int i=0; i<1000; i++) {
				printer.Print("f1.txt");
				Thread.sleep(200);
				printer.Print("f2.txt");
				Thread.sleep(200);
				printer.Print("f3.txt");
				Thread.sleep(200);
				printer.Print("f4.txt");
				Thread.sleep(200);
				printer.Print("f5.txt");
				Thread.sleep(200);
				printer.Print("f4.txt");
				Thread.sleep(200);
				printer.Print("f3.txt");
				Thread.sleep(200);
				printer.Print("f2.txt");
				Thread.sleep(200);	
				
	        }
	        System.exit(0);
		}
	}
}
