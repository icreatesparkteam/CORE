/**************************************************************************************************
  Filename:       NwkMngrClient.java
  Revised:        $$
  Revision:       $$

  Description:    Network Manager Client Class

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

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hagdriver.nwkmngrPb.nwkmgrPb.NwkGetDeviceListCnf;
import com.hagdriver.nwkmngrPb.nwkmgrPb.NwkGetDeviceListReq;
import com.hagdriver.nwkmngrPb.nwkmgrPb.NwkSetPermitJoinReq;
import com.hagdriver.nwkmngrPb.nwkmgrPb.NwkZigbeeDeviceInd;
import com.hagdriver.nwkmngrPb.nwkmgrPb.nwkDeviceInfo_t;
import com.hagdriver.nwkmngrPb.nwkmgrPb.nwkMgrCmdId_t;
import com.hagdriver.nwkmngrPb.nwkmgrPb.nwkPermitJoinType_t;
import com.hagdriver.nwkmngrPb.nwkmgrPb.zStackNwkMgrSysId_t;


public class NwkMngrClient extends ClientConnection {

	private final String logTag = "nwkMngrClient";
	
	final private int NWKMNGR_SERVER_PORT = 2540;

	public interface NwkMngrCallbacks {
		void nwkMngrUpdateDeviceList(final List<nwkDeviceInfo_t> list);
		void nwkMngrUpdateDevice(nwkDeviceInfo_t device);
	}	
	
	public NwkMngrCallbacks nwkMngrCallbacks;
	
	public NwkMngrClient(final String srvrIp) {
		setServerPort(NWKMNGR_SERVER_PORT);
		setServerIpAddr(srvrIp);
		setLogTag(logTag);
		startClient();
	}

	public boolean isConnected(){
		return bSocketConnected;
	}
	
	@Override
	protected void parseIncoming(final byte[] buffer, final int readLen) {
		final pkt_buf_t pkt = new pkt_buf_t(readLen);

        final int highByte = buffer[PKT_HEADER_LEN_FIELD + 1] & 0xFF;
        final int lowByte = buffer[PKT_HEADER_LEN_FIELD] &0xFF;
        
		pkt.header.len = (short) (lowByte + (highByte << 8));

		if (pkt.header.len != readLen) {
			System.out.println(
					"parseIncoming: pkt.header.len does not match number of bytes read from the socket");
			return;
		}

		pkt.header.subsystem = buffer[PKT_HEADER_SUBSYS_FIELD];
		pkt.header.cmdId = buffer[PKT_HEADER_CMDID_FIELD];

		for (int pktIdx = 0; pktIdx < pkt.header.len; pktIdx++) {
			pkt.packed_protobuf_packet[pktIdx] = buffer[PKT_HEADER_DATA_FIELD
					+ pktIdx];
		}

		//switch (pkt.header.subsystem) {
		//case zStackNwkMgrSysId_t.RPC_SYS_PB_NWK_MGR_VALUE:
			nwkmngrParseIncoming(pkt);
		//	break;

		//}

	}

	private void nwkmngrParseIncoming(final pkt_buf_t pkt) {
		switch (pkt.header.cmdId) {
		case nwkMgrCmdId_t.NWK_GET_DEVICE_LIST_CNF_VALUE:
			final NwkGetDeviceListCnf.Builder nwkGetDeviceListCnf = NwkGetDeviceListCnf
					.newBuilder();
			try {
				nwkGetDeviceListCnf.mergeFrom(pkt.packed_protobuf_packet);
				/*System.out.println(
						"NWK_GET_DEVICE_LIST_CNF_VALUE: device cnt:" + nwkGetDeviceListCnf.getDeviceListCount());
				for(int devIdx = 0; devIdx < nwkGetDeviceListCnf.getDeviceListCount(); devIdx++)
				{
					System.out.println(
							"NWK_GET_DEVICE_LIST_CNF: device [" + devIdx + "]: nwkAddr " 
									+ nwkGetDeviceListCnf.getDeviceListList().get(devIdx).getNetworkAddress());									
				}*/
				
				if(nwkMngrCallbacks != null)
				{
					nwkMngrCallbacks.nwkMngrUpdateDeviceList(nwkGetDeviceListCnf.getDeviceListList());
				}
				
			} catch (final InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;	
			
		case nwkMgrCmdId_t.NWK_ZIGBEE_DEVICE_IND_VALUE:
			final  NwkZigbeeDeviceInd.Builder  nwkZigbeeDeviceInd =  NwkZigbeeDeviceInd
					.newBuilder();
			try {
				nwkZigbeeDeviceInd.mergeFrom(pkt.packed_protobuf_packet);
				
				//System.out.println(
				//		"NWK_ZIGBEE_DEVICE_IND: device nwkAddr " 
				//				+ nwkZigbeeDeviceInd.getDeviceInfo());	
				
				nwkMngrCallbacks.nwkMngrUpdateDevice(nwkZigbeeDeviceInd.getDeviceInfo());
				
			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;						
			
		default:
			System.out.println(
					"nwkmngrParseIncoming: unhandled cmd:" + pkt.header.cmdId);
			break;						
		}
	}

	public void nwkMngrOpenNetworkReq(final int openType, final int openDuration) {
		final NwkSetPermitJoinReq.Builder nwkSetPermitJoinReq = NwkSetPermitJoinReq
				.newBuilder();
		nwkSetPermitJoinReq.clear();
		nwkSetPermitJoinReq.setCmdId(nwkMgrCmdId_t.NWK_SET_PERMIT_JOIN_REQ);
		nwkSetPermitJoinReq.setPermitJoin(nwkPermitJoinType_t.PERMIT_NETWORK);
		nwkSetPermitJoinReq.setPermitJoinTime(openDuration);

		final List<String> err = nwkSetPermitJoinReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("nwkGetDeviceListReq error - " + err.toString());
			return;
		}
		
		final byte pkt[] = new byte[nwkSetPermitJoinReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (nwkSetPermitJoinReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((nwkSetPermitJoinReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackNwkMgrSysId_t.RPC_SYS_PB_NWK_MGR_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = nwkMgrCmdId_t.NWK_SET_PERMIT_JOIN_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < nwkSetPermitJoinReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = nwkSetPermitJoinReq.build()
					.toByteArray()[pktIdx];
		}

		sendClientMsg(pkt);

		return;
	}

	public void nwkMngrDevListReq() {
		final NwkGetDeviceListReq.Builder nwkGetDeviceListReq = NwkGetDeviceListReq
				.newBuilder();
		nwkGetDeviceListReq.clear();
		nwkGetDeviceListReq.setCmdId(nwkMgrCmdId_t.NWK_GET_DEVICE_LIST_REQ);

		final byte pkt[] = new byte[nwkGetDeviceListReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (nwkGetDeviceListReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((nwkGetDeviceListReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackNwkMgrSysId_t.RPC_SYS_PB_NWK_MGR_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = nwkMgrCmdId_t.NWK_GET_DEVICE_LIST_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < nwkGetDeviceListReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = nwkGetDeviceListReq.build()
					.toByteArray()[pktIdx];
		}

		final List<String> err = nwkGetDeviceListReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("nwkGetDeviceListReq error - " + err.toString());
		} else {
			sendClientMsg(pkt);
		}
		return;
	}
}
