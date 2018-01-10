package net.idik.crepecake

import com.android.build.gradle.AppExtension
import net.idik.crepecake.transform.AspectTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class CrepeCakePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def android = project.extensions.getByType(AppExtension)
        def injectTransform = new AspectTransform(project)
        android.registerTransform(injectTransform)

        project.dependencies {
//            implementation 'net.idik.crepecake:api:0.0.3'
//            annotationProcessor 'net.idik.crepecake:compiler:0.0.3'
            implementation project.project(':api')
            annotationProcessor project.project(':compiler')
        }

    }


}
