package com.moneyminder.moneyminderusers.webServer;

import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfig {

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addEngineValves(remoteIpValve());
        return factory;
    }

    private RemoteIpValve remoteIpValve() {
        RemoteIpValve valve = new RemoteIpValve();
        valve.setProtocolHeader("x-forwarded-proto");
        valve.setProtocolHeaderHttpsValue("https");
        return valve;
    }
}
