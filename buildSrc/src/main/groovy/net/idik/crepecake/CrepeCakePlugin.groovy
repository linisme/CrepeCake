package net.idik.crepecake

import com.android.build.gradle.AppExtension
import net.idik.crepecake.transform.AspectTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrepeCakePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.afterEvaluate {
            def android = project.extensions.getByType(AppExtension)
            def properties = new Properties()
            properties.load(project.rootProject.file('local.properties').newDataInputStream())
            def injectTransform = new AspectTransform()
            injectTransform.androidPath = "${properties.getProperty("sdk.dir")}/platforms/${android.compileSdkVersion}/android.jar"
            android.registerTransform(injectTransform)
        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1)
    }
}
