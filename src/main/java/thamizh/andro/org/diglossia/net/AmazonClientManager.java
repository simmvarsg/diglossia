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

import thamizh.andro.org.diglossia.utils.PropertyLoader;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
/**
* This class is used to get clients to the various AWS services.  Before accessing a client 
* the credentials should be checked to ensure validity.
*/
public class AmazonClientManager {
    private static final String LOG_TAG = "AmazonClientManager";

    private AmazonSQSClient sqsClient = null;
    
    public AmazonClientManager() {
    	
    }
                
            
    public AmazonSQSClient sqs() {
        validateCredentials();    
        return sqsClient;
    }

    
    public boolean hasCredentials() {
        return PropertyLoader.getInstance().hasCredentials();
    }
    
    public void validateCredentials() {
        if ( sqsClient == null ) {        
            //AWSCredentials credentials = new BasicAWSCredentials( "AKIAJUQ5IXQEWKWNRR4Q", "DFuMEX3m13F25TrZDj3ENw895EZa4nEY0l6FbJRg" );
            AWSCredentials credentials = new BasicAWSCredentials( "AKIAILGP4XZ7WSPZE7HA", "IxEVzJdnl8Jvvmxt4x6RVuU8MhaxA2I2hxGqdqJ+" );
            //AWSCredentials credentials = new BasicAWSCredentials( PropertyLoader.getInstance().getAccessKey(), PropertyLoader.getInstance().getSecretKey() );
		    sqsClient = new AmazonSQSClient( credentials );
        }
    }
    public void clearClients() {
        sqsClient = null;

    }
}
