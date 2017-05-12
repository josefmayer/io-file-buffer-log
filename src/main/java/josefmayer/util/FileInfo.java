package josefmayer.util;

/**
 * Created by Josef Mayer on 11.05.2017.
 */
public class FileInfo {
        private String name;
        private long fileSize;
        private long readTime;
        private long writeTime;


        public void setName(String name){
            this.name = name;
        }

        public void setFileSize(long fileSize){
            this.fileSize = fileSize;
        }

        public void setReadTime (long readTime){
            this.readTime = readTime;
        }

        public void setWriteTime (long writeTime){
            this.writeTime = writeTime;
        }

        public String toString(){
            return ("\nfile name: " + name + "\nsize: " + fileSize + "\nread: " + readTime + "\nwrite: " + writeTime);
        }

}
