package josefmayer.util;

/**
 * Created by Josef Mayer on 11.05.2017.
 */
public class OSInfo {
    public OSInfo(String osArch, String osName, String osVersion, String javaVersion){
        this.osArch = osArch;
        this.osName = osName;
        this.osVersion = osVersion;
        this.javaVersion = javaVersion;
    }

    private String osArch;
    private String osName;
    private String osVersion;
    private String javaVersion;

    public String toString(){
        return ("\n***** System Information: *****" + "\nos.arch: " + osArch + "\nos.name: " + osName +
            "\nos.version: " + osVersion + "\njava.version: " + javaVersion + "\n\n");
    }


}
