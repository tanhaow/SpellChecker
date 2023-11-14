# SpellMaster: Java Spell Checker

## Overview
SpellMaster is a simple Java application for spell checking and correction. It's designed to be user-friendly and flexible, allowing users to upload their preferred dictionary and check spelling in their text files. With simple interface, SpellMaster not only identifies errors but also suggests the best replacements, making your writing flawless.

## Features
- **Dynamic File Input**: Users can select dictionary and text files at runtime.
- **Interactive Correction**: Offers suggestions for misspelled words, enabling user-driven corrections.
- **Advanced Word Recommendations**: Employs similarity metrics and commonality percentages for optimal word replacements.
- **Efficient Output Management**: Uses the `Printer` class for real-time, append-mode output file writing.
- **Comprehensive Test Suite**: Guarantees reliability with extensive testing.

## Components
- `SpellChecker.java`: Core class for spell checking and user interaction.
- `WordRecommender.java`: Suggests words using similarity and commonality metrics.
- `Printer.java`: Handles efficient output file operations.
- `Util.java`: Manages user-interaction prompts.
- `engDictionary.txt`: Example dictionary file.
- `sampleInput1.txt`: Example text file for spell checking.
- Test Classes: Include `SpellCheckerRunner.java`, `PrinterTest.java`, `WordRecommenderTest.java`, and more.

## Getting Started
### Prerequisites
- Java Runtime Environment (JRE) and Java Development Kit (JDK).

### Usage
Launch the application and Run `SpellCheckerRunner.java`. You will be prompted to input the dictionary and text file names. The application will then perform spell checking, identifying misspelled words and offers suggestions. Users can select a suggestion, accept the misspelling, or input a new word. Corrected text is saved in an output file, named after the input file with "_chk" added.

## Contact
- Hao Tan: [tanhao@seas.upenn.edu](mailto:tanhao@seas.upenn.edu)
- Lisa Ling: [lisaling.yx@gmail.com](mailto:lisaling.yx@gmail.com)
