*** BULK OF CODE IN Node.java ***

AML2194
CSEE 4119
-------------
PA2
-------------
Description
=============
This program implements a distance vector routing protocol.

The program takes 2 input files, Graph and Events. They behave as follows:
- Graph describes the topology:
  - The first line in the number of nodes
  - Then there is one line per node, with numbers separated by a space for:
  node_ID neighbor1_ID costToReachneighbor_1 neighbor2_ID costToReachneighbor_2 ...

- Event describes the simulation. First line is the timeslots («turns» for the duration of
your simulation), second is the number of events, and then you have one line per event (each characteristics are separated by a space) :
  - the turn at which the event is taken into account
  - the type of event: R for remove a link between two nodes ; M to modify the
    cost between two nodes.
  - ID of the source node for the link that is changed/suppressed.
  - ID of the destination node for the link that is changed/suppressed.
  - for M events only: the new cost.

The program outputs a file with the topology, with each line containing a source node and a node that it can reach followed by the distance from the source node to that following node.
----------
Execution
==========
All .java files included in ./src.
Graph, Events, Output files included in PA2.

run with:
$ javac *.java
$ java Main ../Graph ../Events ../Output 
