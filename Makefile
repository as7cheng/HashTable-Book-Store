junit5:
	javac -cp .:./junit-platform-console-standalone-1.5.2.jar *.java
	java -jar junit-platform-console-standalone-1.5.2.jar --class-path . -p ""

clean:
	\rm -f *.class
