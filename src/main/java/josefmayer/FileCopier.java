package josefmayer;

/**
 * Created by Josef Mayer on 08.05.2017.
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import josefmayer.util.FileInfo;
import josefmayer.util.IOInfo;
import josefmayer.util.OSInfo;
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

        OSInfo osInfo = new OSInfo(System.getProperty("os.arch"), System.getProperty("os.name"),
                System.getProperty("os.version"), System.getProperty("java.version"));
        IOInfo ioInfo = new IOInfo();
        ioInfo.setOSInfo(osInfo);

        File[] files = inboxDirectory.listFiles();

        for (File source : files) {
            if (source.isFile()) {
                File dest = new File(
                        outboxDirectory.getPath()
                                + File.separator
                                + source.getName());
                FileInfo fileInfo = new FileInfo();
                copyFileByteBuf(source, dest, fileInfo);

                ioInfo.addFileInfo(fileInfo);
            }
        }

        for (File source : files) {
            if (source.isFile()) {
                File dest = new File(
                        outboxDirectory.getPath()
                                + File.separator
                                + source.getName());
                FileInfo fileInfo = new FileInfo();
                copyFileByteBufDir(source, dest, fileInfo);
                ioInfo.addFileInfoDir(fileInfo);
            }
        }

        System.out.println(ioInfo);
        out.write(ioInfo.toString().getBytes());
        logger.info(ioInfo.toString());

    }


    private void copyFileByteBuf(File source, File dest, FileInfo fileInfo) throws IOException {
        long startTime, stopTime;

        fileInfo.setName(source.getName());
        fileInfo.setFileSize(source.length());

        FileChannel fcin = new FileInputStream(source).getChannel();
        FileChannel fcout = new FileOutputStream(dest).getChannel();

        ByteBuffer bytebuf= ByteBuffer.allocate((int)source.length());

        startTime = System.nanoTime();
        int bytesCount = 0;
        while ((bytesCount = fcin.read(bytebuf)) > 0) {
            stopTime = System.nanoTime();
            fileInfo.setReadTime(stopTime - startTime);

            bytebuf.flip();                 // flip the buffer which set the limit to current position, and position to 0.;

            startTime = System.nanoTime();
            fcout.write(bytebuf);           // Write data from ByteBuffer to file
            stopTime = System.nanoTime();
            fileInfo.setWriteTime(stopTime - startTime);

            bytebuf.clear();                // For the next read

            startTime = System.nanoTime();  // if loop runs more than once
        }

    }

    private void copyFileByteBufDir(File source, File dest, FileInfo fileInfo) throws IOException {
        long startTime, stopTime;

        fileInfo.setName(source.getName());
        fileInfo.setFileSize(source.length());

        FileChannel fcin = new FileInputStream(source).getChannel();
        FileChannel fcout = new FileOutputStream(dest).getChannel();

        ByteBuffer bytebuf= ByteBuffer.allocateDirect((int)source.length());

        startTime = System.nanoTime();
        int bytesCount = 0;
        while ((bytesCount = fcin.read(bytebuf)) > 0) {
            stopTime = System.nanoTime();
            fileInfo.setReadTime(stopTime - startTime);

            bytebuf.flip();                 // flip the buffer which set the limit to current position, and position to 0.;

            startTime = System.nanoTime();
            fcout.write(bytebuf);           // Write data from ByteBuffer to file
            stopTime = System.nanoTime();
            fileInfo.setWriteTime(stopTime - startTime);

            bytebuf.clear();                // For the next read

            startTime = System.nanoTime();  // if loop runs more than once
        }

    }

}
