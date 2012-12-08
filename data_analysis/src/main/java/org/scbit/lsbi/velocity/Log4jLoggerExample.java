package org.scbit.lsbi.velocity;

import java.io.StringWriter;
import java.util.logging.Logger;

import org.apache.log4j.BasicConfigurator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;

public class Log4jLoggerExample {

	public static String LOGGER_NAME = "Walter";
	
	public static void main(String[] args)throws Exception {
		
		/*
		 * configure log4j to log to console
		 */
		BasicConfigurator.configure();
		Logger log  = Logger.getLogger(LOGGER_NAME);
		log.info("Hello from Log4jLoggerExample -ready to start velocity");
		
		/*
		 * now create a new VelocityEngine instance, 
		 * and configure it to use the logger
		 */
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		ve.setProperty(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, LOGGER_NAME);
		ve.init();
		
		VelocityContext context = new VelocityContext();
		context.put("name", "Walter");
		context.put("job", "Coder");
		context.put("gender", Gender.MALE);
		StringWriter w = new StringWriter();
		String templateStr = "${name} is a ${job} !---is a $!{gender.Alias}";
		ve.evaluate(context, w, "Walter", templateStr);
		System.out.println(w);
		System.out.println(Gender.MALE.compareTo(Gender.FEMALE));
		log.info("this should follow the initialization output from velocity");
		
		
	}
	
}
