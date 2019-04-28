/*
 * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package thamizh.andro.org.diglossia.net;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleQueue {
	
	private static List<Message> lastRecievedMessages = null;
	public static final String QUEUE_URL = "_queue_url"; 
	public static final String MESSAGE_INDEX = "_message_index";
	public static final String MESSAGE_ID = "_message_id";
    public static AmazonClientManager clientManager = null;
	public static AmazonSQSClient getInstance() {
        return clientManager.sqs();
	}
	
	public static CreateQueueResult createQueue(String queueName){
		CreateQueueRequest req = new CreateQueueRequest(queueName);
		return getInstance().createQueue(req);
	}
	
	public static List<String> getQueueUrls(){
		return getInstance().listQueues().getQueueUrls();
	}

	
	public static List<String> recieveMessageBodies(String queueUrl){
		List<String> messageBodies = new ArrayList<String>();
        try {
            ReceiveMessageRequest req = new ReceiveMessageRequest(queueUrl);
            req.setMaxNumberOfMessages(1);
            lastRecievedMessages = getInstance().receiveMessage(req).getMessages();
			if(lastRecievedMessages == null)
				return null;
            for (Message m : lastRecievedMessages) {
                messageBodies.add(m.getBody().toString());
                DeleteMessageRequest request = new DeleteMessageRequest();
                request.setQueueUrl(queueUrl);
                request.setReceiptHandle(m.getReceiptHandle());
                getInstance().deleteMessage(request);
            }
			return messageBodies;
        } catch(AmazonClientException e){
            return null;
        } catch (Exception e) {
            return null;
        }
	}
	
	public static List<String> recieveMessageIds(String queueUrl){
		List<String> messageIds = new ArrayList<String>();
		ReceiveMessageRequest req = new ReceiveMessageRequest(queueUrl);
		req.setMaxNumberOfMessages(1);
		lastRecievedMessages = getInstance().receiveMessage(req).getMessages();
		for(Message m : lastRecievedMessages){
			messageIds.add(m.getMessageId().toString());
		}
		return messageIds;
	}
	
	
	
	public static String getMessageBody(String Url,int messageIndex){
		String returnStr = new String();
		if(lastRecievedMessages != null && lastRecievedMessages.size() > 0) {
			returnStr = lastRecievedMessages.get(messageIndex).getBody().toString();
			DeleteMessageRequest request = new DeleteMessageRequest();
			request.setQueueUrl(Url);
			request.setReceiptHandle(lastRecievedMessages.get(messageIndex).getReceiptHandle());
			getInstance().deleteMessage(request);
			lastRecievedMessages.remove(messageIndex);
		}
		return returnStr;
	}
	public static String getMessageHandle(int messageIndex){
		if(lastRecievedMessages == null) {
			return new String();
		} else {
			return lastRecievedMessages.get(messageIndex).getReceiptHandle();
		}
	}
	public static String deleteMessage(String Url,int messageIndex){
		if(lastRecievedMessages == null) {
			return new String();
		} else {
			DeleteMessageRequest request = new DeleteMessageRequest();
			request.setQueueUrl(Url);
			request.setReceiptHandle(lastRecievedMessages.get(messageIndex).getReceiptHandle());
			getInstance().deleteMessage(request);
			lastRecievedMessages.remove(messageIndex);
			return "success";
		}
	}
	
	public static SendMessageResult sendMessage(String queueUrl, String body){
        try{
		SendMessageRequest req = new SendMessageRequest(queueUrl, body);
		return getInstance().sendMessage(req);
        } catch(AmazonClientException e){
            return null;
        } catch (Exception e) {
            return null;
        }
	}
	public static SendMessageBatchResult sendMessageBatch(String queueUrl, List<SendMessageBatchRequestEntry> messages){
		SendMessageBatchRequest req = new SendMessageBatchRequest(queueUrl, messages);
		return getInstance().sendMessageBatch(req);
	}
	
	public static void deleteMessage(String queueName, String msgHandle){
		DeleteMessageRequest req = new DeleteMessageRequest(createQueue(queueName).getQueueUrl(), msgHandle);
		getInstance().deleteMessage(req);
	}

	public static String getQueueArn(String queueUrl){
		GetQueueAttributesRequest req = new GetQueueAttributesRequest(queueUrl).withAttributeNames("QueueArn");
		return getInstance().getQueueAttributes(req).getAttributes().get("QueueArn").toString();
	}
	
    public static void allowNotifications(String queueUrl, String topicArn){
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("Policy", generateSqsPolicyForTopic(getQueueArn(queueUrl), topicArn));
        getInstance().setQueueAttributes(new SetQueueAttributesRequest(queueUrl, attributes));
    }
	
    private static String generateSqsPolicyForTopic(String queueArn, String topicArn) {
        String policy =
            "{ " +
            "  \"Version\":\"2008-10-17\"," +
            "  \"Id\":\"" + queueArn + "/policyId\"," +
            "  \"Statement\": [" +
            "    {" +
            "        \"Sid\":\"" + queueArn + "/statementId\"," +
            "        \"Effect\":\"Allow\"," +
            "        \"Principal\":{\"AWS\":\"*\"}," +
            "        \"Action\":\"SQS:SendMessage\"," +
            "        \"Resource\": \"" + queueArn + "\"," +
            "        \"Condition\":{" +
            "            \"StringEquals\":{\"aws:SourceArn\":\"" + topicArn + "\"}" +
            "        }" +
            "    }" +
            "  ]" +
            "}";

        return policy;
    }



}
