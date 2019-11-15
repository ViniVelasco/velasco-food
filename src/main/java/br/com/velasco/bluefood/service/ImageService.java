package br.com.velasco.bluefood.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.velasco.bluefood.util.IOUtils;

@Service
public class ImageService {
	
	@Value("${bluefood.files.logotipo}")
	private String logotiposDir;
	
	@Value("${bluefood.files.categoria}")
	private String categoriasDir;
	
	@Value("${bluefood.files.comida}")
	private String comidasDir;
	
	public void uploadLogoTipo(MultipartFile multi, String fileName) {
		try {
			IOUtils.copy(multi.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public void uploadComida(MultipartFile multi, String fileName) {
		try {
			IOUtils.copy(multi.getInputStream(), fileName, comidasDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public byte[] getBytes(String type, String imgName) {
		try {
			String dir;
			if("comida".equals(type)) {
				dir = comidasDir;
			} else if("logotipo".equals(type)){
				dir = logotiposDir;
			} else if("categoria".equals(type)) {
				dir = categoriasDir;
			} else {
				throw new Exception(type + " não é um tipo de imagem válido");
			}
			return IOUtils.getByes(Paths.get(dir, imgName));
		} catch(Exception e) {
			throw new ApplicationServiceException(e);
		}
	}

}
