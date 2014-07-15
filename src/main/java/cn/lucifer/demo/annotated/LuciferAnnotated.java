/**
 * 
 */
package cn.lucifer.demo.annotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Lucifer
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LuciferAnnotated {
	String name() default "";
}
