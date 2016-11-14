/**
 * ClientHandler.java
 * Author: Alec Reyerson
 * This class handles communication between the client
 * and the server.  It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 *
 */
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Character;

public class ClientHandler implements Runnable
{
	private Socket connectionSock = null;
	private ArrayList<Socket> socketList;
	private Boolean isFirst = false;
	public String word;
	DataOutputStream clientOutput = null;
	DataOutputStream secondClientOutput = null;		
	public char[] wordArray = null;          //For comparison purposes it was easier to make the word a char array to access the Arrays.equals() method
	public char[] letters = null;	
	public int messageCount = 0;		
	private HangInterface game;

	ClientHandler(Socket sock, ArrayList<Socket> socketList, HangInterface _game)
	{
		this.connectionSock = sock;
		this.socketList = socketList;	// Keep reference to master list
		this.game = _game;
	}

	public void run()
	{        		// Get data from a client and send it to everyone else		
		try
		{
			if(socketList.size() == 1)
			{
				isFirst = true;        //If the client is the first to connect, it will be the creator of the word or phrase
				Socket socket = socketList.get(0); //Sends a 100 code to the first client to connect 
				clientOutput = new DataOutputStream(socket.getOutputStream());
				clientOutput.writeBytes("100" + "\n");
			}

			if(socketList.size()>2)
			{
				//System.out.println("In if");
				clientOutput = new DataOutputStream(connectionSock.getOutputStream());
				//System.out.println("In if");
				clientOutput.writeBytes("Sorry, but two players have already connected");    //Handles if more than 2 clients try to join
				socketList.remove(connectionSock);
				connectionSock.close();	

			}
			else
			{				
				System.out.println("Connection made with socket " + connectionSock);
				BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
				while (true)
				{

					// Get data sent from a client
					String clientText = clientInput.readLine();
					if (clientText != null)
					{
						if(isFirst) //The first player to connect will be player 1, the player that decides the word or phrase.
						{
							System.out.println("Player 1: " + clientText);
						
							//System.out.println(word);
							if(messageCount==0)
							{
								word = clientText;

								game.setWord(word); //Sets the word 								
								
								Socket socket = socketList.get(1); //Sends a 200 code to the second client to connect when the word is chosen
								clientOutput = new DataOutputStream(socket.getOutputStream());
								clientOutput.writeBytes("200" + "\n");					

								letters = game.getGuess();	

								int i = 0;
								System.out.print("Word: ");
								while(i<word.length())
								{																	
									System.out.print(letters[i]+ " ");	

									i++;
								}
								System.out.println("");	

								i=0;

								clientOutput.writeBytes(Character.toString(letters[0])+"\n");
								while(i<word.length())
								{										
									clientOutput.writeBytes(Character.toString(letters[i])+"\n");									
									i++;
								}
										

								clientOutput.writeBytes("\n" + "Chances: " + game.getChances() + "\n");
								clientOutput.writeBytes("Enter your first letter guess" + "\n");
								messageCount++; //Good up until this point
							}							
													
						}
						else   //This block runs if the client is the second to connect
						{
							Socket socket = socketList.get(0);  //Socket connecting to the first client
							Socket secondSocket = socketList.get(1); //Socket connecting the second client

							clientOutput = new DataOutputStream(socket.getOutputStream());
							secondClientOutput = new DataOutputStream(secondSocket.getOutputStream());

							int guessNum = 1;

							while(game.getChances() > 0 && !game.isGuessed()) //The game will be played inside this while loop. It is broken when either 
							{												  //the word is guessed or the user runs out of chances.	
								clientText = clientInput.readLine();

								if(clientText != null)
								{
									System.out.println("Player 2: " + clientText);
									char guess = clientText.charAt(0);

									letters = game.guessLetter(guess);

									System.out.print("Word: ");

									clientOutput.writeBytes("Guess " + guessNum + "\n");
									guessNum++;

									secondClientOutput.writeBytes(" " + "\n");
									for(char letter : letters)
									{
										clientOutput.writeBytes(Character.toString(letter) + "\n");
										secondClientOutput.writeBytes(Character.toString(letter) + "\n");
										System.out.print(letter + " ");
									}
									System.out.println("");
								}

								secondClientOutput.writeBytes("\n" + "Chances: " + game.getChances() + "\n");
								secondClientOutput.writeBytes("Enter your next letter guess" + "\n");
							}

							if(game.isGuessed())
							{
								System.out.println("Player 2 has guessed the word");
								clientOutput.writeBytes("Player 2 has guessed your word" + "\n");
								secondClientOutput.writeBytes("Congratulations! You guessed the word "
																	 + "'" + game.getWord() + "'!" + "\n");
							}
							else
							{
								System.out.println("Player 2 didn't guess the word");
								clientOutput.writeBytes("Player 2 didn't guess your word" + "\n");
								secondClientOutput.writeBytes("You did not guess the word: " + game.getWord() + "\n");
							}

							connectionSock.close();		
							socketList.remove(connectionSock);											
							

							System.exit(0);
						}
						
					}				
					else
					{
					  // Connection was lost
					  System.out.println("Closing connection for socket " + connectionSock);
					   // Remove from arraylist
					   socketList.remove(connectionSock);
					   connectionSock.close();
					   break;
					}
					
					//*System.out.println(messageCount);
					messageCount += 1;
				}						
			}
		}
		catch (IOException e)
		{
			//System.out.println("Handler Error: " + e.toString());

			// Remove from arraylist
			socketList.remove(connectionSock);
		}
	}
}      // ClientHandler for HangServer.java
