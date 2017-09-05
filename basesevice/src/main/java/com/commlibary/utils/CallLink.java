package com.commlibary.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class CallLink {

	final int TALK_PORT = 22222;

	String ipAddr = null;
	Socket outSock = null;
	ServerSocket inServSock = null;
	Socket inSock = null;

	CallLink(String inIP) {
		ipAddr = inIP;
	}

	void open() throws IOException, UnknownHostException {
		if (ipAddr != null)
			outSock = new Socket(ipAddr, TALK_PORT);
	}

	void listen() throws IOException {
		inServSock = new ServerSocket(TALK_PORT);
		inSock = inServSock.accept();
	}

	public InputStream getInputStream() throws IOException {
		if (inSock != null)
			return inSock.getInputStream();
		else
			return null;
	}

	public OutputStream getOutputStream() throws IOException {
		if (outSock != null)
			return outSock.getOutputStream();
		else
			return null;
	}

	void close() throws IOException {// �ر��������
		inSock.close();
		outSock.close();
	}

}
