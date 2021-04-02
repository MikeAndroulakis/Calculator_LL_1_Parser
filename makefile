all: compile

compile:
	@java -jar jtb132di.jar minijava.jj
	@java -jar javacc5.jar minijava-jtb.jj
	@javac Main.java

execute:
	@java Main tests/BubbleSort.java tests/MoreThan4.java tests/Factorial.java

clean:
	rm -f *.class *~
