@ECHO OFF

IF EXIST smartparking DEL smartparking
IF NOT EXIST scala-library.jar COPY %SCALA_HOME%\lib\scala-library.jar .

CALL scalac -sourcepath src -d bin src\viewScala\Main.scala

CD bin
jar -cfm ..\smartparking ..\MANIFEST.MF *.*
CD ..

java -jar smartparking
