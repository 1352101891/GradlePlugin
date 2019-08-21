package com.example.buildplugin
import org.gradle.api.Project

public class SettingHelper {

    HashMap<String,String> keyValue
    Project rootProject

    SettingHelper(Project rootProject) {
        this.rootProject = rootProject
        keyValue=new HashMap<>()
        parse()
    }

    void parse(){
        System.out.println("==================")
        System.out.println("doing parse")
        System.out.println("=================="+rootProject.projectDir.absolutePath)
        Properties props = new Properties()
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(
                rootProject.projectDir.absolutePath+File.separator+"setting.properties"))
        props.load(new InputStreamReader(inStream))
        println props.getProperty(Constant.SPECIAL_FOLDER_KEY)
        Enumeration e = props.keys()
        while (e.hasMoreElements())
        {
            def key = e.nextElement()
            def val = props.get(key)
            keyValue.put(key,val)
        }
    }

    public String specialFolder(){
        return keyValue.get(Constant.SPECIAL_FOLDER_KEY)
    }

    public String appName(){
        return keyValue.get(Constant.APP_NAME_KEY)
    }

    public String needReplace(){
        return keyValue!=null && keyValue.size()>0
    }
}
