import socket               # Import socket module

soc = socket.socket()         # Create a socket object
host = "" # Get local machine name
port = 4400              # Reserve a port for your service.
soc.bind((host, port))       # Bind to the port
soc.listen(5)                 # Now wait for client connection.

try:
	while True:
		conn, addr = soc.accept()     # Establish connection with client.
		msg = conn.recv(1024)
		intmsg = int(msg.decode('ascii'))
		print (intmsg)
	
except KeyboardInterrupt:
	conn.close()	
	soc.close()
