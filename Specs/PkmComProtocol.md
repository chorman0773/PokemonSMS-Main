<h1>Info and Copyright Notice</h1>

<h2>Copyright:</h2>
PokemonSMS Public Specification Project, Copyright 2018 Connor Horman
Pokemon, the Pokemon Logo, and all Official Pokemon are Copyright Nintendo and Game Freak. This Project is in no way affiliated with Nintendo or Game Freak, and disclaims all relation with the above parties. This project is intended as a Fan Game, or as Parody of Legitimate Pokemon titles, and no Concreate Game produced using this project should be considered legitimate or affiliated to the above companies, unless they provide official consent to the connection. This project, and all games produced using this specification intend no copyright infringement or Intellectual Property theft of any kind.<br/><br/>


The PkmCom Protocol Specification("This Document"), provided by the PokemonSMS Public Specification Project ("This Project") is Copyright Connor Horman("The Owner"), 2018. 
Using the license specified by the project, you may, with only the restrictions detailed below,
(a)Use this document to produce a complete or partial implementation of PokemonSMS, 
(b)Use this document as reference material to create other related projects or derivative works,
(c)Transmit and/or Distribute Verbatim Copies of this document,
(d)Transmit and/or Distribute Partial copies of this document, which link to this document and include this copyright notice,
(e)Use this document as a guideline to design other specifications for projects, related or not,
(f)Quote parts of this document for works of any kind, that link to this document,
(g)Use part or the whole of this document for any purpose which does not constitute copyright infringement under the intellectual property laws of your jurisdiction.
<br/><br/>
The following restrictions (and only the following restrictions) apply to the above:
You may not, under any circumstances, 
(a)Claim that this document, or any part of it, belongs to you, 
(b)Use this document in any way that would be unlawful in your jurisdiction, or in Canada, 
(c)Use this document in a way that would infringe upon the copyright of any person, organization, or corporation, without prior written consent from that entity or an entity which has been designated by that company as a legal authority on their behalf, including the Owner, or Sentry Game Engine.
<br/><br/>
  This Document, and this project are distributed with the intention that they will be useful and complete. However this document and this project are provided on an AS-IS basis, without any warranties of Any Kind, including the implied warranties of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. As such, any person using this document for any reason does so at their own risk.  By using this document, you explicitly agree to release The Owner, and any person who might have distributed a copy of this document to you from all liability connected with your use of this document
<br/><br/>
<h2>Info:</h2>
This document details the format for PokemonSMS's communication protocol, and SHOULD be implemented in full to provide a working implementation of the game. 
<br/><br/>
<h1>Handshaking Protocol</h1>
The PkmCom Handshaking Protocol is a multistep linear Protocol which describes how PkmCom initializes the Secure Connection to the server. 
In this specification AES refers to AES-256 using PKCS5 Padding and Cipher Block Chaining Mode, and RSA refers to RSA-1028
<br/><br/>
<ol>
 <li>The server has prepared an RSA Keypair. The public key is then sent to to the client.</li>
 <li>The client should create a new RSA Keypair and send it to the server. The Messages from this point are then sign-encrypted by the sender's private key and then encrypted with the receiver's public key. RSA Messages are padded using ISO/IEC 7816-4 Padding, and chained blocks are considered independent.</li>
 <li>At this point, the server should produce a 2048 bit "message" and send it to the client. The client does the same and sends it to the server. Each of the bits in the message are interlaced, with (from msb of combined) msb of the servers message, msb of the clients message, etc until lsb and 2nd lsb of combined with lsb of the server's message, lsb of the client's message.</li>
 <li>The client and the server should produce a SHA-256 hash of the interlaced message. This is then used to produce an AES-256 symmetric key.</li>
 <li>In addition, a separate 2048-bit message should be produced by xoring the 2 message's together. This new message is used to produce a SHA-256 hash, which is reduced to 128 bits by xoring every odd byte with every even byte. This is used as the IV for the AES encryption.</li>
 <li>From this point, communication via RSA is dropped, and the client should destroy their current private key, and start using AES for encryption.</li>
 <li>The client must then send the MAGIC number to the server and the server must send the MAGIC number to the client. Both of these numbers should decrypt to 0x0F0E1C3E. If not the client or the server may choose to close the connection or repeat from step 1 by sending an FF byte (over nonsecure). (Otherwise they both send a 00 byte).</li>
</ol>


