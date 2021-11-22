package com.amazonaws.lambda.demo;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactoryBuilder;
import io.split.client.api.SplitResult;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

	private SplitClient split;

	@Override
	public String handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);
		
		if(split == null) {
			SplitClientConfig config = SplitClientConfig.builder()
					.setBlockUntilReadyTimeout(10000)
					.featuresRefreshRate(5)
					.build();
			try {
				split = SplitFactoryBuilder.build("3jueh9a4g1ksklep3f910s131abaj4sfo8mm", config).client();
				split.blockUntilReady();
			} catch (Exception e) {
				context.getLogger().log(e.getMessage());
			}			
		}
		
		String userid = "dmartin";
		if(input != null) {
			String rawString = input.toString();
			userid = rawString.replaceAll("\\{", "");
			userid = userid.replaceAll("\\}", "");
			userid = userid.substring(userid.indexOf("=") + 1);
		}

		String message = "not evaluated";
		try {

			SplitResult result = split.getTreatmentWithConfig(userid, "multivariant_demo");
			String treatment = result.treatment();
			if (result.config() != null) {
				JSONObject jsonObj = new JSONObject(result.config());
				message = jsonObj.toString();
			}
			message = treatment + " " + message;
		} catch (Exception e) {
			context.getLogger().log("Error! " + e.getMessage());
		}

		context.getLogger().log(message);
		return message;
	}

}
