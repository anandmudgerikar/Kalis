
BUILD_EXTRA_DEPS += TestNetwork.class

CLEAN_EXTRA = *.class TestNetworkMsg.java

                

TestNetwork.class: $(wildcard *.java) TestNetworkMsg.java
	javac *.java

TFLAGS = -I /home/odroid/tinyos-main/types -I /home/odroid/tinyos-main/platforms/null -I /home/odroid/tinyos-main/lib/serial -DNEW_PRINTF_SEMANTICS 

TestNetworkMsg.java:
	mig java -target=null $(TFLAGS) -java-classname=TestNetworkMsg ../TestNetwork.h TestNetworkMsg -o $@
