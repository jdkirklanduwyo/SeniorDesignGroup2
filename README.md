COSC 4950 - Senior Design - The Power of Robotany
================================================

**Group Members:**

	-Jake Kirkland
	-Kevin Shelton
	-Nate Kuhn

**Project Name:**

The Power of Robotany

**Description:**

Our project is a remote service that runs on an Arduino Uno that supports interaction with different types of sensors and devices. In addition to this, a server service was created that saves data from the arduino in an XML file, and allows the arduino to set settings from the XML. A client application for Android was designed to provide a streamlined and convenient way for the user to adjust and check the functions of the sensors and devices. We also used C.A.R.T. Artificial Intelligence decision trees to provide a learning facet to the server to further user convenience and hopefully ultimately remove the human factor from the equation. 
	
We will now delve into the following specifics:
	
		- Description of Files and Folders
		- Explain packages and resources
		- Explain how to look through the repo
		- Help navigate the repo 

Our project contains the following files and folders:

* **Documents:** Contains mainly our presentation information.  

* **Source:** 
In the Arduino folder, the required libraries and environment are located in arduino_pixy, arduino-1.6.12 and PixyMon. Our code is located in:

	-SeniorDesignGroup2\Source\Arduino\ourSketches\ArduinoSketchFinal
	
ArduinoSketchFinal being the final iteration of our code. The MobileApp folder contains the project folder PlantCareSystem that can be opened in Android Studio for the full list of files, but the important Java and res classes are located in:

	SeniorDesignGroup2\Source\MobileApp\PlantCareSystem\app\src\main\java\com\cosc\nathaniel\plantcaresystem
	SeniorDesignGroup2\Source\MobileApp\PlantCareSystem\app\src\main\res\layout
	
For the python server, our code is located at:

	SeniorDesignGroup2\Source\Python ReST Server	
	
And PythonWebServerFinal.py is our final code version. plant_data.xml is the xml file created by the server.

* **Resources:** Resources will be included here, such as helpful websites and the like.

Instructions for looking through the repo:

* **Documents:** Check this for our presentation on our project.

* **Source:** Continue to the source folder for info on how the project works, there are three main components: Arduino, App and Python Server. Internal comments on the code will provide further explanation.

* **Resources:** Visit this folder if you are looking for where we got information or ideas from. See the README file within the Resources folder for a further description. 

* **To Run:** If you would actually like to build our project, you need the Arduino IDE with Pixy and DHT libraries imported to upload the sketch to an Arduino (Ensuring your local network info is placed into the sketch). Once the arduino is setup, use Android Studio to install the app onto an android device. Simply run the Python server on Python 2.7 or later on the specified computer on the local network and begin adding plants with the app.
