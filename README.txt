Name: Hangman

Description: A multi-threaded, two player hangman game written in Java. This project contains a server, client, client handler, client listener, and a game interface.

Client: Starts a client listener thread for every client to connect and continuously receives user input via command line.

Server: Creates a new instance of a Game Interface and passes it to each Client Handler. Waits for clients to connect and starts a client handler thread for each. 

Client Handler: Limits the number of connections that can be made to the server (2). Takes a Socket, Socket list, and a game interface object as parameters. Handles input from both clients and uses it to control the game. Each Client Handler thread holds a reference to the same game interface object which allows game states and logic to be shared between threads. 

Client Listener: Listens for messages from the Server for each client connected. Displays them to command line.

Game Interface: The game interface is an object in which the game logic is performed. Each Client Handler thread keeps a reference to the same Game Interface so data like the word to be guessed, the number of chances, and the current array
of guesses can be shared between threads. 

**NOTE: The Game Interface class could have possibly been made into a singleton.
 