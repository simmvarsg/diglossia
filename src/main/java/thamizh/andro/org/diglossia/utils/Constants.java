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
package thamizh.andro.org.diglossia.utils;

import java.util.Locale;

public class Constants {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This sample App is for demonstration purposes only.
    // It is not secure to embed your credentials into source code.
    // Please read the following article for getting credentials
    // to devices securely.
    // http://aws.amazon.com/articles/Mobile/4611615499399490
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//public static final String ACCESS_KEY_ID = "AKIAJUQ5IXQEWKWNRR4Q";
	//public static final String SECRET_KEY = "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";
	public static final String ACCESS_KEY_ID = "AKIAILGP4XZ7WSPZE7HA";
	public static final String SECRET_KEY = "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";

	public static final String PICTURE_BUCKET = "welcometaxiservice";
	public static final String PICTURE_NAME = "NameOfThePicture";
	
	
	public static String getPictureBucket() {
		return (ACCESS_KEY_ID + PICTURE_BUCKET).toLowerCase(Locale.US);
	}
	
}








