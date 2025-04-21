package io.neo.gradle

import groovy.lang.Closure
import org.gradle.api.artifacts.Dependency
import org.gradle.kotlin.dsl.DependencyHandlerScope

@Suppress("unused")
object DependencyHandlerUtils {
    fun DependencyHandlerScope.implementation(
        dependencyNotation: Any,
    ): Dependency? {
        return add("implementation", dependencyNotation)
    }

    fun DependencyHandlerScope.implementation(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("implementation", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.lintChecks(
        dependencyNotation: Any,
    ): Dependency? {
        return add("lintChecks", dependencyNotation)
    }

    fun DependencyHandlerScope.lintChecks(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("lintChecks", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.testImplementation(
        dependencyNotation: Any,
    ): Dependency? {
        return add("testImplementation", dependencyNotation)
    }

    fun DependencyHandlerScope.testImplementation(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("testImplementation", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.androidTestImplementation(
        dependencyNotation: Any,
    ): Dependency? {
        return add("androidTestImplementation", dependencyNotation)
    }

    fun DependencyHandlerScope.androidTestImplementation(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("androidTestImplementation", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.kapt(
        dependencyNotation: Any,
    ): Dependency? {
        return add("kapt", dependencyNotation)
    }

    fun DependencyHandlerScope.kapt(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("kapt", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.kaptAndroidTest(
        dependencyNotation: Any,
    ): Dependency? {
        return add("kaptAndroidTest", dependencyNotation)
    }

    fun DependencyHandlerScope.kaptAndroidTest(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("kaptAndroidTest", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.ksp(
        dependencyNotation: Any,
    ): Dependency? {
        return add("ksp", dependencyNotation)
    }

    fun DependencyHandlerScope.ksp(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("ksp", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.kspDebug(
        dependencyNotation: Any,
    ): Dependency? {
        return add("kspDebug", dependencyNotation)
    }

    fun DependencyHandlerScope.kspDebug(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("kspDebug", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.detekt(
        dependencyNotation: Any,
    ): Dependency? {
        return add("detekt", dependencyNotation)
    }

    fun DependencyHandlerScope.detekt(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("detekt", dependencyNotation, configureClosure)
    }

    fun DependencyHandlerScope.detektPlugins(
        dependencyNotation: Any,
    ): Dependency? {
        return add("detektPlugins", dependencyNotation)
    }

    fun DependencyHandlerScope.detektPlugins(
        dependencyNotation: Any,
        configureClosure: Closure<Any>,
    ): Dependency? {
        return add("detektPlugins", dependencyNotation, configureClosure)
    }
}