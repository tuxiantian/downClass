package com.smz.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Properties;

public class GetClassFile
{
	private static String PROJECT_CLASS_PATH = "";
	private static String OUT_PUT_DIR = "";

	static
	{
		Properties p = new Properties();
		try
		{
			p.load(new FileInputStream("config/sys.properties"));
			PROJECT_CLASS_PATH = p.getProperty("PROJECT_CLASS_PATH");
			PROJECT_CLASS_PATH = PROJECT_CLASS_PATH.replaceAll("\\\\", "/");
			OUT_PUT_DIR = p.getProperty("OUT_PUT_DIR");
			OUT_PUT_DIR = OUT_PUT_DIR.replaceAll("\\\\", "/");
			if (!PROJECT_CLASS_PATH.endsWith("/")) {
				PROJECT_CLASS_PATH += "/";
			}
			if (!OUT_PUT_DIR.endsWith("/")) {
				OUT_PUT_DIR += "/";
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		try
		{
			File outFile = new File(OUT_PUT_DIR);
			if (outFile.exists()) {
				deleteDir(outFile);
			}
			BufferedReader br = new BufferedReader(new FileReader("config/file.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if ((line != null) && (line.trim().length() > 0)) {
					process(line);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void process(String fileName)
			throws Exception
	{
		if (fileName.indexOf(".java") != -1)
		{
			String formatFileName="";
			if (fileName.indexOf("src/") != -1) {
				formatFileName = fileName.substring(fileName.indexOf("src/") + 4);
			}
			if (fileName.indexOf("src\\") != -1) {
				formatFileName = fileName.substring(fileName.indexOf("src\\") + 5);
			}
			formatFileName=formatFileName.replaceAll("\\\\", "/");
			/**
			 * 处理内部类和匿名类
			 */
			final String tempFileName=formatFileName.substring(formatFileName.lastIndexOf("/")+1, formatFileName.lastIndexOf(".java"));
			
			String directryPath=formatFileName.substring(0, formatFileName.lastIndexOf("/"));
			File directory=Paths.get(PROJECT_CLASS_PATH,directryPath).toFile();
			
			File[] listFiles = directory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.equals(tempFileName+".class")||name.startsWith(tempFileName+"$")) {
						return true;
					}
					return false;
				}
			});
			if (listFiles==null||listFiles.length==0) {
				System.out.println("文件名《" + fileName + "》不存在！");
			}else {
				for (File sourceFile : listFiles) {
					String outPutFileName =Paths.get(OUT_PUT_DIR,directryPath,sourceFile.getName()).toString();
					outPutFileName = outPutFileName.replaceAll("\\\\", "/");
					String outPutDir = outPutFileName.substring(0, 
							outPutFileName.lastIndexOf("/"));
					File outDir = new File(outPutDir);
					if (!outDir.exists()) {
						outDir.mkdirs();
					}
					File outFile = new File(outPutFileName);
					fileChannelCopy(sourceFile, outFile);
					System.out.println("处理文件<" + sourceFile.getAbsolutePath() + ">成功！");
				}
			}

		}
		else
		{
			System.out.println("文件名必须是java类型fileName=" + fileName);
		}
	}

	public static void fileChannelCopy(File s, File t)
	{
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try
		{
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();
			out = fo.getChannel();
			in.transferTo(0L, in.size(), out);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fi.close();
				in.close();
				fo.close();
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
