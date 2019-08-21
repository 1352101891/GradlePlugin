package com.example.buildplugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CleanTestTask extends DefaultTask{
    CleanTestTask() {
        super()
    }
    @TaskAction
    def testClean(){
        System.out.println("==================")
        System.out.println("Test Clean Task")
        System.out.println("==================")
    }
}