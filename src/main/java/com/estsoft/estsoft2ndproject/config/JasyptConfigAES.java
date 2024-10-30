package com.estsoft.estsoft2ndproject.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptConfigAES {
	@Bean("jasyptEncryptorAES")
	public StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(System.getenv("JASYPTKEY"));     // 지정한 암호화 키
		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");    // 알고리즘
		config.setKeyObtentionIterations("1000");    // 반복할 해싱 회수
		config.setPoolSize("1");    // 인스턴스 pool
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");    // salt 생성 클래스
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");    // 인코딩 방식
		encryptor.setConfig(config);
		return encryptor;
	}
}
