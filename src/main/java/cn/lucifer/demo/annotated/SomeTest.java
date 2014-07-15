/**
 * 
 */
package cn.lucifer.demo.annotated;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.google.code.morphia.annotations.Entity;

/**
 * @author Lucifer
 * 
 */
@Entity(value = "tt")
public class SomeTest {

	@Test
	public void testMain() {
		Annotation[] as = EntityDemo.class.getAnnotations();
		for (Annotation annotation : as) {
			System.out.println(annotation);
		}

		LuciferAnnotated a = SomeTest.class
				.getAnnotation(LuciferAnnotated.class);
		System.out.println(SomeTest.class
				.isAnnotationPresent(LuciferAnnotated.class));
		System.out.println(a);
		Entity b = SomeTest.class.getAnnotation(Entity.class);
		System.out.println(b);
	}
}
