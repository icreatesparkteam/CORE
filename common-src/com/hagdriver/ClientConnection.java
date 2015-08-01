/**************************************************************************************************
  Filename:       Client Connection.java
  Revised:        $$
  Revision:       $$

  Description:    Socket Client Class

  Copyright (C) {2014} Texas Instruments Incorporated - http://www.ti.com/


   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

     Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.

     Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the
     distribution.

     Neither the name of Texas Instruments Incorporated nor the names of
     its contributors may be used to endorse or promote products derived
     from this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
   OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
   OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
**************************************************************************************************/

package com.hagdriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Semaphore;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hagdriver.hagatewayPb.hagatewayPb.GwZigbeeGenericCnf;
import com.hagdriver.hagatewayPb.hagatewayPb.gwCmdId_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwStatus_t;

//import android.os.AsyncTask;
//import android.util.Log;

public class ClientConnection implements Runnable /* extends  AsyncTask<Void, byte[], Boolean>  */{
	Socket nsocket; // Network Socket
	InputStream nis; // Network Input Stream
	OutputStream nos; // Network Output Stream
	protected boolean bSocketConnected;

	private int serverPort = 1234;
	private String serverIpAddr = "192.168.1.1";
	private String logTag = "CleintConnection";
	
	private Semaphore semWaitConf;
	private Semaphore semSendWaitConf; 
	
	Thread clientThread;
	private gwStatus_t lastCnfStatus;
	private int lastCnfSeq;

	public void startClient() {	
		semSendWaitConf = new Semaphore(1);
		semWaitConf = new Semaphore(0);
		clientThread = new Thread(this);	        
		clientThread.start();
	}
	
	public void setLogTag(final String tag)
	{
		logTag = tag;
	}

	public void setServerIpAddr(final String ipAddr)
	{
		serverIpAddr = ipAddr;
	}
	
	protected void setServerPort(final int port)
	{
		serverPort = port;
	}
	
	protected class pkt_buf_hdr_t {
		short len;
		byte subsystem;
		byte cmdId;
	}

	protected class pkt_buf_t {
		pkt_buf_hdr_t header;
		byte[] packed_protobuf_packet;

		public pkt_buf_t(final int payloadLen) {
			header = new pkt_buf_hdr_t();
			packed_protobuf_packet = new byte[payloadLen];
			return;
		}
	}
	
	protected static final short PKT_HEADER_SIZE = 4;
	protected static final short PKT_HEADER_LEN_FIELD = 0;
	protected static final short PKT_HEADER_SUBSYS_FIELD = 2;
	protected static final short PKT_HEADER_CMDID_FIELD = 3;
	protected static final short PKT_HEADER_DATA_FIELD = 4;

	public void run() { // This runs on a different thread
		try {
			//System.out.println( logTag + ": Creating socket");
			final SocketAddress sockaddr = new InetSocketAddress(serverIpAddr,
					serverPort);
			nsocket = new Socket();
			nsocket.connect(sockaddr, 5000); // 10 second connection timeout
			if (nsocket.isConnected()) {
				bSocketConnected = true;
				nis = nsocket.getInputStream();
				nos = nsocket.getOutputStream();
				//System.out.println(
				//		logTag + ": Socket created, streams assigned");
				//System.out.println( logTag + ": Waiting for inital data...");

				final byte[] inPktBuf = new byte[4096];
				int bytesRead = 0;

				Thread.sleep(10);
				
				while ( bytesRead != -1) {					
					// read the header - blocking
					bytesRead = nis.read(inPktBuf, 0, PKT_HEADER_SIZE);
					
					if(bytesRead == -1)
					{
						System.out.println( logTag + ": read -1");
						//return;
					}
					
					//System.out.println( logTag + ": Got some data");
					final int highByte = inPktBuf[PKT_HEADER_LEN_FIELD + 1] & 0xFF;
					final int lowByte = inPktBuf[PKT_HEADER_LEN_FIELD] &0xFF;
					
					final int bytesToRead = (lowByte) + (highByte << 8);
					if(bytesToRead > 0)
					{
						// read the protobuf payload - blocking
						bytesRead = nis
							.read(inPktBuf,
									PKT_HEADER_SIZE, bytesToRead
									);
						if(parseForCnf(inPktBuf, bytesRead) == 0)
						{
							//not a conf send for processing
							parseIncoming(inPktBuf, bytesRead);
						}
					}
				}
			}
		} catch (final IOException e) { //for Socket
			e.printStackTrace();
			System.out.println( logTag + ": IOException - " + e);
			
		} catch (final Exception e) { //for the sleep
			e.printStackTrace();
			System.out.println( logTag + ": Exceptionn - " + e);			
		} finally {
			try {
				System.out.println( logTag + ": Socket exception - closing IO streams and socket");
				nis.close();
				nos.close();				
				nsocket.close();
				bSocketConnected = false;				
				
				//Let sockets close
				Thread.sleep(10);							
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final Exception e) {
				e.printStackTrace();
			}
			System.out.println( logTag + ": Finished");
			
			//attempting to restart thread
			//this.run();
		}
	}

