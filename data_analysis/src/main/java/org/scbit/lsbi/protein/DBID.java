package org.scbit.lsbi.protein;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scbit.lsbi.protein.pojo.IdmapDbs;
import org.scbit.lsbi.protein.pojo.IdmapPart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author 1
 * 
 */
public class DBID {

	private static ApplicationContext context;
	private  HibernateTemplate hibernateTemplate;

	@BeforeClass
	public static void initAll() {
		context = new FileSystemXmlApplicationContext(DBID.class
				.getClassLoader().getResource("").toString().substring(6)
				+ "/applicationContextDatabase.xml");
	}

	@Before
	public void initTemplate() {
		hibernateTemplate = (HibernateTemplate) context
				.getBean("hibernateTemplate");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testBeanfactory() {
		
		List ides = hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				  SQLQuery sqlQuery = session.createSQLQuery("select finalId from (select m.id finalId,m.database_name, c count from idmap_dbs m,(select count(*) c,d.id ids from idmap_part t,idmap_dbs d where t.dbs_id = d.id group by d.id) where m.id = ids and c = 11488024 order by c asc)");
				return sqlQuery.list();
			}
		});
		
		for(int j=0;j<ides.size();j++) {
			long now1 = System.currentTimeMillis();
			IdmapDbs idmapDbs = (IdmapDbs) hibernateTemplate.find("from IdmapDbs i where i.id = "+ides.get(j).toString()+"").get(0);
			String dbsId = idmapDbs.getId().toString();
			List result = hibernateTemplate
					.find("select count(*) from IdmapPart i where i.dbsId = "
							+ dbsId);
			String dbName = idmapDbs.getDatabaseName();
			long allCount = (Long) result.get(0);
			System.out.println(Thread.currentThread().getName()+": 共有库id 为 " + dbsId + "--> " + dbName + "  的数据:"
					+ allCount + " 条");
			long numToRead = allCount / 50000 + 1;
			for(int i=0;i<numToRead;i++) {
				List<String> list = fetch(i*50000, 50000, dbsId);
				System.out.println(Thread.currentThread().getName()+": 读出 " + list.size() + "  条数据到 "+allCount+"_"+dbName+"_data.txt");
				writeIntoFile(list, dbName,allCount);
			}
			long now2 = System.currentTimeMillis();
			System.out.println("此次文件读写耗时: "+(now2-now1)/1000+"s");
			
		}
		
		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List fetch(int start, int count, String dbsId) {
		final int finalStart = start;
		final int finalCount = count;
		final String dbsIdFinal = dbsId;
		List<IdmapPart> list = hibernateTemplate
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session
								.createQuery("select i.id from IdmapPart i where i.dbsId = "
										+ dbsIdFinal);
						q.setFirstResult(finalStart);
						q.setMaxResults(finalCount);
						return q.list();
					}
				});
		return list;
	}

	/**
	 * 将list中的数据写入到文件中去
	 * 
	 * @param list
	 */
	public  void writeIntoFile(List<String> list, String dbsName,long size) {
		String filePath = DBID.class.getClassLoader().getResource("")
				.toString().substring(6);
		File file = new File(filePath +""+size+"_"+ dbsName + "_data.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rwd");
			randomAccessFile.seek(randomAccessFile.length());
			for (String id : list) {
				randomAccessFile.write((id + "\n").getBytes());
			}
		} catch (FileNotFoundException e) {
			System.out.println("原文件未找到，请确认文件存在");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class FileWriter {
		private String dbsName;
		
		public FileWriter(String dbsName) {
			this.dbsName = dbsName;
		}
		
		public void writeIntoFile(List<String> list) {
			String filePath = DBID.class.getClassLoader().getResource("")
					.toString().substring(6);
			File file = new File(filePath + dbsName + "_data.txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			RandomAccessFile randomAccessFile = null;
			//同步代码快，防止多线程同时打开同一个文件进行写入
			synchronized (this) {
				try {
					System.out.println("写入文件");
					randomAccessFile = new RandomAccessFile(file, "rwd");
					randomAccessFile.seek(randomAccessFile.length());
					for (String id : list) {
						randomAccessFile.write((id + "\n").getBytes());
					}
				} catch (FileNotFoundException e) {
					System.out.println("原文件未找到，请确认文件存在");
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (randomAccessFile != null) {
						try {
							randomAccessFile.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}//同步代码快完成 
		}
	}
	
	class SingleDataReader extends Thread {
		private int start;
		private  int count;
		private String dbsId;
		private String dbName;
		private FileWriter fileWriter;
		private  HibernateTemplate hibernateTemplate;
		
		public SingleDataReader(int start, int count, String dbsId,String dbName,FileWriter fileWriter,HibernateTemplate hibernateTemplate) {
			super(dbsId+" Threading: ");
			this.start = start;
			this.count = count;
			this.dbsId = dbsId;
			this.dbName = dbName;
			this.fileWriter = fileWriter;
			this.hibernateTemplate = hibernateTemplate;
		}
		
		@Override
		public void run() {
			List<String> list = fetch(start, count, dbsId);
			System.out.println(Thread.currentThread().getName()+": 读出 " + list.size() + "  条数据到 "+dbName+"_data.txt");
			fileWriter.writeIntoFile(list);
		}
		
		public List fetch(int start, int count, String dbsId) {
			final int finalStart = start;
			final int finalCount = count;
			final String dbsIdFinal = dbsId;
			List<IdmapPart> list = hibernateTemplate
					.executeFind(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query q = session
									.createQuery("select i.id from IdmapPart i where i.dbsId = "
											+ dbsIdFinal);
							q.setFirstResult(finalStart);
							q.setMaxResults(finalCount);
							return q.list();
						}
					});
			return list;
		}
	}
	
	class DataReader extends Thread {
		
		private IdmapDbs idmapDbs;
		
		public DataReader(IdmapDbs idmapDbs) {
			super(idmapDbs.getDatabaseName()+"Threading");
			this.idmapDbs = idmapDbs;
		}
		@Override
		public void run() {
			String dbsId = idmapDbs.getId().toString();
			List result = hibernateTemplate
					.find("select count(*) from IdmapPart i where i.dbsId = "
							+ dbsId);
			String dbName = idmapDbs.getDatabaseName();
			FileWriter fileWriter = new FileWriter(dbName);
			long allCount = (Long) result.get(0);
			System.out.println(Thread.currentThread().getName()+": 共有库id 为 " + dbsId + "--> " + dbName + "  的数据:"
					+ allCount + " 条");
			long numToRead = allCount / 50000 + 1;
			for(int i=0;i<numToRead;i++) {
				List<String> list = fetch(i*50000, 50000, dbsId);
				System.out.println(list);
				System.out.println(Thread.currentThread().getName()+": 读出 " + list.size() + "  条数据到 "+dbName+"_data.txt");
				//writeIntoFile(list, dbName);
			}
		}
	}

}
