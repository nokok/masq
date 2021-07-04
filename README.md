# masq

masq is a command line tool to erase sensitive information in SQL/MySQL Process List

# Examples

## 1. Plain SQL

```bash
$ cat select_users_by_name.sql
SELECT
  *
FROM
  users
WHERE
  user_name = 'Noriyuki Kazusawa';
```

```bash
$ masq -t sql select_users_by_name.sql
SELECT * FROM users WHERE user_name = '<mask>'
```

## 2. MySQL Full Process List

```bash
$ masq -t mysql/process-list processlist.txt > masked_result
```

# Usage

```bash
Usage: masq [input_file] [options...]

[supported file types]
  sql
  mysql/process-list
[options]
    -r, --replace  Replacement string
 -t, --input-type  Specifies the file format of input_file
       -h, --help  Print this help text and exit
```

# How to run

## Requirements

GraalVM 21.x+ with JDK 16

## Native-Image
 
```
$ ./gradlew nativeImage
$ ./build/bin/masq
```

## JAR

```
$ ./gradlew shadowJar
$ java -jar ./build/libs/masq-all.jar
```

# Limitations

- The current version of masq erases newlines in SQL.
