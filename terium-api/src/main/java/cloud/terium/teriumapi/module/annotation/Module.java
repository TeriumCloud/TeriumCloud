package cloud.terium.teriumapi.module.annotation;

import cloud.terium.teriumapi.module.ModuleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {

    String name();

    String author();

    String version();

    String description();

    boolean reloadable();

    ModuleType moduleType();
}