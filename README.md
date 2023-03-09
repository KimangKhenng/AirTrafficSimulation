
# Air Traffic Simulation Project
This is a simulation project that simulates air traffic using multi-agent system framework in Java. It uses Maven to manage dependencies and the Java JADE library for the multi-agent framework.

The project simulates air traffic by initializing route data defined in an XML file for plane trajectories. Each plane has a unique identifier, starting location, and destination. It passes through several checkpoints, which are points defined by their latitude and longitude. During the simulation, planes have to negotiate with air traffic controllers and other planes to ensure safety.

## Getting Started
To get started with this project, follow these steps:

- Clone this repository to your local machine.
- Open the project in your preferred Java IDE.
- Build the project using Maven.
- Run the simulation using the Main class in main/java/MainWindows/Main.java 
## Prerequisites
To run this project, you will need the following:

Java 8 or higher
Maven
## Dependencies
This project uses the following dependencies:

JADE 4.5.0
## Usage
Once the simulation is running, you can view the output in both console and GUI. The GUI will display the actions taken by the planes and air traffic controllers as they negotiate routes and ensure safety.

## Configuration
The XML file containing the route data can be found in resources/lightscheduels.xml directory. You can modify this file to change the routes that planes take during the simulation.

## Contributing
If you would like to contribute to this project, feel free to submit a pull request. Please ensure that your code follows the project's coding standards and has appropriate unit tests.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
