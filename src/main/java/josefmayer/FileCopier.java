package josefmayer;

/**
 * Created by Josef Mayer on 08.05.2017.
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;


public class FileCopier {

    public  void readDirs() throws Exception{

        LoggerContext ctx = CustomLoggerContext.createFileLogCtx();
        Logger logger = LogManager.getLogger();

        File inboxDirectory = new File("data/inbox");
        File outboxDirectory = new File("data/outbox");

        File logDirectory = new File("log");
        logDirectory.mkdir();

        File logFile = new File("log/logFile");
        FileOutputStream out = new FileOutputStream(logFile, true);

        outboxDirectory.mkdir();

        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(("***** System Information: ***** \n"));
        sb.append("os.arch: " + System.getProperty("os.arch") + "\n");
        sb.append("os.name: " + System.getProperty("os.name") + "\n");
        sb.append("os.version: " + System.getProperty("os.version") + "\n");
        sb.append("java.version: " + System.getProperty("java.version") + "\n\n\n");
        sb.append("*** Files Testing: *** \n");

        sb.append("file size in bytes\n");
        sb.append("read time in ns\n");
        sb.append("write time in ns\n");

        File[] files = inboxDirectory.listFiles();

        sb.append("\n** ByteBuffer ** \n");
        for (File source : files) {
            if (source.isFile()) {
                File dest = new File(
                        outboxDirectory.getPath()
                                + File.separator
                                + source.getName());

                copyFileByteBuf(source, dest, sb);
            }
        }

        sb.append("\n** ByteBufferDirect ** \n");
        for (File source : files) {
            if (source.isFile()) {
                File dest = new File(
                        outboxDirectory.getPath()
                                + File.separator
                                + source.getName());

                copyFileByteBufDir(source, dest, sb);
            }
        }

        sb.append("\n\n");
        System.out.println(sb);
        out.write(sb.toString().getBytes());

        logger.info(sb.toString());

    }


    private void copyFileByteBuf(File source, File dest, StringBuffer sb) throws IOException {
        long startTime, stopTime;

        sb.append("\n");
        sb.append("file name: " + source.getName() + "\n");
        sb.append("size: " + source.length() + "\n");

        FileChannel fcin = new FileInputStream(source).getChannel();
        FileChannel fcout = new FileOutputStream(dest).getChannel();

        ByteBuffer bytebuf= ByteBuffer.allocate((int)source.length());

        startTime = System.nanoTime();
        int bytesCount = 0;
        while ((bytesCount = fcin.read(bytebuf)) > 0) {
            stopTime = System.nanoTime();
            sb.append("read:  " + (stopTime - startTime) + "\n");

            bytebuf.flip();                 // flip the buffer which set the limit to current position, and position to 0.;

            startTime = System.nanoTime();
            fcout.write(bytebuf);           // Write data from ByteBuffer to file
            stopTime = System.nanoTime();
            sb.append("write: " + (stopTime - startTime) + "\n");

            bytebuf.clear();                // For the next read

            startTime = System.nanoTime();  // if loop runs more than once
        }

    }

    private void copyFileByteBufDir(File source, File dest, StringBuffer sb) throws IOException {
        long startTime, stopTime;

        sb.append("\n");
        sb.append("file name: " + source.getName() + "\n");
        sb.append("size: " + source.length() + "\n");

        FileChannel fcin = new FileInputStream(source).getChannel();
        FileChannel fcout = new FileOutputStream(dest).getChannel();

        ByteBuffer bytebuf= ByteBuffer.allocateDirect((int)source.length());

        startTime = System.nanoTime();
        int bytesCount = 0;
        while ((bytesCount = fcin.read(bytebuf)) > 0) {
            stopTime = System.nanoTime();
            sb.append("read:  " + (stopTime - startTime) + "\n");

            bytebuf.flip();                 // flip the buffer which set the limit to current position, and position to 0.;

            startTime = System.nanoTime();
            fcout.write(bytebuf);           // Write data from ByteBuffer to file
            stopTime = System.nanoTime();
            sb.append("write: " + (stopTime - startTime) + "\n");

            bytebuf.clear();                // For the next read

            startTime = System.nanoTime();  // if loop runs more than once
        }

    }

}
