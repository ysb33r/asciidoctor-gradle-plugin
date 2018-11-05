/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asciidoctor.gradle.jvm.slides.internal

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.ysb33r.grolifant.api.OperatingSystem
import spock.lang.Shared
import spock.lang.Specification

class FunctionalSpecification extends Specification {
    static
    final String TEST_PROJECTS_DIR = System.getProperty('TEST_PROJECTS_DIR') ?: './asciidoctor-gradle-jvm-slides/src/intTest/projects'
    static
    final String TEST_REPO_DIR = System.getProperty('OFFLINE_REPO') ?: './testfixtures/offline-repo/build/repo'
    static
    final OperatingSystem OS = OperatingSystem.current()

    @Rule
    final TemporaryFolder testProjectDir = new TemporaryFolder()

    @Shared
    List<File> pluginClasspath

    def setupSpec() {
        def pluginClasspathResource = getClass().classLoader.getResource('plugin-classpath.txt')
        if (pluginClasspathResource == null) {
            pluginClasspathResource = new File('./asciidoctor-gradle-jvm-slides/build/createClasspathManifest/plugin-classpath.txt')
        }
        if (pluginClasspathResource == null) {
            throw new IllegalStateException('Did not find plugin classpath resource, run `intTestClasses` build task.')
        }

        pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }
    }

    GradleRunner getGradleRunner(List<String> taskNames) {
        GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(taskNames)
            .withPluginClasspath(pluginClasspath)
            .forwardOutput()
            .withDebug(true)
    }

    @SuppressWarnings(['FactoryMethodName','BuilderMethodWithSideEffects'])
    void createTestProject(String docGroup ) {
        FileUtils.copyDirectory(new File(TEST_PROJECTS_DIR, docGroup), testProjectDir.root)
    }

    String getOfflineRepositories() {
        File repo = new File(TEST_REPO_DIR, 'repositories.gradle')
        if (!repo.exists()) {
            throw new FileNotFoundException("${repo} not found. Run ':testfixture-offline-repo:buildOfflineRepositories' build task")
        }

        if(OS.windows) {
            "apply from: /${repo.absolutePath}/"
        } else {
            "apply from: '${repo.absolutePath}'"
        }
    }
}