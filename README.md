Game Of Life
============

 # Introduction
 
This repository contains a Java implementation of Conway's Game Of Life. The implantation includes an user interface 
that allows for the visualisation of the separate states of the game of life. 

# Running the game

To build the project and run it simply open a terminal and `gradle run`. Alternatively if you do not have gradle installed use the gradle wrapper: `gradlew run`.

# Rules of Game of life

A the Game of Life  consists of an plane on which multiple cells are place. Each cell is meant to represent a living organism.
The game of life plays out by iterating over and over gain on the current generation of living cells. On each iteration each 
cell survives or dies based on the following rules:

1) If a living cell is surrounded by 2 or 3 living cells, it stays alive and carries over to the next generation.
2) If a living cell is surrounded by less than 2 living cells, it dies due to underpopulation.
3) If a living cell is surrounded by more than 3 living cells, it dies due to overpopulation.
4) If a dead cell is surrounded by exactly 3 living cells, it becomes alive mimicking the act of reproduction.

# Implementation

The Game of life user interface is implemented using the Model-View-Controller design pattern. This way separation of concerns
as well as easily maintainable code is acheived.

## Model

The model of the application is represented by the [Colony](https://github.com/Paketche/Game-Of-Life/blob/master/src/life/Colony.java) 
and the [Cell](https://github.com/Paketche/Game-Of-Life/blob/master/src/life/Cell.java) classes. The Colony class maintains a collection
cells and provides methods for interacting with the collection. In order to efficiently create an infinite grid and iterate over all of 
the cells in it, the Colony class maintains a collection of only the living cells. However, keeping just the living cells will make it 
impossible for adhering to rule 4) as there will be no dead cells to change their state. That is why whenever a cell becomes alive or a 
living cell is inserted into the colony, a 8 more dead cells are inserted around it(dead cells are inserted only if they do not 
override an existing one). This way the number of tracked cells is still kept to a minimum and is able to adhere to all the rules. 
Furthermore, if a dead cell has no living neighbours, it is deleted in order to conserve memory.  The Colony class maintains two 
HashMaps that map an XY tuple containing the cartesian coordinates of each cell, making the colony easy to query whenever a cell needs 
to get a reference of its neighbouring cells. The two hash maps contain the current generation and the next generation. The reason for 
this is so that cells may update their state only after the all of them have been iterated through. Otherwise, if a cell changes its 
state after being iterated over neighbouring cells will be affected by that change when their turn comes. On, every iteration the next 
generation become the current and a new map is allocated for becoming then the next generation.

## View

The [ColonyView](https://github.com/Paketche/Game-Of-Life/blob/master/src/UI/ColonyView.java) of the application extends the JavaFX 
Canvas class. Its role is to visualize the the living cells to the user interface. As the canvas takes in arguments that are in polar 
coordinates and the cells has cartesian coordinates, the ColonyView maintains polar coordinates of the center of the canvas so that it 
may easily map the cells from the center of the canvas outwards to the edges. Furthermore, the class provides a method for that center 
offset to be shifted, making it possible for the controller to drag the canvas around.

## Controller

The [ColonyController](https://github.com/Paketche/Game-Of-Life/blob/master/src/UI/ColonyView.java) is a very simple class that simply 
maps buttons to the corresponding their functionality as well as maintain 
the looping event that iterates the colony generations. The following are the controls that this class handles: 

1) Playing the iteration loop.
2) Pausing the iteration loop.
3) Stepping through one generation at a time.
4) Changing the duration each generation is displayed on the screen.
5) Moving the canvas by dragging the mouse on it.
6) Centering the canvas to its original position.

# How to use

To start the program by launching the runanble jar file.Afterwards, you may load one if the [patterns](https://github.com/Paketche/Game-Of-Life/tree/master/patterns) 
contained in the projects. You are able to make your own CSV file and create an test as many colony patterns as you like, however there 
are two rules to the file structure. Each row in the file must contain the same amount of values and living cells must be denoted with a 
value of one.

![Opening File](https://github.com/Paketche/Game-Of-Life/blob/master/pictures/Screenshot%20from%202019-02-07%2013-05-44.png)

After you have loaded the file you are free to manipulate the canvas and the speed of the generation by using the controls. 

![UI](https://github.com/Paketche/Game-Of-Life/blob/master/pictures/Screenshot%20from%202019-02-07%2013-10-42.png)