	protected int parseForCnf(final byte[] buffer, final int readLen) {
		final pkt_buf_t pkt = new pkt_buf_t(readLen);

        final int highByte = buffer[PKT_HEADER_LEN_FIELD + 1] & 0xFF;
        final int lowByte = buffer[PKT_HEADER_LEN_FIELD] &0xFF;
		pkt.header.len = (short) ( lowByte + (highByte<< 8));

		if (pkt.header.len != readLen) {
			System.out.println(
					"parseIncoming: pkt.header.len does not match number of bytes read from the socket");
			return 0;
		}

		pkt.header.subsystem = buffer[PKT_HEADER_SUBSYS_FIELD];
		pkt.header.cmdId = buffer[PKT_HEADER_CMDID_FIELD];

		for (int pktIdx = 0; pktIdx < pkt.header.len; pktIdx++) {
			pkt.packed_protobuf_packet[pktIdx] = buffer[PKT_HEADER_DATA_FIELD
					+ pktIdx];
		}

		if (pkt.header.cmdId == gwCmdId_t.ZIGBEE_GENERIC_CNF_VALUE) {
			final GwZigbeeGenericCnf.Builder genericCnf = GwZigbeeGenericCnf
					.newBuilder();
			try {
				genericCnf.mergeFrom(pkt.packed_protobuf_packet);
				lastCnfStatus = genericCnf.getStatus();
				if(lastCnfStatus == gwStatus_t.STATUS_SUCCESS )
				{
					lastCnfSeq = genericCnf.getSequenceNumber();
				}
				semWaitConf.release();
				/*System.out.println(
						logTag + " - parseForCnf: Released semWaitConf seq:" + lastCnfSeq);*/	

			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
			return 1;
		}
		
		return 0;
	}
	
	protected void parseIncoming(final byte[] buffer, final int readLen) {
		//THIS FUNCTION SHOULD BE OVERLOADED
	}	


	public boolean waitForSocketToConnect() {
		// immediately return if socket is already open
		if (bSocketConnected) {
			return true;
		}

		// Wait until socket is open and ready to use
		int count = 0;
		while (!bSocketConnected && count < 3000) {
			//System.out.println(
			//		 logTag + ": waiting for connection");
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			count += 500;			
		}
				
		if(!bSocketConnected && !clientThread.isAlive())
		{
			//System.out.println(
			//		 logTag + ": Attempting to reconnect");

			clientThread = new Thread(this);
			clientThread.start();
		}
		
		return bSocketConnected;
	}

	protected void sendClientMsg(final byte[] pkt) {
		// Wait until socket is open and ready to use
		waitForSocketToConnect();

		if (bSocketConnected) {
			new Thread(new Runnable() {				
				public void run() {
					try {
						try {
							/*System.out.println(
									 logTag + ": Writing received message to socket");*/
							nos.write(pkt);							
						} catch (final IOException e) {
							e.printStackTrace();
						}
					} catch (final Exception e) {
						e.printStackTrace();
						System.out.println(
								logTag + ": Message send failed. Caught an exception");
					}
				}
			}).start();
			return;
		} else {
			System.out.println(
					logTag + ": Cannot send message. Socket is closed");
		}

		return;
	}
	
	protected int sendClientMsgWaitCnf(final byte[] pkt) {
		
		/*System.out.println(
				logTag + " - sendClientMsgWaitCnf: Acquiring semSendWaitConf");*/
		//lock this resource until sent 
		try {
			semSendWaitConf.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/*System.out.println(
				logTag + " - sendClientMsgWaitCnf: Acquired semSendWaitConf");*/
		
		// Wait until socket is open and ready to use
		waitForSocketToConnect();

		if (bSocketConnected) {
			new Thread(new Runnable() {				
				public void run() {
					try {
						try {
							/*System.out.println(
									 logTag + ": Writing received message to socket");*/
							nos.write(pkt);														
						} catch (final IOException e) {
							e.printStackTrace();
						}
					} catch (final Exception e) {
						e.printStackTrace();
						/*System.out.println(
								logTag + ": Message send failed. Caught an exception");*/
					}
				}
			}).start();
			//return 0;
		} else {
			System.out.println(
					logTag + ": Cannot send message. Socket is closed");
		}


		//wait for the cnf 
		/*System.out.println(
				logTag + " - sendClientMsgWaitCnf: Acquiring semWaitConf");*/
		try {
			semWaitConf.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/*System.out.println(
				logTag + " - sendClientMsgWaitCnf: Acquired semWaitConf");*/
	
		semSendWaitConf.release();
		/*System.out.println(
				logTag + " - sendClientMsgWaitCnf: Released semSendWaitConf");*/	
		
		int rtn = -1;
		if(lastCnfStatus == gwStatus_t.STATUS_SUCCESS )
		{
			rtn = lastCnfSeq;
		}

		return rtn;
	}
	
	protected void close() throws IOException
	{
		clientThread.interrupt();
		nis.close();
		nos.close();
		nsocket.close();
	}

}
