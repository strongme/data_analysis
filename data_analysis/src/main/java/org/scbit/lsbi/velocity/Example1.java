package org.scbit.lsbi.velocity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class Example1 {
	
	public Example1(String templateFile) {
		
		try {
			
			/*
			 * setup
			 */
			Velocity.init(Example1.class.getResource("velocity.properties").toString().substring(6));
			
			/*
			 * Make a context object and populate with the data.This 
			 * is where the Velocity engine gets the data to resolve the
			 * reference (x. $list) in the template
			 */
			VelocityContext context =  new VelocityContext();
			context.put("list", getNames());
			
			Template template = null;
			
			try {
				template = Velocity.getTemplate(templateFile);
				
			}catch (ResourceNotFoundException e1) {
				System.out.println("Example : error : cannot find template "+templateFile);
			}catch (ParseErrorException e2) {
				System.out.println("Example : Syntax error in template "+templateFile+" : "+e2);
			}
			
			/*
			 * Now have the template engine process your template using the data placed
			 * into the context. Think of it as a 'Merge' of the template and the data to produce
			 *  the output stream
			 */
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
			
			//Velocity.mergeTemplate(templateFile, context, writer);
			
			
			if(template !=null) {
				template.merge(context, writer);
			}
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	public  ArrayList getNames() {
		ArrayList<String>  list = new ArrayList<String>();
		
		list.add("Jack Black");
		list.add("Smith Rose");
		list.add("Roland Benth");
		
		return list;
	}
	
	/**
	 * 关于file加载和class加载
	 * file加载：是以与src上级目录开始搜索路径
	 * class加载：是classpath的跟路径开始搜索
	 * 
	 * 
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		Example1 t = new Example1("template/example.vm");
	}
	
	

}
