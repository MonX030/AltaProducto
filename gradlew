#!/bin/sh
# Gradle start up script (simplified). Requires gradle/wrapper/gradle-wrapper.jar.
# If the jar is missing, open the project in Android Studio (it downloads Gradle
# from gradle-wrapper.properties) or run `gradle wrapper` once with a local Gradle.
APP_HOME=$(cd "$(dirname "$0")" && pwd -P)
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then JAVACMD="$JAVA_HOME/bin/java"; else JAVACMD=java; fi
if [ ! -f "$CLASSPATH" ]; then
  echo "Falta gradle/wrapper/gradle-wrapper.jar. Abre el proyecto en Android Studio o ejecuta 'gradle wrapper'." >&2
  exit 1
fi
exec "$JAVACMD" -Dorg.gradle.appname=gradlew -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
