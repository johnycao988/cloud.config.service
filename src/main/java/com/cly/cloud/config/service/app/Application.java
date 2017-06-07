package com.cly.cloud.config.service.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController; 

@SpringBootApplication
@RestController
public class Application {

	private static Logger logger = Logger.getGlobal();

	private static final String AUTH_CODE = "CLOUD.CONFIG.SERVICE.AUTH.CODE";

	@RequestMapping("/")
	public String version() {
		return "Cloud Config Service V1.0, released on June 2017!";
	}

	@RequestMapping("/info")
	public String info() {
		return version();
	}

	@RequestMapping("/refresh")
	public String refreshApp() {
		
		return "Refreshed.";
	}

	private String getAuthCode() {

 		String authCode=System.getProperty(AUTH_CODE, null);

		if (authCode == null) {
			logger.warning("Property:[" + AUTH_CODE + "] of Config Service is not set.");
		} else
			authCode = authCode.trim();

		return authCode;

	}

	@RequestMapping("/version")
	public String ver() {
		return version();
	}

	@RequestMapping("/health")
	public String health() {
		return "Health check ok";
	}

	
	@RequestMapping(value="/GetConfigFile",method = RequestMethod.POST)
	public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String reqAuthCode = request.getParameter("AUTH_CODE");

		if (reqAuthCode != null)
			reqAuthCode = reqAuthCode.trim();

		String configFile = request.getParameter("CONFIG_FILE_NAME");

		logger.info("A request from:" + request.getRemoteAddr() + " to get file:" + configFile);

		String authCode=getAuthCode();
		
		if (authCode != null && !authCode.equals(reqAuthCode)) {
			throw new IOException("Invalide Auth code:" + reqAuthCode);
		}

		readConfigFile(configFile, response);

	}

	private void readConfigFile(String configFile, HttpServletResponse response) throws IOException {

		try {

			try (FileInputStream fileInput = new FileInputStream(configFile);
					OutputStream out = response.getOutputStream()) {

				byte[] buffer = new byte[4098];

				int byteread = 0;

				while ((byteread = fileInput.read(buffer)) != -1) {

					out.write(buffer, 0, byteread);

				}
			}

		} catch (IOException ie) {
			logger.warning(ie.getMessage());
			throw ie;
		}
	}

	public static void main(String[] args) { 
		SpringApplication.run(Application.class, args);

	}

}
