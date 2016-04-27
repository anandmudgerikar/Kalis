# README #

### TO-DOs ###

Immediate items to do:
* Check all the TODOs in the Java code and in the TelosB code and get them all done


To-do items for next iteration:
* Implement a couple more Modules for different attacks (Wifi vs ZigBee, ...).
* Add Network Profiling module for traffic load, stats, ...



Done:


* Make sure that we can listen to REAL-TIME, UNLIMITED AMOUNTS of WiFi traffic
* Remove need for full package name in initial config file and simply add that string in front when you are about to use Reflection to load the module class.
* PacketFactory.getPacket(String[] raw) does not call any Packet constructor! (And actually would always generate an error at the moment...).
* The DataStore.onNewPacket() methods have a lot of duplicated code!
* The DataStore.onNewPacket() methods should not care if the packet is coming from a trace or from a live stream... why do we have that parameter? And why is the timestamp set there?
* The DataStore.onNewPacket() method for CSV packets also logs to a CSV file, which does not make sense.
* In the Main, the "tracefile" must be specifiable by command line arguments.
* Switch from the "Listen" thing to the PrintF primitive in TinyOS to send text over serial to the listening Java application. We need to send data from the application layer (when receiving generic non-CTP packets) and from inside CtpForwardingEngine (when receiving CTP packets).
* Time correctly the notification of packets when replaying a trace.
* Capture all overheard traffic in ZigBee, switching from Receive to Snoop (we assume that the IDS is not part of the monitored network).
* Install WiFi card
* Format from sensor to Java: SrcAddr DstAddr MpduLinkQuality RSSI? PayloadLen Payload
* Decode intercepted packets on the Java side
* Intercept all WiFi traffic (for now, capture source IP, dest IP, data even if encrypted)
* Add Enum with all packet types
* Add first field in CSV format to store type of packet
* Implement a SelectiveForwarding module.
* Design Environment Sensing as a different kind of Module
* Figure out SoC for DataStore and Modules wrt queues
* Have each module expose its characteristics statically (for now, e.g., "IsForMultihop" and "IsForSinglehop" could be static bool properties). The KnowledgeBase component then should be able to iterate over all modules in the edu.purdue.idsforiot.modules package and select only those that have the right characteristics for the current environment.
* Environmental Sensing component