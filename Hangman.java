import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.Character;

public class Hangman
{
	public static void main(String[] args)
	{
		Boolean guessed = false;   //Boolean varibale used to break out of the while loop if the answer is guessed before the user runs out of chances
		int chances = 0;   //Starts chances at 0

		Scanner reader = new Scanner(System.in);
		System.out.println("Welcome to Hangman!");
		System.out.print("Enter a word: ");
		String word = reader.nextLine();


		char[] wordArray = word.toCharArray();           //For comparison purposes it was easier to make the word a char array to access the Arrays.equals() method
		char[] letters = new char[word.length()];		 //makes a letters array of the same length as the word

		int i = 0;

		if(word.length()<7)
		{
			chances = 6;
		}
		else                                            //Making the number of chances fixed seemed like a good idea until I realized a word could possibly have more
		{												//letters than the user has chances. These lines fix that.
			chances = word.length();
		}
		

		while(i<word.length())
		{
			letters[i] = '_';
			System.out.print(letters[i] + " ");         //Starts the letters[] array off as an array full of "_" to represent unguessed letters
			i++;
		}

		i = 0;
		while(i<chances && !guessed)                    //This loop is the heart of the Hangman logic. It will exit when either the word is guessed or the user runs out
		{												//of chances.
			System.out.print("\n"+ "Enter a letter to guess: ");
			String input = reader.nextLine();
			char guess = input.charAt(0);               //Transforms the string input into a char. NO ERROR CHECKING IMPLEMENTED 

			int j = 0;
			while(j<word.length())
			{
				if(Character.toLowerCase(wordArray[j])==guess)   //Compares each character in the wordArray to the guessed character. If they are equal, the space
				{												 //at the same index in the letters array is set to the matching character.
					letters[j] = wordArray[j]; 
				}
				j++;
			}
			int k=0;
			while(k<word.length())
			{				
				System.out.print(letters[k] + " ");            //Prints the updated letters array after the guess was made
				k++;
			}
			i++;

			if(Arrays.equals(letters, wordArray))              //If the letters array is equal to the wordArray then the word has been guessed and "guessed" is set to true
			{
				guessed = true;
			}

		}
		if(guessed)
		{
			System.out.println("\n"+"Congratulations, you guessed the word!");  //If when the loop exitss guessed is true then the word was guessed and a celebratory message is printed
		}
		else
		{
			System.out.println("\n"+"Unfortunately you did not guess the word"); //Otherwise an alternate message is printed along with the unguessed word
			System.out.print("The word was '");
			i = 0;
			while(i<word.length())
			{
				System.out.print(wordArray[i]);       //Prints the word out if it was not successfully guessed.
				i++;
			}
			System.out.print("'");
		}
	}

}
