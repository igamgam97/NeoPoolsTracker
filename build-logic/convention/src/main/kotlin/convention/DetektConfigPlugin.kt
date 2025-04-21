import convention.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.neo.gradle.DependencyHandlerUtils.detektPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

/**
 * Basic detekt configuration.
 */
class DetektConfigPlugin : Plugin<Project> {
    companion object {
        private val excludeFiles: List<String> = listOf("**/build/**", "**/generated/**")
        private val includeFiles: List<String> = listOf("**/*.kts", "**/*.kt")
    }

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.gitlab.arturbosch.detekt")
            configureDetektPlugin()
            configureDetektTasks()
            configureDetektBaselineTasks()
            addDependencies()
        }
    }

    private fun Project.configureDetektPlugin() {
        configure<DetektExtension> {
            autoCorrect = false
            parallel = true
            buildUponDefaultConfig = true
            baseline = file("detekt-baseline.xml")
            config.from(rootProject.file("tools/lint/detekt/config.yml"))
            ignoredBuildTypes = listOf("release")
        }
    }

    private fun Project.configureDetektTasks() {
        tasks.withType<Detekt>().configureEach {
            setSource(files(project.projectDir))
            include(includeFiles)
            exclude(excludeFiles)

            reports {
                html.required.set(true)
                html.outputLocation.set(file("build/reports/detekt/results.html"))
                xml.required.set(false)
                sarif.required.set(false)
                txt.required.set(false)
                md.required.set(false)
            }
        }
    }

    private fun Project.configureDetektBaselineTasks() {
        tasks.withType<DetektCreateBaselineTask>().configureEach {
            include(includeFiles)
            exclude(excludeFiles)
        }
    }

    private fun Project.addDependencies() {
        dependencies {
            detektPlugins(libs.findLibrary("detekt.formatting").get())
            detektPlugins(libs.findLibrary("android.detekt.compose").get())
        }
    }
}