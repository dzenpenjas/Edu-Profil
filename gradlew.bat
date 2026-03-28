@ECHO OFF
SET DIR=%~dp0
SET APP_HOME=%DIR%
SET CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

IF NOT EXIST "%CLASSPATH%" (
  ECHO Missing gradle-wrapper.jar. Open this project in Android Studio and let Gradle setup finish there.
  EXIT /B 1
)

java -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
