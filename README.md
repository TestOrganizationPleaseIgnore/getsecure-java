# GetSecure Java

A Java implementation of secure links generator for limited time access with simple MD5 hash.

## Requirements

- Java 11 or higher
- Maven

## Building

To build the project:

```bash
mvn clean package
```

This will create two JAR files in the `target` directory:
- `getsecure-1.0-SNAPSHOT.jar` - The basic JAR
- `getsecure-1.0-SNAPSHOT-jar-with-dependencies.jar` - JAR with all dependencies included

## Usage

### As a Command Line Tool

You can run GetSecure Java from the command line using the JAR with dependencies:

```bash
java -jar target/getsecure-1.0-SNAPSHOT-jar-with-dependencies.jar [options] url

Options:
  -s, --secret SECRET    Secret key for hash generation (required)
  -t, --time TIME        Time in minutes for link validity (default: 30)
  -p, --param PARAM      Parameter name for the hash (default: 'hash')
  -e, --expires PARAM    Parameter name for expiry timestamp (default: 'expires')
```

Example usage:
```bash
# Generate a secure link valid for 30 minutes
java -jar target/getsecure-1.0-SNAPSHOT-jar-with-dependencies.jar -s mysecret123 https://example.com/resource

# Generate a secure link valid for 60 minutes
java -jar target/getsecure-1.0-SNAPSHOT-jar-with-dependencies.jar -s mysecret123 -t 60 https://example.com/resource

# Custom parameter names
java -jar target/getsecure-1.0-SNAPSHOT-jar-with-dependencies.jar -s mysecret123 -p signature -e validuntil https://example.com/resource
```

### As a Library

To use GetSecure as a library in your Java project, first add it as a dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>com.minbash</groupId>
    <artifactId>getsecure</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then use it in your code:

```java
import com.minbash.getsecure.SecureLink;

public class Main {
    public static void main(String[] args) {
        String secret = "mysecret123";
        String url = "https://example.com/resource";
        String secureUrl = SecureLink.generate(secret, url);
        System.out.println(secureUrl);
    }
}
```
