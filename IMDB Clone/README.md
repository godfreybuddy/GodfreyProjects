# CS 474 SQL Project
Authors: Max Samoylov, Phillip Zubov, Tyree Mitchell, Buddy Godfrey

Link to Scrum board: https://app.vivifyscrum.com/boards/58638


## To-Do

- [x] Generic SQL queries.
- [x] Return search results.
- [x] Display a content pane showing more details of a search result.
- [x] Develop a filter and allow for more advanced queries.
- [x] Flesh out the content pane more to show even more details such as images from the web, and associated content from other tables.



## About
This project is a front-end application for an "IMDB clone" database for JMU's CS 474 Databases class.
A majority of the semester was spent creating the database schema, ER diagram and then the actual database of an IMDB clone database.
For the last leg of the semester, due for the final part of the project, we were to implement a frontend application for the IMDB clone interface.

The project is built in Java Swing and uses JDBC, this language and technology was chosen as the comfort level amongst all team members is well with Java Swing.
  
This application for the project was designed using Java Swing to be a similar experience to the IMDB website, as it runs off a similar database. It is a cut and dry program as far as functionality is concerned, and will offer a user a straight forward approach to finding information from queries corresponding to the IMDB database.   

## Setup
The Source code is in the uploaded files. If you choose to build and run from source the following procedure worked for us using Eclipse.

  1.	Drag the source code(Backend folder, UI folder, Main.java) from: …\474SQLProgram-1.0.0\program\src  into the Eclipse “src” folder   and the Assets folder: … \474SQLProgram-1.0.0 into the Eclipse project folder.

  2.	Right click on your Project -> Build Path -> Edit Build Path, then click on the Libraries tab if not already there, click on  
  Classpath then “Add External JARs…” and select both the mysql-connector jar and material-ui-swing jar from the Assets folder.
  If you choose to not use the skin or because the material-ui-swing.jar file gives you issues you can remove the try/catch block in
  Main.java that begins on line 18 and ends on line 20. Server/Connection settings can be changed through the Settings button in the
  program, or line 27 in Main.java (Line 24 or 25 if you deleted the previously mentioned try/catch block). 
  
## How To Use
Upon launching the program, the user is presented with a windowed interface that allows them to enter in a query into a search bar. The
search can either be executed by hitting the search button or by tapping “enter” on your keyboard. Additionally, you can adjust a few
settings prior to searching; one such feature in the settings is a filter for weeding out all the adult content in the database. 

After searching for your query, a list of results is posted for you to select from. Selecting a search result from the list will bring up another window with information pertaining to that selected results containing information ranging from fellow cast members to episodes from a television series. From this point in the program you can either go down a “rabbit hole” of internal searching, being able to infinitely click on some of the displayed information within that pane to go to another pane with new information displayed about what you had selected, or you can click the back button from anywhere within a search progress to return to the prior pane. 
