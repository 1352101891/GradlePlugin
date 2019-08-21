package com.example.buildplugin

import com.example.buildplugin.task.CleanTestTask
import com.example.buildplugin.task.ParseTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle

public class EgovaPlugin implements Plugin<Project> {
    Project project
    Project rootProject
    Set<String> subModuleNames
    String specialFolderPath
    SettingHelper settingHelper

    @Override
    void apply(Project project) {
        this.project = project
        rootProject = project.rootProject
        subModuleNames=filterLibNames()
        settingHelper=new SettingHelper(rootProject)
        specialFolderPath=checkSpFolder(rootProject,settingHelper.specialFolder())
        setListener()
    }

    void setListener(){
        //设置模块间依赖关系
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {
                System.out.println("beforeEvaluate模块名称："+project.name)
            }

            @Override
            void afterEvaluate(Project project, ProjectState projectState) {
                System.out.println("afterEvaluate模块名称："+project.name+",输出目录是:"+project.buildDir)
                if (project == rootProject) {
                    return
                }

                if(isAppProject(project)){
                    System.out.println("afterEvaluate模块名称：这是AppModule")
                    subModuleNames.each({
                        Project p = rootProject.project(":$it")
                        project.dependencies.add("compile", p)
                    })
                    addNewTask(project,"CleanTest",CleanTestTask,"preBuild")
                    addNewTask(project,"ParseTask", ParseTask,'preBuild')
                }else if (isBaseLib(project)){
                    System.out.println("afterEvaluate模块名称：isBaseLib")
                    project.android {
                        defaultConfig {}
                    }
                }else if(isLibProject(project)){
                    System.out.println("afterEvaluate模块名称：isLibProject")
                    project.dependencies.add("provided", getBaseLib())
                }
            }
        })
        ProjectEvaluationListener listener=new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {}
            @Override
            void afterEvaluate(Project project, ProjectState projectState) {
                if (project==rootProject){
                    return
                }

                replaceStr(project)

                project.android {
                    flavorDimensions "replaceStr", "specialFolder"
                    productFlavors {
                        ss {
                            dimension "replaceStr"
                        }
                        pp {
                            dimension "specialFolder"
                        }
                    }
                    if (isLibProject(project)) {
                        // 强制要求子module执行ssPpRelease打包配置
                        defaultPublishConfig isDebugMode(project) ? "ssPpDebug" : "ssPpRelease"
                    }
                }

                boolean needUpdateSpFolder
                File targetSpDir
                if (specialFolderPath != null) {
                    targetSpDir = new File(specialFolderPath, relativePath(project))
                    needUpdateSpFolder = targetSpDir.exists()
                }
                if(needUpdateSpFolder){
                    println("正在处理特殊目录："+targetSpDir)
                }

                if (needUpdateSpFolder){
                    File replaceSrcDir=new File(targetSpDir,'src')
                    Set<String> excludeSet = getExcludeFiles(replaceSrcDir)
                    project.android {
                        sourceSets {
                            pp {
                                manifest.srcFile new File(targetSpDir, 'AndroidManifest.xml')
                                java.srcDirs = [new File(targetSpDir, 'src')]
                                resources.srcDirs = [replaceSrcDir]
                                aidl.srcDirs = [replaceSrcDir]
                                renderscript.srcDirs = [replaceSrcDir]
                                res.srcDirs = [new File(targetSpDir, 'res')]
                                assets.srcDirs = [new File(targetSpDir, 'assets')]
                            }
                            main{
                                java{
                                    srcDirs = ['src']
                                    exclude excludeSet
                                }
                            }
                        }
                    }
                }
            }
        }
        //特殊目录替换
        gradle.addProjectEvaluationListener(listener)
    }

    //将自定义的task添加到depsTask依赖下，每次运行到depsTask会前置执行task
    static void addNewTask(Project p,String taskName,Class task,String depsTask){
        Task depTask=p.tasks.findByName(depsTask)
        p.tasks.create(taskName,task,new Action<Task>() {
            @Override
            void execute(Task t) {
                if (t != null&& depTask!=null) {
                    depTask.dependsOn(t)
                }
            }
        })
    }


    //解析setting.propertied文件替换资源文件中字符串
    void replaceStr(Project p){
        def variants = isAppProject(p) ? p.android.applicationVariants : p.android.libraryVariants
        variants.all { variant ->
            hookReplaceStr(p, variant)
        }
    }
    void hookReplaceStr(Project p, Object variant) {
        if (settingHelper.needReplace()) {
            Task mergeTask = p.tasks.findByName("merge${variant.name.capitalize()}Resources")
            if (mergeTask != null) {
                mergeTask.outputs.upToDateWhen { false }
                mergeTask.doLast {
                    mergeTask.outputs.files.each {
                        if (it.absolutePath.contains("incremental")) {
                            doReplace(new File(it, "merged.dir/values/values.xml"))

                        }
                        if (it.absolutePath.contains("merged")) {
                            doReplace(new File(it, "values/values.xml"))
                            p.fileTree(new File(it, "layout")).each { layoutFile ->
                                doReplace(layoutFile)
                            }
                        }
                    }
                }
            }
        }
    }

    void doReplace(File file) {
        if (file.exists()) {
            def lines = file.readLines("utf-8")
            boolean fileChanged = false

            lines.eachWithIndex { line, index ->
                settingHelper.getKeyValue().each { key, value ->
                    if (line.contains(key)) {
                        line = line.replace(key, value)
                        fileChanged = true
                    }
                }
                lines.set(index, line)
            }

            if (fileChanged) {
                file.write(lines.join(System.properties.'line.separator'), "utf-8")
            }
        }
    }


    static boolean isAppProject(Project project) {
        return project.hasProperty('android') && project.android.hasProperty('applicationVariants')
    }

    static boolean isLibProject(Project project) {
        return project.hasProperty('android') && project.android.hasProperty('libraryVariants')
    }

    static boolean isBaseLib(Project project) {
        return project.name == Constant.BASE_LIBRARY
    }

    public static boolean isDebugMode(Project project) {
        return project.gradle.startParameter.taskNames.any {
            return it.toLowerCase().contains("debug")
        }
    }

    Project getBaseLib(){
        for (Project project:rootProject.subprojects){
            if (isBaseLib(project)){
                return project
            }
        }
        return null
    }

    Gradle getGradle() {
        return rootProject.getGradle()
    }

    Set<String> filterLibNames() {
        return rootProject.subprojects.findAll {
            it.buildDir.absolutePath.contains("plugin") ||
                    it.buildDir.absolutePath.contains("baselibrary")
        }.collect {
            it.name
        }.toSet()
    }

    static String relativePath(Project module) {
        String path = module.projectDir.absolutePath.substring(module.rootProject.projectDir.absolutePath.length())
        return path
    }

    static File checkSpFolder(Project project, String path) {
        File spBaseFolder = resolvePath(project, path)
        if (spBaseFolder == null) {
            throw new IllegalArgumentException("特殊目录【${path}】不存在")
        }
        return spBaseFolder
    }

    static File resolvePath(Project project, String path) {
        File file = new File(path)
        if (file.exists() && !file.isFile()) {
            return file
        }

        file = project.rootProject.file("${Constant.UPDATE}/${path}")
        if (file.exists() && !file.isFile()) {
            return file
        }

        return null
    }

    private Set<String> getExcludeFiles(File specialFile) {
        Set<String> excludeSet = new HashSet<>()
        if (specialFile != null) {
            int splitIndex = specialFile.absolutePath.length()
            rootProject.files(specialFile).getAsFileTree().filter { File file ->
                excludeSet.add(file.absolutePath.substring(splitIndex))
                return true
            }.getFiles()
        }
        return excludeSet
    }
}