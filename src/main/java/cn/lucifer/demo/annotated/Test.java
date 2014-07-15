/**
 * 
 */
package cn.lucifer.demo.annotated;

import java.lang.annotation.Annotation;

import com.google.code.morphia.annotations.Entity;

/**
 * @author Lucifer
 * 
 */
@Entity(value = "tt")
public class Test {
	public static void main(String[] args) {
		Annotation[] as = EntityDemo.class.getAnnotations();
		for (Annotation annotation : as) {
			System.out.println(annotation);
		}

		LuciferAnnotated a = Test.class.getAnnotation(LuciferAnnotated.class);
		System.out.println(Test.class
				.isAnnotationPresent(LuciferAnnotated.class));
		System.out.println(a);
		Entity b = Test.class.getAnnotation(Entity.class);
		System.out.println(b);
	}
}
