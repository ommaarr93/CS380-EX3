// Omar Rodriguez
// CS 380
// Professor Nima Davarpanah

import java.io.*;
import java.net.*;
import java.util.*;

public final class Ex3Client
{
	private static Socket socket;
	private static InputStream in;
	private static OutputStream out;
	private static DataInputStream dis;
	private static byte[] ar;
	private static byte[] returnData;

	public static void main(String args[]) throws IOException {
		int array = 0;
		try {
			socket = new Socket("codebank.xyz", 38103);
			System.out.println("Connected to server.");
			in = socket.getInputStream();
			out = socket.getOutputStream();
			dis = new DataInputStream(in);
		}
		catch(UnknownHostException e) {

		}

		array = dis.read();
		ar = new byte[array];

		for(int i = 0; i < array; i++) {
			ar[i] = dis.readByte();
		}
		short sum = checksum(ar);
		returnData = new byte[2];

		int temp = sum >> 8;
		temp = temp & 0xFF;
		returnData[0] = (byte)temp;
		temp = (int)sum;
		temp = temp & 0xFF;
		returnData[1] = (byte)temp;

		out.write(returnData);
    System.out.println("Received " + (javax.xml.bind.DatatypeConverter.printHexBinary(ar).length()) / 2 + " bytes.");
		System.out.println("Data received: ");
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(ar));
		System.out.print("Checksum calculated: ");
		System.out.println("0x" + javax.xml.bind.DatatypeConverter.printHexBinary(returnData));

		if (dis.read() == 1) {
			System.out.println("Response good.");
		} else {
			System.out.println("Response fail.");
		}
	}

	public static short checksum(byte[] b) {
		long sum = 0;
		int i = 0;
		int length = b.length;
		while(length > 1) {
			sum += ((b[i] << 8) & 0xFF00 | ((b[i + 1]) & 0xFF));

			if((sum & 0xFFFF0000) > 0) {
					sum = sum & 0xFFFF;
					sum++;
			}
			i += 2;
			length -= 2;
		}
		if(length > 0) {
			sum += (b[i] << 8 & 0xFF00);

			if((sum & 0xFFFF0000) > 0) {
				sum = sum & 0xFFFF;
				sum++;
			}
		}
		return (short)(~sum & 0xFFFF);
	}
}
