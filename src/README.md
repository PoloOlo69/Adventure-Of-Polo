Polo vs Bolo Multiplayer
========================
Programmiermethoden- und techniken SS22, Paul Dürrwang 02/08/2022.

**DESCRIPTION AND CONCEPT**
****
This game has been written in context of Programiermethoden- und techniken SS22.
Polo vs Bolo Multiplayer is based on the concept of Adventure of Polo, using Java RMI(Remote Method Invocation).
RMI is a Java Library which enables a JVM to invoke methods on Objects located on a remote JVM.
In this case the Client program looks up a remote "Server" Object to redirect the method calls to the targeted client.
I am sure there is a lot of headroom for optimization in my example.


The rules are pretty basic:
> Interfaces have to extend Remote
> 
> RemoteObject´s have to be exported (Unicast.RemoteObject.export())
> 
> RemoteObject´s have to be Serializable

Using Records is highly recommended since we have to aggregate plain data.

    "The data, the whole data, and nothing else"

As a side effect we also benefit from speed advantages over using regular serializable Classes. As well as reducing Boilerplate code


**SETUP**
****
The easiest way is to run a demo is to open the Project in your IDE of choice:

1. Run: [ServerSetup](server/GameServerSetup.java)
2. Run Client 1: [Main](client/Main.java)
3. Run Client 2: [Main](client/Main.java)

Otherwise, you can just "javac" the above-mentioned classes, but you have to figure out the dependencies yourself.
You can add the IP and Port that should be used to connect to the Server as arguments when running the Client Main.

**SUMMARY**
****
Top-notch exercise! I really enjoyed this Project, so much fun! I appreciated the freedom of choice we had!
This would reduce requests, maybe it would speed things up.
I am excited for next Semester and I hope that we will learn more about Threading because I had a lot of questions regarding this exercise.

Questions:

It would have been way easier to just use the Server as a Database on which each client will update the position of its player and just fetch the last updated position of the opponent.
I thought this could be quicker because the Game Thread wouldn't have to wait for the response, which could take a long time because the other client could be busy?


**SOURCES**
****
All Sprites and Sounds are self-made using Photoshop, Pixilart, Beepbox and Garageband, except for the theme song, which is made by Dargolan.

Shutouts:

https://www.dargolan-free.com/

https://www.beepbox.co/

https://www.pixilart.com/