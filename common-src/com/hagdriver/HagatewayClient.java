/**************************************************************************************************
  Filename:       HagatewayClient.java
  Revised:        $$
  Revision:       $$

  Description:    HomeAutomation Gateway Client Class

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

import com.hagdriver.hagatewayPb.hagatewayPb.DevGetColorReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetColorRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetLevelReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetLevelRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetOnOffStateReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetOnOffStateRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetTempReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevGetTempRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.DevSetColorReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevSetLevelReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevSetOnOffStateReq;
import com.hagdriver.hagatewayPb.hagatewayPb.DevThermostatSetpointChangeReq;
import com.hagdriver.hagatewayPb.hagatewayPb.GwReadDeviceAttributeReq;
import com.hagdriver.hagatewayPb.hagatewayPb.GwReadDeviceAttributeRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.GwSendZclFrameReq;
import com.hagdriver.hagatewayPb.hagatewayPb.GwWriteDeviceAttributeReq;
import com.hagdriver.hagatewayPb.hagatewayPb.GwWriteDeviceAttributeRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.GwZclFrameReceiveInd;
import com.hagdriver.hagatewayPb.hagatewayPb.GwZigbeeGenericRspInd;
import com.hagdriver.hagatewayPb.hagatewayPb.gwAddressType_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwClientServerDir_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwCmdId_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwDisableDefaultRsp_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwFrameType_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwMfrSpecificFlag_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwOnOffState_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwQualityOfService_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwSecurityOptions_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwStatus_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwThermostatSetpointMode_t;
import com.hagdriver.hagatewayPb.hagatewayPb.gwZclAttributeDataTypes_t;
import com.hagdriver.hagatewayPb.hagatewayPb.zStackGwSysId_t;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;


public class HagatewayClient extends ClientConnection  {

	private final String logTag = "HagatewayClient";
	
	final private int HAGATEWAY_SERVER_PORT = 2541;	
	
	private Map<Integer, HagatewayZclCallback> indCallbackList = new HashMap<Integer, HagatewayZclCallback>();
	
	public interface HagatewayZclCallback {
		void zclGenericGetSetRspInd(
				byte[] data, gwStatus_t status);
	}
	
	class ZclHeader {
		final static int ZCL_HEADER_SIZE = 3;				
		
		gwFrameType_t frameType;
		gwClientServerDir_t dir;
		boolean manSpecific;
		int manufacturerCode;
		gwDisableDefaultRsp_t disableDefualtRsp;
		int zclCommandId;
	}
	
	public HagatewayClient(final String srvrIp) {
		setServerPort(HAGATEWAY_SERVER_PORT);
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
		pkt.header.len = (short) ( lowByte + (highByte<< 8));

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

		hagatewayParseIncoming(pkt);
	}

	private void hagatewayParseIncoming(final pkt_buf_t pkt) {
		
		int seqNum = 0;
		
		switch (pkt.header.cmdId) {		
		case gwCmdId_t.DEV_GET_ONOFF_STATE_RSP_IND_VALUE:
			final DevGetOnOffStateRspInd.Builder devGetOnOffStateRspInd = DevGetOnOffStateRspInd
					.newBuilder();
			try {
				devGetOnOffStateRspInd.mergeFrom(pkt.packed_protobuf_packet);
			
                gwStatus_t status = devGetOnOffStateRspInd.getStatus();
                seqNum = devGetOnOffStateRspInd.getSequenceNumber();
                byte[] data = new byte[1];
                if(status == gwStatus_t.STATUS_SUCCESS)
                {
					data[0] = (byte) devGetOnOffStateRspInd.getStateValue().getNumber();
                }
                
                HagatewayZclCallback cb = indCallbackList.get(seqNum);
                if(cb != null)
                {
                	indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);
                }

			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;
		case gwCmdId_t.DEV_GET_LEVEL_RSP_IND_VALUE:
			final DevGetLevelRspInd.Builder devGetLevelRspInd = DevGetLevelRspInd
					.newBuilder();
			try {
				devGetLevelRspInd.mergeFrom(pkt.packed_protobuf_packet);
			
                gwStatus_t status = devGetLevelRspInd.getStatus();
                seqNum = devGetLevelRspInd.getSequenceNumber();
                byte[] data = new byte[1];
                if(status == gwStatus_t.STATUS_SUCCESS)
                {
					data[0] = (byte) devGetLevelRspInd.getLevelValue();
                }
                
                HagatewayZclCallback cb = indCallbackList.get(seqNum);
                if(cb != null)
                {
                	indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);
                }

			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;

		case gwCmdId_t.DEV_GET_COLOR_RSP_IND_VALUE:
			final DevGetColorRspInd.Builder devGetColorRspInd = DevGetColorRspInd
					.newBuilder();
			try {
				devGetColorRspInd.mergeFrom(pkt.packed_protobuf_packet);
			
                gwStatus_t status = devGetColorRspInd.getStatus();
                seqNum = devGetColorRspInd.getSequenceNumber();
                byte[] data = new byte[2];
                if(status == gwStatus_t.STATUS_SUCCESS)
                {           
					if(data != null)
					{
						data[0] = (byte) devGetColorRspInd.getHueValue();
						data[1] = (byte) devGetColorRspInd.getSatValue();
					}
                }
                HagatewayZclCallback cb = indCallbackList.get(seqNum);
                if(cb != null)
                {
                	indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);
                }

			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;

		case gwCmdId_t.DEV_GET_TEMP_RSP_IND_VALUE:
			final DevGetTempRspInd.Builder devGetTempRspInd = DevGetTempRspInd
					.newBuilder();
			try {
				devGetTempRspInd.mergeFrom(pkt.packed_protobuf_packet);
			
                gwStatus_t status = devGetTempRspInd.getStatus();
                seqNum = devGetTempRspInd.getSequenceNumber();
                byte[] data = new byte[2];
                if(status == gwStatus_t.STATUS_SUCCESS)
                {
                	ByteString bsTemp = int16ToByteString((short) devGetTempRspInd.getTemperatureValue());
                	bsTemp.copyTo(data, 0);
                }
                
                indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);

			} catch (final InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			break;
			
		case gwCmdId_t.GW_READ_DEVICE_ATTRIBUTE_RSP_IND_VALUE:
            final GwReadDeviceAttributeRspInd.Builder gwReadDeviceAttributeRspInd = GwReadDeviceAttributeRspInd.newBuilder();
            try {                                               
                gwReadDeviceAttributeRspInd.mergeFrom(pkt.packed_protobuf_packet);
                gwStatus_t status = gwReadDeviceAttributeRspInd.getStatus();
                seqNum = gwReadDeviceAttributeRspInd.getSequenceNumber();                
                
                byte[] data = null;
                if(status == gwStatus_t.STATUS_SUCCESS)
                {
                	data = gwReadDeviceAttributeRspInd.getAttributeRecordList(0).getAttributeValue().toByteArray();
                }
                                
                if(indCallbackList.get(seqNum) != null)
                {
                    indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);                	
                }
                
            } catch (final InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            break;
        case gwCmdId_t.GW_WRITE_DEVICE_ATTRIBUTE_RSP_IND_VALUE:
            final GwWriteDeviceAttributeRspInd.Builder gwWriteDeviceAttributeRspInd = GwWriteDeviceAttributeRspInd.newBuilder();
            try {                                               
            	gwWriteDeviceAttributeRspInd.mergeFrom(pkt.packed_protobuf_packet);
                gwStatus_t status = gwWriteDeviceAttributeRspInd.getStatus();
                seqNum = gwWriteDeviceAttributeRspInd.getSequenceNumber();
                byte[] data = null;            
                
                indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);
                
            } catch (final InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            break;            
        case gwCmdId_t.GW_ZCL_FRAME_RECEIVE_IND_VALUE:
            final GwZclFrameReceiveInd.Builder gwZclFrameReceiveInd = GwZclFrameReceiveInd.newBuilder();
            try {                                               
            	gwZclFrameReceiveInd.mergeFrom(pkt.packed_protobuf_packet);        			        		
        		if( gwZclFrameReceiveInd.getManufacturerSpecificFlag() == gwMfrSpecificFlag_t.MFR_SPECIFIC &&
        			gwZclFrameReceiveInd.getFrameType() == gwFrameType_t.FRAME_VALID_ACCROSS_PROFILE &&
        			gwZclFrameReceiveInd.getCommandId() == 0 /*READ_ATTR_REQ*/)        			
        		{
        			/* This is a manufacturer specific ZCL attr read rsp*/
        			gwStatus_t status = gwStatus_t.STATUS_FAILURE;
        			//read the zcl status
        			if(gwZclFrameReceiveInd.getPayload().byteAt(3) == 1)
        			{
        				status = gwStatus_t.STATUS_SUCCESS;
        			}
        			                    
                    seqNum = gwZclFrameReceiveInd.getSequenceNumber();
                    byte[] data = null;
                    if(status == gwStatus_t.STATUS_SUCCESS)
                    {
                    	//Copy payload from 4th byte
                    	data = Arrays.copyOfRange(gwZclFrameReceiveInd.getPayload().toByteArray(), 
                    			4, (gwZclFrameReceiveInd.getPayload().toByteArray().length-4));
                    }            

                    indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);

        		}
        		else if( gwZclFrameReceiveInd.getManufacturerSpecificFlag() == gwMfrSpecificFlag_t.MFR_SPECIFIC &&
            			gwZclFrameReceiveInd.getFrameType() == gwFrameType_t.FRAME_VALID_ACCROSS_PROFILE &&
            			gwZclFrameReceiveInd.getCommandId() == 1 /*WRITE_ATTR_REQ*/)        			
            	{
           			/*This is a manufacturer specific ZCL attr write rsp*/
        			gwStatus_t status = gwStatus_t.STATUS_FAILURE;
        			//read the zcl status
        			if(gwZclFrameReceiveInd.getPayload().byteAt(3) == 1)
        			{
        				status = gwStatus_t.STATUS_SUCCESS;
        			}
        			                    
                    seqNum = gwZclFrameReceiveInd.getSequenceNumber();
                    byte[] data = null;          

                    indCallbackList.get(seqNum).zclGenericGetSetRspInd(data, status);
            	}
        		else
        		{
        			/*Deal with other cases like report attr*/
        		}        		
            } catch (final InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            break;            
        case gwCmdId_t.ZIGBEE_GENERIC_RSP_IND_VALUE:
        	final GwZigbeeGenericRspInd.Builder gwZigbeeGenericRspInd = GwZigbeeGenericRspInd.newBuilder();                           
			try {
				gwZigbeeGenericRspInd.mergeFrom(pkt.packed_protobuf_packet);
				System.out.println(
						"hagatewayParseIncoming: ZigbeeGenericRspInd - " + gwZigbeeGenericRspInd.getStatus().toString());				
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
        	break;
		default:
			System.out.println(
					"hagatewayParseIncoming: unhandled cmd:" + pkt.header.cmdId);

			break;						
		}
	}

	public int hagwReadAttrReq(final long devIeee, final int ep, final short attrReqClusterId, 
			final short attrReqAttrId, HagatewayZclCallback zclCallback) {
		final GwReadDeviceAttributeReq.Builder gwReadDeviceAttributeReq = GwReadDeviceAttributeReq
				.newBuilder();
		gwReadDeviceAttributeReq.clear();
		gwReadDeviceAttributeReq.setCmdId(gwCmdId_t.GW_WRITE_DEVICE_ATTRIBUTE_REQ);			
		gwReadDeviceAttributeReq.setClusterId(attrReqClusterId);		
				
		//set dst addr
		gwReadDeviceAttributeReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		gwReadDeviceAttributeReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		gwReadDeviceAttributeReq.getDstAddressBuilder().setEndpointId(ep);
		
		//set Attr						
		gwReadDeviceAttributeReq.addAttributeList(attrReqAttrId); 
		
		final List<String> err = gwReadDeviceAttributeReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwReadAttrReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[gwReadDeviceAttributeReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (gwReadDeviceAttributeReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((gwReadDeviceAttributeReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.GW_READ_DEVICE_ATTRIBUTE_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < gwReadDeviceAttributeReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = gwReadDeviceAttributeReq.build()
					.toByteArray()[pktIdx];
		}
		
		int genericCnfSeq=-1;		
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
		
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwWriteAttrReq(final long devIeee, final int ep, final short attrReqClusterId, 
			final short attrReqAttrId, final ByteString attrReqAttrVal, 
			gwZclAttributeDataTypes_t attrReqAttrType, HagatewayZclCallback zclCallback) {						
		
		final GwWriteDeviceAttributeReq.Builder gwWriteDeviceAttributeReq = GwWriteDeviceAttributeReq
				.newBuilder();
		gwWriteDeviceAttributeReq.clear();
		gwWriteDeviceAttributeReq.setCmdId(gwCmdId_t.GW_WRITE_DEVICE_ATTRIBUTE_REQ);
		gwWriteDeviceAttributeReq.setClusterId(attrReqClusterId);
		
		//set dst addr
		gwWriteDeviceAttributeReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		gwWriteDeviceAttributeReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		gwWriteDeviceAttributeReq.getDstAddressBuilder().setEndpointId(ep);
		
		//set Attr Record				
		gwWriteDeviceAttributeReq.addAttributeRecordListBuilder();		
		gwWriteDeviceAttributeReq.getAttributeRecordListBuilder(0).setAttributeType(attrReqAttrType);
		gwWriteDeviceAttributeReq.getAttributeRecordListBuilder(0).setAttributeId(attrReqAttrId);		
		gwWriteDeviceAttributeReq.getAttributeRecordListBuilder(0).setAttributeValue(attrReqAttrVal);			

		final List<String> err = gwWriteDeviceAttributeReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwWriteAttrReq error - " + err.toString());
			return -1;
		} 

		final byte pkt[] = new byte[gwWriteDeviceAttributeReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (gwWriteDeviceAttributeReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((gwWriteDeviceAttributeReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.GW_WRITE_DEVICE_ATTRIBUTE_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < gwWriteDeviceAttributeReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = gwWriteDeviceAttributeReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
				
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwReadManuSpecAttrReq(long devIeee, int ep, int srcEp, int manufacturerCode, short attrReqMspProfileId, 
			short attrReqClusterId, short attrReqAttrId, HagatewayZclCallback zclCallback) {		
		
		ZclHeader zclHeader = new ZclHeader();	
		
		zclHeader.frameType = gwFrameType_t.FRAME_VALID_ACCROSS_PROFILE;
		zclHeader.manSpecific = true;
		zclHeader.manufacturerCode = manufacturerCode;
		zclHeader.dir = gwClientServerDir_t.CLIENT_TO_SERVER;		
		zclHeader.disableDefualtRsp = gwDisableDefaultRsp_t.DEFAULT_RSP_DISABLED;
		zclHeader.zclCommandId = 0; //READ_ATTR_REQ		
		
		final byte[] zclPayload = new byte[2];
		zclPayload[0] = (byte) ((attrReqAttrId) & 0xFF);
		zclPayload[1] = (byte) ((attrReqAttrId & 0xFF00) >> 8);
		final ByteString zclPayloadByteString = ByteString.copyFrom(zclPayload);
		
		int cnf = hagwZclReq(devIeee, ep, srcEp, attrReqClusterId, attrReqMspProfileId, 
				zclHeader, zclPayloadByteString, zclCallback);
		
		return cnf;			
	}

	public int hagwWriteManuSpecAttrReq(long devIeee, int ep, int srcEp, int manufacturerCode, short attrReqMspProfileId, 
			short attrReqClusterId, final short attrReqAttrId, final ByteString attrReqAttrVal, 
			gwZclAttributeDataTypes_t attrReqAttrType, HagatewayZclCallback zclCallback ) {		
		
		ZclHeader zclHeader = new ZclHeader();	
		
		zclHeader.frameType = gwFrameType_t.FRAME_VALID_ACCROSS_PROFILE;
		zclHeader.manSpecific = true;
		zclHeader.manufacturerCode = manufacturerCode;
		zclHeader.dir = gwClientServerDir_t.CLIENT_TO_SERVER;		
		zclHeader.disableDefualtRsp = gwDisableDefaultRsp_t.DEFAULT_RSP_DISABLED;
		zclHeader.zclCommandId = 1; //WRITE_ATTR_REQ		
		
		final byte[] zclPayload = new byte[4];
		zclPayload[0] = (byte) ((attrReqAttrId) & 0xFF);
		zclPayload[1] = (byte) ((attrReqAttrId & 0xFF00) >> 8);
		zclPayload[2] = (byte) attrReqAttrType.getNumber();
		final ByteString zclPayloadByteString = ByteString.copyFrom(zclPayload);
		
		zclPayloadByteString.concat(attrReqAttrVal);
		
		int cnf = hagwZclReq(devIeee, ep, srcEp, attrReqClusterId, attrReqMspProfileId, 
				zclHeader, zclPayloadByteString, zclCallback);
		
		return cnf;		
	}
	
	public int hagwZclReq(long devIeee, int ep, int srcEp, short attrReqMspProfileId, short attrReqClusterId, 
			ZclHeader zclHeader, ByteString zcloayload, HagatewayZclCallback zclCallback) {							
		
		final  GwSendZclFrameReq.Builder gwSendZclFrameReq = GwSendZclFrameReq.newBuilder();
		gwSendZclFrameReq.clear();
		gwSendZclFrameReq.setCmdId(gwCmdId_t.GW_SEND_ZCL_FRAME_REQ);
		gwSendZclFrameReq.setClusterId(attrReqClusterId);				
		
		//set dst addr
		gwSendZclFrameReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		gwSendZclFrameReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		gwSendZclFrameReq.getDstAddressBuilder().setEndpointId(ep);
		
		//set source endpoint
		gwSendZclFrameReq.setEndpointIdSource(srcEp);
		
		//set ZCL Header			
		gwSendZclFrameReq.setFrameType(zclHeader.frameType);
		gwSendZclFrameReq.setClientServerDirection(zclHeader.dir);
		gwSendZclFrameReq.setDisableDefaultRsp(zclHeader.disableDefualtRsp);
		if(zclHeader.manSpecific)
		{
			gwSendZclFrameReq.setManufacturerSpecificFlag(gwMfrSpecificFlag_t.MFR_SPECIFIC);
			gwSendZclFrameReq.setManufacturerCode(zclHeader.manufacturerCode);
			gwSendZclFrameReq.setProfileId(attrReqMspProfileId);			
		}
		else
		{
			gwSendZclFrameReq.setManufacturerSpecificFlag(gwMfrSpecificFlag_t.NON_MFR_SPECIFIC);			
		}
		
		gwSendZclFrameReq.setCommandId(zclHeader.zclCommandId);
		
		//set payload
		gwSendZclFrameReq.setPayload(zcloayload);

		//Todo: Should these be exposed through the API		
		gwSendZclFrameReq.setQualityOfService(gwQualityOfService_t.APS_NOT_ACK);
		gwSendZclFrameReq.setSecurityOptions(gwSecurityOptions_t.APS_SECURITY_DISABLED);		
				
		final List<String> err = gwSendZclFrameReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwZclReq error - " + err.toString());
			return -1;
		}

		final byte pkt[] = new byte[gwSendZclFrameReq.build().getSerializedSize()
		            				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (gwSendZclFrameReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((gwSendZclFrameReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.GW_SEND_ZCL_FRAME_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < gwSendZclFrameReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = gwSendZclFrameReq.build()
					.toByteArray()[pktIdx];
		}
		
		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwSetDevStateReq(final long devIeee, final int ep, final gwOnOffState_t state,
			HagatewayZclCallback zclCallback) {									
		
		final DevSetOnOffStateReq.Builder devSetOnOffStateReq = DevSetOnOffStateReq
				.newBuilder();
		devSetOnOffStateReq.clear();
		devSetOnOffStateReq.setCmdId(gwCmdId_t.DEV_SET_ONOFF_STATE_REQ);
		devSetOnOffStateReq.setState(state);

		//set dst addr
		devSetOnOffStateReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devSetOnOffStateReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devSetOnOffStateReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devSetOnOffStateReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwSetDevStateReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devSetOnOffStateReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devSetOnOffStateReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devSetOnOffStateReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_SET_ONOFF_STATE_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devSetOnOffStateReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devSetOnOffStateReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwGetDevStateReq(final long devIeee, final int ep, HagatewayZclCallback zclCallback) {					
				
		final DevGetOnOffStateReq.Builder devGetOnOffStateReq = DevGetOnOffStateReq
				.newBuilder();
		devGetOnOffStateReq.clear();
		devGetOnOffStateReq.setCmdId(gwCmdId_t.DEV_GET_ONOFF_STATE_REQ);

		//set dst addr
		devGetOnOffStateReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devGetOnOffStateReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devGetOnOffStateReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devGetOnOffStateReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwGetDevStateReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devGetOnOffStateReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devGetOnOffStateReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devGetOnOffStateReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_GET_ONOFF_STATE_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devGetOnOffStateReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devGetOnOffStateReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}

	public int hagwSetDevLevelReq(final long devIeee, final int ep, int level,
			HagatewayZclCallback zclCallback) {									
		
		final DevSetLevelReq.Builder devSetLevelReq = DevSetLevelReq
				.newBuilder();
		devSetLevelReq.clear();
		devSetLevelReq.setCmdId(gwCmdId_t.DEV_SET_LEVEL_REQ);
		devSetLevelReq.setLevelValue(level);
		//hardcode transition time to 1s
		devSetLevelReq.setTransitionTime(10);

		//set dst addr
		devSetLevelReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devSetLevelReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devSetLevelReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devSetLevelReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwSetDevLevelReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devSetLevelReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devSetLevelReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devSetLevelReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_SET_LEVEL_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devSetLevelReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devSetLevelReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwGetDevLevelReq(final long devIeee, final int ep, HagatewayZclCallback zclCallback) {					
				
		final DevGetLevelReq.Builder devGetLevelReq = DevGetLevelReq
				.newBuilder();
		devGetLevelReq.clear();
		devGetLevelReq.setCmdId(gwCmdId_t.DEV_GET_LEVEL_REQ);

		//set dst addr
		devGetLevelReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devGetLevelReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devGetLevelReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devGetLevelReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwGetDevLevelReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devGetLevelReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devGetLevelReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devGetLevelReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_GET_LEVEL_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devGetLevelReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devGetLevelReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}

	public int hagwSetDevColorReq(final long devIeee, final int ep, int hue, int sat,
			HagatewayZclCallback zclCallback) {									
		
		final DevSetColorReq.Builder devSetColorReq = DevSetColorReq
				.newBuilder();
		devSetColorReq.clear();
		devSetColorReq.setCmdId(gwCmdId_t.DEV_SET_COLOR_REQ);
		devSetColorReq.setHueValue(hue);
		devSetColorReq.setSaturationValue(sat);

		//set dst addr
		devSetColorReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devSetColorReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devSetColorReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devSetColorReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwSetDevColorReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devSetColorReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devSetColorReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devSetColorReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_SET_COLOR_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devSetColorReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devSetColorReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	public int hagwGetDevColorReq(final long devIeee, final int ep, HagatewayZclCallback zclCallback) {					
				
		final DevGetColorReq.Builder devGetColorReq = DevGetColorReq
				.newBuilder();
		devGetColorReq.clear();
		devGetColorReq.setCmdId(gwCmdId_t.DEV_GET_COLOR_REQ);

		//set dst addr
		devGetColorReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devGetColorReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devGetColorReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devGetColorReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwGetDevColourReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devGetColorReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devGetColorReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devGetColorReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_GET_COLOR_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devGetColorReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devGetColorReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}

	public int hagwGetDevTempReq(final long devIeee, final int ep, HagatewayZclCallback zclCallback) {					
		
		final DevGetTempReq.Builder devGetTempReq = DevGetTempReq
				.newBuilder();
		devGetTempReq.clear();
		devGetTempReq.setCmdId(gwCmdId_t.DEV_GET_TEMP_REQ);

		//set dst addr
		devGetTempReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devGetTempReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devGetTempReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devGetTempReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwGetDevTempReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devGetTempReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devGetTempReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devGetTempReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_GET_TEMP_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devGetTempReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devGetTempReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
		
	public int hagwSetPointChangeReq(final long devIeee, final int ep, final int setPointChange,
			gwThermostatSetpointMode_t mode, HagatewayZclCallback zclCallback) {							
		
		final DevThermostatSetpointChangeReq.Builder devThermostatSetpointChangeReq = DevThermostatSetpointChangeReq
				.newBuilder();
		devThermostatSetpointChangeReq.clear();
		devThermostatSetpointChangeReq.setCmdId(gwCmdId_t.DEV_THERMOSTAT_SETPOINT_CHANGE_REQ);
		devThermostatSetpointChangeReq.setMode(mode);
		devThermostatSetpointChangeReq.setAmount(setPointChange);

		//set dst addr
		devThermostatSetpointChangeReq.getDstAddressBuilder().setAddressType(gwAddressType_t.UNICAST);
		devThermostatSetpointChangeReq.getDstAddressBuilder().setIeeeAddr(devIeee);
		devThermostatSetpointChangeReq.getDstAddressBuilder().setEndpointId(ep);

		final List<String> err = devThermostatSetpointChangeReq.findInitializationErrors();
		if (!err.isEmpty()) {
			System.out.println("hagwSetPointChangeReq error - " + err.toString());
			return -1;
		}
		
		final byte pkt[] = new byte[devThermostatSetpointChangeReq.build().getSerializedSize()
				+ PKT_HEADER_SIZE];

		pkt[PKT_HEADER_LEN_FIELD] = (byte) (devThermostatSetpointChangeReq.build()
				.getSerializedSize() & 0xFF);
		pkt[PKT_HEADER_LEN_FIELD + 1] = (byte) ((devThermostatSetpointChangeReq.build()
				.getSerializedSize() & 0xFF00) << 8);
		pkt[PKT_HEADER_SUBSYS_FIELD] = zStackGwSysId_t.RPC_SYS_PB_GW_VALUE;
		pkt[PKT_HEADER_CMDID_FIELD] = gwCmdId_t.DEV_THERMOSTAT_SETPOINT_CHANGE_REQ_VALUE;

		for (int pktIdx = 0; pktIdx < devThermostatSetpointChangeReq.build().toByteArray().length; pktIdx++) {
			pkt[PKT_HEADER_DATA_FIELD + pktIdx] = devThermostatSetpointChangeReq.build()
					.toByteArray()[pktIdx];
		}

		int genericCnfSeq = -1;
		genericCnfSeq = sendClientMsgWaitCnf(pkt);
	
		if(zclCallback != null)
		{
			indCallbackList.put(genericCnfSeq, zclCallback);
		}

		return genericCnfSeq;
	}
	
	
/*******
 * 	Utility functions
 */
	public static boolean byteToBool(byte data){
		//convert data to a boolean
		Boolean bool = ((data & 0x1) != 0);
		return bool;
	}

	public static ByteBuffer byteArrayToByteBuffer(byte data[])
	{
		ByteBuffer byteBuff = ByteBuffer.allocate(data.length);
		byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		for(int byteIdx = 0; byteIdx < data.length; byteIdx++)
		{
			byteBuff.put(data[byteIdx]);
		}
		
		return byteBuff;
	}
	
	public static short byteArrayToInt16(byte data[])
	{
		//copy data into byte buffer
		ByteBuffer int16ByteBuff = byteArrayToByteBuffer(data);
		//extract the short
		return int16ByteBuff.getShort(0);								
	}
	
	public static int byteArrayToInt32(byte data[])
	{
		//copy data into byte buffer
		ByteBuffer int16ByteBuff = byteArrayToByteBuffer(data);
		//extract the short
		return int16ByteBuff.getInt(0);								
	}
	
	public static long byteArrayToUint(byte data[])
	{
		//make container lager than an int to so it is always unsigned
		long uIntAttr = 0;
		for(int dataIdx=0; dataIdx < data.length; dataIdx++)
		{
			uIntAttr += ((data[dataIdx] & 0xFF)<<(dataIdx*8));									
		}										
		uIntAttr &= 0xFFFFFFFFL;		
		return uIntAttr;
	}
	
	public static ByteString int8ToByteString(byte int8Attr)
	{
		byte[] byteArrayAttr = new byte[1];
		byteArrayAttr[0] = (byte) (int8Attr & 0xff);
		
		return ByteString.copyFrom(byteArrayAttr);
	}
    
	public static ByteString int16ToByteString(short int16Attr)
	{
		byte[] byteArrayAttr = new byte[2];
		byteArrayAttr[0] = (byte) (int16Attr & 0xff);
		byteArrayAttr[1] = (byte) ((int16Attr >>> 8) & 0xff);
		
		return ByteString.copyFrom(byteArrayAttr);
	}
	
	
	public static ByteString int32ToByteString(Integer int32Attr)
	{
		byte[] byteArrayAttr = new byte[4];
	    byteArrayAttr[0] = (byte) (int32Attr & 0xff);
	    byteArrayAttr[1] = (byte) ((int32Attr >>> 8) & 0xff);
	    byteArrayAttr[2] = (byte) ((int32Attr >>> 16) & 0xff);
	    byteArrayAttr[3] = (byte) ((int32Attr >>> 24) & 0xff);		
		return ByteString.copyFrom(byteArrayAttr);
	}	
}
