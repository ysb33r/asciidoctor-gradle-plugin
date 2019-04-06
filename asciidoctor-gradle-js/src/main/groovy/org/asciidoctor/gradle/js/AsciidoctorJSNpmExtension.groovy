package org.asciidoctor.gradle.js

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension

/** An extension to configure Npm.
 *
 * @since 3.0
 */
@CompileStatic
class AsciidoctorJSNpmExtension extends NpmExtension {
    public final static String NAME = 'asciidoctorNpm'

    AsciidoctorJSNpmExtension(Project project) {
        super(project)
    }

    AsciidoctorJSNpmExtension(Task task) {
        super(task, NAME)
    }

    @Override
    protected String getNodeJsExtensionName() {
        AsciidoctorJSNodeExtension.NAME
    }

}
