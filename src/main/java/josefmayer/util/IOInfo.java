package josefmayer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josef Mayer on 11.05.2017.
 */
public class IOInfo {
    public IOInfo(){
        fileInfoList = new ArrayList<FileInfo>();
        fileInfoListDir = new ArrayList<FileInfo>();
    }

    private OSInfo osInfo;
    private List<FileInfo> fileInfoList;
    private List<FileInfo> fileInfoListDir;


    public void setOSInfo(OSInfo osInfo){
        this.osInfo = osInfo;
    }

    public void addFileInfo(FileInfo fileInfo){
        fileInfoList.add(fileInfo);
    };

    public void addFileInfoDir(FileInfo fileInfo){
        fileInfoListDir.add(fileInfo);
    };

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(osInfo.toString());

        sb.append("file size in bytes\n");
        sb.append("read time in ns\n");
        sb.append("write time in ns\n\n");

        sb.append("** ByteBuffer **" + "\n");

        for (FileInfo fileInfo : fileInfoList){
           sb.append(fileInfo.toString() + "\n");
        }

        sb.append("\n** ByteBuffer Direct **" + "\n");

        for (FileInfo fileInfo : fileInfoListDir){
            sb.append(fileInfo.toString() + "\n");
        }
        return sb.toString();
    }
}
