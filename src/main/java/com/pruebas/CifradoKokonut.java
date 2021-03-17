package com.pruebas;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;



public class CifradoKokonut 
{
	public static void main(String []args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		for(int i=0; i<1000; i++) {
			String informacion="Hola soy la informacion que se va a cifrar... ".concat(String.valueOf(RandomUtils.nextDouble()*1000));
			System.out.println("Informacion... "+ informacion);
			String textCifrado=cifraInformacion(informacion);
			System.out.println("Informacion cifrada... "+textCifrado);
			System.out.println("Informacion descifrada.. "+descrifraInformacion(textCifrado));
			System.out.println("\n\n\n");
		}
	}
	
	private static String cifraInformacion(String informacionClaro) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher cipher = crearCipher(1);
		
		byte[] encrypted = cipher.doFinal(informacionClaro.getBytes(StandardCharsets.UTF_8));
		String crypt = new String(Base64.encodeBase64(encrypted));
		
		return crypt;
	}
	
	private static String descrifraInformacion(String text) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException
	{
		Cipher cipher = crearCipher(2);
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(text.getBytes()));
		
		return new String(decrypted);
	}
	
	private static Cipher crearCipher(int type) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
	{
		String claveCifrado="2ZNQAP7V9OPQCGROONT2BJJY";
		String vectorCifrado="91 05 52 42 28 50 46 06 56 55 60 44 34 64 68 78";
		
		SimpleDateFormat formatoFecha=new SimpleDateFormat("yyyyMMdd");
		Date diaActual=new Date();
		claveCifrado=claveCifrado.concat(formatoFecha.format(diaActual));
		System.out.println("Clave para cifrar... "+claveCifrado);
		byte[] ky = StringUtils.leftPad(claveCifrado, 32, "0").getBytes(StandardCharsets.UTF_8);
		SecretKeySpec skeySpec = new SecretKeySpec(ky, "AES");
		IvParameterSpec iv = new IvParameterSpec(arregloBytes(vectorCifrado), 0, 16);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(type, skeySpec, iv);
		return cipher;
	}
	
	private static byte[] arregloBytes(String in)
	{
		in = StringUtils.leftPad(in.replaceAll(" ", ""), 32, "0");
		int len = in.length() / 2;
		byte[] out = new byte[len];
		for (int i = 0; i < len; i++) {
			out[i] = ((byte) Integer.parseInt(in.substring(i * 2, i * 2 + 2), 16));
		}
		
		return out;
	}
}

