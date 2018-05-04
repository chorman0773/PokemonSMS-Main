package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Base64Images {

	private Base64Images() {
		// TODO Auto-generated constructor stub
	}

	public static final String img2b64(BufferedImage i) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			ImageIO.write(i, "png", buffer);
			return new String(Base64.getEncoder().encode(buffer.toByteArray()),StandardCharsets.UTF_8);
		}catch(IOException e) {
			throw new RuntimeException("Problem occured with writing to a byte buffer",e);
		}
	}
	public static final BufferedImage b642img(String base64){
		ByteArrayInputStream array = new ByteArrayInputStream(Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8)));
		try {
			return ImageIO.read(array);
		} catch (IOException e) {
			throw new RuntimeException("Problem occurred with reading from base64 string",e);
		}
	}

}
