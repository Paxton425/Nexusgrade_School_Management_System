package com.nexusgrade.app.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    String action();      // e.g., "Update Grade"
    String entityType();  // e.g., "STUDENT"
}