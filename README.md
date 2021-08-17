# Touka V3
[![Support Server](https://img.shields.io/discord/783764380398518292.svg?label=Discord&logo=Discord&colorB=7289da&style=for-the-badge)](https://discord.com/invite/tvDXKZSzqd)

Another Touka rewrite but not cluttered

# How to run
First of all you have to change the necessary bot tokens and settings in the `src/main/java/cofig/Config.java` and `src/main/java/commands/search/ApiRequest.java` files.

## Configuration
```java
// File: src/main/java/cofig/Config.java

/*  Discord Bot  */
public static final String BOT_TOKEN = "YOUR BOT TOKEN";

/*  Error Discord Bot */
    
// Bot sends exceptions for debugging purposes into the specified discord channel
public static final String ERROR_BOT_TOKEN = "YOUR BOT TOKEN";
public static final long ERROR_GUILD_ID = 0L;
public static final long ERROR_CHANNEL_ID = 0L;

/*  Discord Bot List  */
public static final String DBL_TOKEN = "DISCORD BOT LIST TOKEN";
public static final String DBL_ID = "DISCORD BOT LIST ID";

/*  Bot Owner */
// Here you have to write your own client id inside the List.of(); function.
// Please note that you should write it with an L at the end to indicate that the value is of type Long
public static final List<Long> BOT_OWNER = List.of(265849018662387712L);
```

```java
// File: src/main/java/commands/search/ApiRequest.java

// Change the URL to point to your levi-api instance.
// For example if you want to use your local hosted instace use this:
// "http://localhost:3000/touka/api/v2/"
private static final String BASE_URL = "http://touka.tv:3000/touka/api/v2/";
```


## Compile 
To compile the bot you have to be in the project root folder and run the following command: \
`mvn clean install assembly:single`

(If this does not work check your Java version. To compile you need to have at least JDK 14 installed)

The finished jar file should now be located inside the target folder.

## Running the Bot
From the project root after compilation just execute the following command: \
`java -jar ./target/touka-3.0-jar-with-dependencies.jar`
