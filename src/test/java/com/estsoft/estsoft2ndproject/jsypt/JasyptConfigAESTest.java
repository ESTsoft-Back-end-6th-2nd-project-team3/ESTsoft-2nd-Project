package com.estsoft.estsoft2ndproject.jsypt;

import static org.junit.jupiter.api.Assertions.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.jupiter.api.Test;

class JasyptConfigAESTest {
	@Test
	void stringEncryptor() {
		String password = "password";

		String encoded = jasyptEncoding(password);
		String decoded = jasyptDecoding(encoded);

		System.out.println(encoded);
		System.out.println(decoded);

		assertEquals(password, decoded);
	}

	public String jasyptEncoding(String value) {
		String key = "key";
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		pbeEnc.setPassword(key);
		pbeEnc.setIvGenerator(new RandomIvGenerator());
		pbeEnc.setSaltGenerator(new RandomSaltGenerator());
		return pbeEnc.encrypt(value);
	}

	public String jasyptDecoding(String encryptedValue) {
		String key = "key";
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
		pbeEnc.setPassword(key);
		pbeEnc.setIvGenerator(new RandomIvGenerator());
		pbeEnc.setSaltGenerator(new RandomSaltGenerator());
		return pbeEnc.decrypt(encryptedValue);
	}
}