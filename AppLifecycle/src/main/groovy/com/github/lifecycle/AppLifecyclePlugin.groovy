import com.android.build.gradle.AppExtension
import com.github.lifecycle.AppLifecycleTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppLifecyclePlugin  implements Plugin<Project> {
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new AppLifecycleTransform(project))
    }
}