Readme.ml

ChatProgram

Made by Christian Kraft and Andrew Mainhart

——————————————————————

How to Build: In the directory with the source code, type ‘make’ — thats it.

How to Run: java -jar chat.jar <Your Name> (Hostname) (Port)

Usage: java -jar chat.jar <Your Name> (Hostname to connect to) (Port to connect on)
Port 4000 is used by default, a port will be assigned if 4000 is not available.

Hostname and Port are optional - If not specified, you will act as a server. As a server, you can have chats back-to-back. CRTL+C to stop the server.

If Hostname and Port are specified, you will act as a single-session-client; when the chat ends, the program ends, and you need to run it again to connect to a new partner.

——————————————————————

A note to the grader: Encryption does not require any effort on your part (You have no numbers to type into make). Upon launch, a new set of keys is randomly generated. When you connect to your partner, a handshake occurs (you both exchange your public key and shared key, as well as your name).

Additionally, our brute-force method is in MiniRSA.java and is explained there. An example is given in the main method of that file.