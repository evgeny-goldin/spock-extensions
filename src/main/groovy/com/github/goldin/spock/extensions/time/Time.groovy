package com.github.goldin.spock.extensions.time

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import org.spockframework.runtime.extension.ExtensionAnnotation


/**
 * Extension allowing to time spec/method run-time and fail execution
 * if it's less than minimal or more than maximal amount of milliseconds.
 *
 * Extended version of @Time extension described at
 * http://ldaley.com/post/971946675/annotation-driven-extensions-with-spock?d21dafe8?986aa398
 */

@Retention( RetentionPolicy.RUNTIME )
@Target([ ElementType.TYPE, ElementType.METHOD ])
@ExtensionAnnotation( TimeExtension )
@interface Time {
    int min() default 0
    int max() default 0x7fffffff // Integer.MAX_VALUE
}
