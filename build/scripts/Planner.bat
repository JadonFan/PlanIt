@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Planner startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PLANNER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Planner.jar;%APP_HOME%\lib\commons-math3-3.6.1.jar;%APP_HOME%\lib\mysql-connector-java-8.0.16.jar;%APP_HOME%\lib\jbcrypt-0.4.jar;%APP_HOME%\lib\jfoenix-9.0.8.jar;%APP_HOME%\lib\hibernate-entitymanager-5.4.2.Final.jar;%APP_HOME%\lib\hibernate-osgi-5.4.2.Final.jar;%APP_HOME%\lib\hibernate-core-5.4.2.Final.jar;%APP_HOME%\lib\google-api-client-java6-1.28.0.jar;%APP_HOME%\lib\google-api-client-servlet-1.28.0.jar;%APP_HOME%\lib\google-api-services-gmail-v1-rev103-1.25.0.jar;%APP_HOME%\lib\google-api-services-oauth2-v2-rev150-1.25.0.jar;%APP_HOME%\lib\google-api-client-1.28.0.jar;%APP_HOME%\lib\google-oauth-client-jetty-1.28.0.jar;%APP_HOME%\lib\google-oauth-client-java6-1.28.0.jar;%APP_HOME%\lib\google-http-client-jackson2-1.29.1.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.9.8.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.9.8.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.9.8.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\jackson-annotations-2.9.8.jar;%APP_HOME%\lib\jackson-core-asl-1.9.13.jar;%APP_HOME%\lib\google-oauth-client-servlet-1.28.0.jar;%APP_HOME%\lib\google-oauth-client-1.28.0.jar;%APP_HOME%\lib\google-http-client-apache-2.0.0.jar;%APP_HOME%\lib\google-http-client-jdo-1.28.0.jar;%APP_HOME%\lib\google-http-client-1.29.1.jar;%APP_HOME%\lib\opencensus-contrib-http-util-0.19.2.jar;%APP_HOME%\lib\guava-26.0-jre.jar;%APP_HOME%\lib\protobuf-java-3.6.1.jar;%APP_HOME%\lib\hibernate-commons-annotations-5.1.0.Final.jar;%APP_HOME%\lib\jboss-logging-3.3.2.Final.jar;%APP_HOME%\lib\javax.persistence-api-2.2.jar;%APP_HOME%\lib\javassist-3.24.0-GA.jar;%APP_HOME%\lib\byte-buddy-1.9.10.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jandex-2.0.5.Final.jar;%APP_HOME%\lib\classmate-1.3.4.jar;%APP_HOME%\lib\jaxb-runtime-2.3.1.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\dom4j-2.1.1.jar;%APP_HOME%\lib\javax.interceptor-api-1.2.jar;%APP_HOME%\lib\org.osgi.core-6.0.0.jar;%APP_HOME%\lib\org.osgi.compendium-5.0.0.jar;%APP_HOME%\lib\servlet-api-2.5.jar;%APP_HOME%\lib\jdo2-api-2.3-eb.jar;%APP_HOME%\lib\jetty-6.1.26.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-2.5.2.jar;%APP_HOME%\lib\error_prone_annotations-2.1.3.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.14.jar;%APP_HOME%\lib\txw2-2.3.1.jar;%APP_HOME%\lib\istack-commons-runtime-3.0.7.jar;%APP_HOME%\lib\stax-ex-1.8.jar;%APP_HOME%\lib\FastInfoset-1.2.15.jar;%APP_HOME%\lib\httpclient-4.5.5.jar;%APP_HOME%\lib\transaction-api-1.1.jar;%APP_HOME%\lib\jetty-util-6.1.26.jar;%APP_HOME%\lib\servlet-api-2.5-20081211.jar;%APP_HOME%\lib\opencensus-api-0.19.2.jar;%APP_HOME%\lib\httpcore-4.4.9.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\grpc-context-1.18.0.jar

@rem Execute Planner
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PLANNER_OPTS%  -classpath "%CLASSPATH%" home.LogIn %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PLANNER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PLANNER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
