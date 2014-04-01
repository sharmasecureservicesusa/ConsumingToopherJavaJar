# Consuming the Toopher Jar

This is a simple project that I used to consume the [toopher-java](https://github.com/toopher/toopher-java) [1.0.0-SNAPSHOT jar](https://oss.sonatype.org/content/repositories/snapshots/com/toopher/toopher-java/).

The important bit in `pom.xml`:

    <dependency>
        <groupId>com.toopher</groupId>
        <artifactId>toopher-java</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>

To run this project navigate to `Main.java` and change the `PAIRING_PHRASE` to a pairing phrase that you generate, then adjust the other constants. Also, the program expects you to set environment variables for `TOOPHER_CONSUMER_KEY` and `TOOPHER_CONSUMER_SECRET` before launching.

Hope this helps.
