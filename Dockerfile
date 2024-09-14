Index: build.gradle
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/build.gradle b/build.gradle
--- a/build.gradle (revision 93b773dced2ff543c1ef5c42c1c1803740649330)
+++ b/build.gradle (date 1726178775839)
@@ -9,7 +9,7 @@

 java {
     toolchain {
-        languageVersion = JavaLanguageVersion.of(17)
+        languageVersion = JavaLanguageVersion.of(21)
     }
 }

Index: docker-compose.yml
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/docker-compose.yml b/docker-compose.yml
--- a/docker-compose.yml (revision 93b773dced2ff543c1ef5c42c1c1803740649330)
+++ b/docker-compose.yml (date 1726179115992)
@@ -23,7 +23,7 @@
     ports:
       - '8080:8080'
     environment:
-      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5433/mydatabase
+      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/mydatabase
     depends_on:
       - postgres
     networks:
Index: Dockerfile
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/Dockerfile b/Dockerfile
--- a/Dockerfile (revision 93b773dced2ff543c1ef5c42c1c1803740649330)
+++ b/Dockerfile (date 1726178855436)
@@ -1,5 +1,5 @@
 # Stage 1: Build the application with Gradle
-FROM gradle:latest as builder
+FROM gradle:latest AS builder

 WORKDIR /app