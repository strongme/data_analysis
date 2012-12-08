package org.scbit.lsbi.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class Example2 {

	public static void main(String[] args) {

		/*
		 * first ,we init the runtime engine
		 */

		try {
			Properties p =new Properties();
			p.setProperty("resource.loader", "file");
			p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			Velocity.init(p);
		} catch (Exception e) {
			System.out.println("Problem iniyializing Velocity : " + e);
		}

		/*
		 * Let's make a context and put data into it
		 */
		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");
		VelocityContext context2 = new VelocityContext(context);
		context2.put("content", "这是个链子");
		/*
		 * Let's render a template
		 */

		StringWriter w = new StringWriter();
		try {
			Velocity.mergeTemplate("src/main/java/template/example2.vm", "UTF-8", context2, w);
		} catch (Exception e) {
			System.out.println("Problem merging template: " + e);
		}
		System.out.println("template : " + w);

		/*
		 * Let's dynamically create out template and use the evaluate() method
		 * to render it
		 */
		String s = "We are using $project $name to render this.$content";
		w = new StringWriter();

		try {
			Velocity.evaluate(context2, w, "Walter", s);
		} catch (ParseErrorException pee) {
			/*
			 * thrown if something is wrong with the syntax of our template
			 * string
			 */
			System.out.println("ParseErrorException : " + pee);
		} catch (MethodInvocationException mee) {
			/*
			 * thrown if a method of a reference called by the template throws
			 * an exception. That won't happen here as we aren't calling any
			 * methods in this example, but we have to catch them anyway
			 */
			System.out.println("MethodInvocationException : " + mee);
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
		System.out.println(" string : "+w);

	}

}
