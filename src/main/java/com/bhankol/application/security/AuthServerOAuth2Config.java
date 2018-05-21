package com.bhankol.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@PropertySource(value = {"classpath:/application.properties"})
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter 
{
    private static final String PROP_CLIENTID = "clientid";
    private static final String PROP_SECRET = "secret";
    private static final int PROP_ACCESS_TOKEN_VALIDITY_SECONDS = 1800;
    private static final int PROP_REFRESH_TOKEN_VALIDITY_SECONDS = 2700;

    @Value("${jwtToken.KeySecret}")
    private String jwtTokenKeySecret;

    @Value("${jwtToken.Alias}")
    private String jwtTokenAlias;

    @Value("${oauth.accessTokenValiditySeconds}")
    private int accessTokenValiditySeconds;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        System.out.println(jwtTokenKeySecret);
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("authService.jks"),
                jwtTokenKeySecret.toCharArray()
        );
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyFactory.getKeyPair(jwtTokenAlias));
        return converter;
    }
    
    @Bean
    public TokenStore tokenStore() {

        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        endpoints.tokenStore(tokenStore())
        		.accessTokenConverter(jwtAccessTokenConverter())
        		.reuseRefreshTokens(false)
                .tokenEnhancer(enhancerChain)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients	.inMemory()
                .withClient(PROP_CLIENTID)
                .scopes("read", "write")
                .authorities("ADMIN", "USER")
                .authorizedGrantTypes("password", "refresh_token")
                .secret(PROP_SECRET)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(PROP_REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    	super.configure(security);
    }
}