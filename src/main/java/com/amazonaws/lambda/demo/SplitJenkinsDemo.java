package com.amazonaws.lambda.demo;
  
import io.split.client.SplitFactoryBuilder;
import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;

public class SplitJenkinsDemo{

    public static void main(String[] args) {

        SplitClientConfig config = SplitClientConfig.builder()
                   .setBlockUntilReadyTimeout(10000)
                   .enableDebug()
                   .build();
        try {

            SplitFactory splitFactory = SplitFactoryBuilder.build("82tpd7u6nu20l486vgav12loknaeunl28pu5", config);
            SplitClient client = splitFactory.client();
            client.blockUntilReady();

            String result = client.getTreatment("user10","jenkins_split_demo");
          
            System.out.print(result + "\n");
            if (result.config() != null) {
              System.out.print(result.config + "\n");
            }
        } catch (Exception e) {
            System.out.print("Exception: "+e.getMessage());
        }
    }
}  
