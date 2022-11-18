import org.json.JSONObject;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactoryBuilder;
import io.split.client.api.SplitResult;

public class LambdaFunctionHandler{

	private SplitClient split;

	@Override
	public static void main(String[] args){
		context.getLogger().log("Input: " + input);
		
		if(split == null) {
			SplitClientConfig config = SplitClientConfig.builder()
					.setBlockUntilReadyTimeout(10000)
					.featuresRefreshRate(5)
					.build();
			try {
				split = SplitFactoryBuilder.build("3206ilbhucf42cca4ftrevbnoi9sg5egiqi", config).client();
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

			SplitResult result = split.getTreatmentWithConfig(userid, "dynamic_boxes");
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
