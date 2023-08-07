package com.momentum.investments.momentformgeneratorservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;

@Configuration
public class AtmozConfiguration {

    private final Integer port;
    private final String host;
    private final String password;
    private final String username;

    public AtmozConfiguration(@Value("${file-manager.atmoz-sftp.port}") Integer port,
                              @Value("${file-manager.atmoz-sftp.host}") String host,
                              @Value("${file-manager.atmoz-sftp.host}") String password,
                              @Value("${file-manager.atmoz-sftp.host}") String username) {
        this.port = port;
        this.host = host;
        this.password = password;
        this.username = username;
    }

    @Bean
    public DefaultSftpSessionFactory getSftpSession() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setAllowUnknownKeys(true);
        factory.setPassword(password);
        factory.setUser(username);
        return  factory;
    }
}
