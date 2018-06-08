package org.carp.test.encrypt;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.carp.exception.CarpException;
import org.carp.security.Base64;
import org.carp.security.IPasswordDecryptor;

public class CustDecryptor implements IPasswordDecryptor {
	private static final String randomKey = ".988jaf.[af$#*^&#*^@)+/sdfn/AJoe10!!~";
	@Override
	public String decrypt(String ciphertext) throws CarpException {
		if(ciphertext == null || ciphertext.isEmpty()){
//			throw new CarpException("decrypt Failed. ciphertext is null.");
		}
		try{
			SecureRandom sr = new SecureRandom();
	        Cipher cipher = Cipher.getInstance("AES");
	        Key secureKey = key();
	        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
	        byte[] byte_content = Base64.getDecoder().decode(ciphertext);
	        byte[] byte_decode = cipher.doFinal(byte_content);
	        return new String(byte_decode,"utf-8");
			
		}catch(Exception ex){
			throw new CarpException("decrypt failed. Cause: "+ex.getMessage(),ex);
		}
	}
	
	private static Key key()throws CarpException{
		try{
			KeyGenerator _generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(randomKey.getBytes());
			_generator.init(128, secureRandom);
			return _generator.generateKey();
		}catch(Exception ex){
			throw new CarpException("Initialization key exception.",ex);
		}
	}
	
	public String encrypt(String content) throws CarpException{
		try{
			SecureRandom sr = new SecureRandom();
	        Key secureKey = key();
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
	        byte[] bt = cipher.doFinal(content.getBytes("utf-8"));
	        String strS = Base64.getEncoder().encodeToString(bt);
	        return strS;
			//return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes("utf-8")));
		}catch(Exception ex){
			throw new CarpException(ex);
		}
	}
}
