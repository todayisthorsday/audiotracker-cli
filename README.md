# 🎧 Audiobook Tracker CLI

## Overview
This is a command-line interface built in Java that connects to the Audiobook Tracker API. It provides a simple, text-based menu to view listening statistics and browse audiobook data.

## Features
- View total listening time by user
- List audiobooks read by a user
- Browse audiobooks by genre
- See sessions by audiobook
- View 5 most recent sessions
- Friendly terminal formatting with spacing and emoji for readability

## Technologies
- Java 17
- Jackson (JSON parsing)
- Standard Java HTTP requests

## Requirements
- The Audiobook Tracker API must be running at `http://localhost:8080`
- Java 17 or later installed

## How to Run
```bash
javac -d out src/main/java/com/audiotracker/audiotracker_cli/**/*.java
java -cp out com.audiotracker.audiotracker_cli.Main
```

##
Don't have the API set up yet?  
Grab the [Audiobook Tracker API](https://github.com/todayisthorsday/audiotracker-api)
