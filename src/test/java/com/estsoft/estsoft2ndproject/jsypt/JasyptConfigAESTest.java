package com.estsoft.estsoft2ndproject.jsypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.jupiter.api.Test;

class JasyptConfigAESTest {
	@Test
	void stringEncryptor() {
		String password = "pfygd5ehG56BqE86zpue";

		System.out.println(jasyptEncoding(password));
	}

	public String jasyptEncoding(String value) {
		String key = "Q1d7M3bP5y6L4nW";
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		pbeEnc.setPassword(key);
		pbeEnc.setIvGenerator(new RandomIvGenerator());
		pbeEnc.setSaltGenerator(new RandomSaltGenerator());
		return pbeEnc.encrypt(value);
	}
}