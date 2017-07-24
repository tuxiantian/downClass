package com.smz.core;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Properties;

public class GetClassFile
{
	private static String PROJECT_CLASS_PATH = "";
	private static String OUT_PUT_DIR = "";
	private static String DEVELOP_TOOL= "";

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
			DEVELOP_TOOL=p.getProperty("DEVELOP_TOOL");
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
			String line;
			while ((line = br.readLine()) != null) {
				if ((line != null) && (line.trim().length() > 0)) {
					if ("eclipse".equals(DEVELOP_TOOL)) {
						process(line);
					}else {
						process_idea(line);
					}
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

        String directoryPath=formatFileName.substring(0, formatFileName.lastIndexOf("/"));
        File directory=Paths.get(PROJECT_CLASS_PATH,directoryPath).toFile();

        File[] listFiles = directory.listFiles((dir, name) -> {
            if (name.equals(tempFileName+".class")||name.startsWith(tempFileName+"$")||name.endsWith(".xml")) {
                return true;
            }
            return false;
        });

        copy(fileName, directoryPath, listFiles);

	}
	private static void process_idea(String fileName)
			throws Exception
	{
		String formatFileName= fileName.replaceAll("\\\\", "/");
        String directoryPath = null;
        if (formatFileName.indexOf("java/")!=-1){
        	directoryPath=formatFileName.substring(formatFileName.indexOf("java/")+"java/".length(), formatFileName.lastIndexOf("/"));
		}

        /**
		 * 处理内部类和匿名类
		 */
		final String tempFileName=formatFileName.substring(formatFileName.lastIndexOf("/")+1, formatFileName.lastIndexOf("."));

		File directory=Paths.get(PROJECT_CLASS_PATH,directoryPath).toFile();

		File[] listFiles = directory.listFiles((dir, name) -> {
            if (name.equals(tempFileName+".class")||name.startsWith(tempFileName+"$")||name.endsWith(".xml")) {
                return true;
            }
            return false;
        });

        copy(fileName, directoryPath, listFiles);
    }

    private static void copy(String fileName, String directoryPath, File[] listFiles) {
        if (listFiles==null||listFiles.length==0) {
            System.out.println("文件名《" + fileName + "》不存在！");
        }else {
            for (File sourceFile : listFiles) {
                String outPutFileName = Paths.get(OUT_PUT_DIR,directoryPath,sourceFile.getName()).toString();
                outPutFileName = outPutFileName.replaceAll("\\\\", "/");
                String outPutDir = outPutFileName.substring(0,outPutFileName.lastIndexOf("/"));
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
