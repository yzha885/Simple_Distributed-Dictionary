# Simple-Distributed-Dictionary
A implementation of threads and socket programming. A multi-threaded server that handle concurrent client requests with simple client and server GUI

# Functionality 
Concurrent clients requests 
 - Search the meaning(s) of a word
   Input: Word to search
   Output: Meaning(s) of the word
   Error: The client should clearly indicate if the word was not found or if an error occurred. In case of an error, a suitable description of the error should be           given to the user.

 - Add a new word
   Input: Word to add, meaning(s)
   Output: Status of the operation (e.g., success, duplicate)
   Error: The user should be informed if any errors occurred while performing the operation.

 - Remove an existing word 
   Input: Word to remove
   Output: Status of the operation (e.g., success, not found)
   Error: The user should be informed if any errors occurred while performing the operation.


# Architecture 
 - The system will follow a client-server architecture in which multiple clients can connect to a milti-threaded server and perform operations concurrently.
 - The multi-threaded server implements a thread per connection architecture.
 
# Interaction 
 - All communication will take place via sockets, These sockets are TCP to ensure high reliablilty.
 
# Run configuration
 Command to Start the Server
  > java –jar MultithreadedServer.jar port dictionary-file
 
 Command to Start the Client 
  > java –jar Client.jar server-address server-port
 
# Model/DataSet 
  All data has been stored in a single file stored into desired place. Dict.txt is a sample Example
 
 
 
