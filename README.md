# parens

Install maven and ensure the executable lies on your system's path variable, or is installed with your IDE of choice.

Run 'mvn clean install' to place into your own repository, or for a simple demonstration, 'mvn clean test'.

A main method also exists for taking file input. This method writes the input expression followed by the number of parethesizations of the expression that evaluate to true. The output file also contains an un-formated 2d array of execution times (in milliseconds) for each step of the algorithm - essentially the time it takes to compute each parenthesization.

See pom.xml for a simple example of tuning the performance of your JVM to handle larger inputs. Input strings containing more than 1000 operands require a certain amount of memory; around 8 Gigabytes. OutOfMemoryException will be thrown if the memory is not available.

Project Statement:

A Boolean sequence is a sequence of 0’s (for False), 1’s (for True), a’s (for the connective “and”), r’s (for the connective “or”), x’s (for the connective “xor”), or n’s (for the connective “nand”) such that: it starts and ends with a 0 or a 1, and between two consecutive bits (0’s or 1’s) there is exactly one of the four connectives: a, r, x, or n. E.g., α = 1a1a1x0x0r1a0n0x1 is a legal Boolean sequence. A (full) parenthesization of a Boolean sequence is obtained by inserting parentheses in the sequence so that it ends being completely parenthesized, i.e. each connective applies to precisely two arguments. E.g., the sequence α has many parenthesizations two of which are
(((1a1)a1)x(((0x0)r1)a0))n(0x1) and 1a((1a(1x0))x((0r(1a(0n0)))x1)) .
