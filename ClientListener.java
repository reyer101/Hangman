/**
 * ClientListener.java
 * Author: Alec Reyerson
 * This class runs on the client end and just
 * displays any text received from the server in 
 * order to play hangman
 */
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class ClientListener implements Runnable
{
	private Socket connectionSock = null;
	private Boolean isFirst = false;
	private Boolean wordChosen = false;

	ClientListener(Socket sock)
	{
		this.connectionSock = sock;
	}

	public void run()
	{
       	// Wait for data from the server.  If received, output it.
       	int messageCount = 0;

       	System.out.println("Welcome to Hangman!");
		System.out.println("Please wait for Player 1 to pick a word");

		try
		{
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			while (true)
			{
				// Get data sent from the server
				String serverText = serverInput.readLine();
				//System.out.println(isFirst);
				if(serverText.equals("100"))
				{
					isFirst = true;	
				}
				else if (serverText.equals("200")) 
				{
					wordChosen = true;					
				}
				//System.out.println(serverText);

				if(messageCount==0)
				{
					if(isFirst)
					{
						
						System.out.println("Enter a word to start the game: ");
						messageCount++;
					}	

				}
				
				if(!serverInput.equals(null))
				{
					if(!serverText.equals("null") && messageCount> 1)
					{						
							System.out.println(serverText);												
					}
				}
				else
				{
					// Connection was lost
					System.out.println("Closing connection for socket " + connectionSock);					
					connectionSock.close();
					break;
				}
				messageCount++;


			}
		}
		catch (Exception e)
		{			
			try
			{
				connectionSock.close();
				System.exit(0);
			}
			catch (Exception ex)
			{
				System.out.println(ex.toString());
			}
			
		}
	}
} // ClientListener for HangClient
