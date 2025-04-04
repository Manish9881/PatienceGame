# Patience Game

A console-based card game implemented in Java, simulating the classic game of Patience (Solitaire). The game features an interactive command-line interface, game state visualization, scoring, and input validation.

## 🕹️ Features

- Display of lanes, foundation piles, draw pile, score, and move count.
- Prompt-based input system for user interaction.
- Full validation for input commands and gameplay rules.
- Efficient card handling using `Deque`.
- Horizontal and vertical layout options for card display (legacy + current support).
- Replayability with new deck shuffle on restart.

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- Maven (optional, if you want to rebuild the project)

### Run from Prebuilt JAR

1. Download or clone the repository.
2. Navigate to the project folder:
   ```bash
   cd PatienceGameV3
   ```
3. Run the game:
   ```bash
   java -jar target/PatienceGameV3-1.0-SNAPSHOT.jar
   ```

### Build from Source

If you want to build the game from source:

```bash
mvn clean package
java -jar target/PatienceGameV3-1.0-SNAPSHOT.jar
```

## 🎮 Gameplay Instructions

- The game starts by displaying the initial layout and instructions.
- You will be prompted to enter commands (e.g., move cards, draw from pile).
- Invalid inputs are flagged with helpful messages.
- The game keeps track of moves and score in real-time.
- You can quit or restart the game anytime.

## 🧪 Testing

The project includes JUnit test cases for core components:

```bash
mvn test
```

Test classes include:
- `CardTest`
- `GameTest`
- `BoardPrinterTest`
- `GameInitializerTest`

## 📂 Project Structure

```
PatienceGameV3/
├── pom.xml                  # Maven build file
├── src/                     # (Source files - may need to be restored if not included)
├── target/                  # Compiled .class files and final .jar
├── .gitignore
└── PatienceGameV3-1.0-SNAPSHOT.jar
```
