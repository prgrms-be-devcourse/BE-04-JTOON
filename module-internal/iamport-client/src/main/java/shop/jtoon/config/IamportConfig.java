package shop.jtoon.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Bean
    public IamportClient iamportClient(
            @Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
            @Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey
    ) {
        return new IamportClient(restApiKey, restSecretKey);
    }
}
