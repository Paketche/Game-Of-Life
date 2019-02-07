Game Of Life
============

 # Introduction
 
This repository contains a Java implementation of Conway's Game Of Life. The implentation includes an user interface 
that allows for the visuallisation of the separate states of the game of life. 

# Rules of Game of life

A the Game of Life  consists of an plane on which multiple cells are place. Each cell is ment to represent a living organisim.
The game of life plays out by itirating over and over gain on the current generation of living cells. On each iteration each 
cell survives or dies based on the following rules:

1) If a living cell is surrounded by 2 or 3 living cells, it stays alive and carries over to the next genration.
2) If a living cell is surrounded by less than 2 living cells, it dies due to underpopulation.
3) If a living cell is surrounded by more than 3 living cells, it dies due to overpopulation.
4) If a dead cell is surrounded by exactly 3 living cells, it becomes alive mimicing the act of reproduction.

# Implementation

The Game of life user interface is implemented using the Model-View-Controller design pattern. This way separation of concerns
as well as easily maintainable code is acheived.

## Model

The model of the application is repressented by the [Colony](https://github.com/Paketche/Game-Of-Life/blob/master/src/life/Colony.java) 
and the [Cell](https://github.com/Paketche/Game-Of-Life/blob/master/src/life/Cell.java) classes. The Colony class maintains a collection
cells and provides methods for interacting with the colelction. In order to efficiently create an infinite grid and iterate over all of 
the cells in it, the Colony class maintains a collection of only the living cells. However, keeping just the living cells will make it 
impossible for adhering to rule 4) as there will be no dead cells to change their state. That is why whenever a cell becomes alive or a 
living cell is inserted into the colony, a 8 more dead cells are insterted arround it(dead cells are inserted only if they do not 
offerride an existing one). This way the number of trackedcells is still kept to a minimum and is able to adhere to all the rules. 
Furthermore, if a dead cell has no living neignbours, it is deleted in order to conserve memory.  The Colony class maintains two 
HashMaps that map an XY tuple containing the cartecian coordinates of each cell, making the colony easy to query whenever a cell needs 
to get a reference of its neignbouring cells. The two hashmaps contain the current generation and the next generation. The reason for 
this is so that cells may update their state only after the all of them have been iterrated through. Otherwise, if a cell changes its 
state after being iterrated over neignbouring cells will be affected by that change when their turn comes. On, every iteration the next 
generation become the current and a new map is allocated for becoming then the next generation.

## View

The [ColonyView](https://github.com/Paketche/Game-Of-Life/blob/master/src/UI/ColonyView.java) of the application extends the JavaFX 
Canvas class. Its role is to visualize the the living cells to the user interface. As the canvas takes in arguments that are in polar 
coordinates and the cells has cartesian coordinates, the ColonyView maintains polar coordinates of the center of the canvas so that it 
may easily map the cells from the center of the canvast outwards to the edges. Furthermore, the class provides a methdod for that center 
offset to be shifted, making it possible for the controller to drag the canvas arround.

## Controller

The [ColonyController](https://github.com/Paketche/Game-Of-Life/blob/master/src/UI/ColonyView.java) is a very simple class that simply 
 maps buttons to the corresponding their functionality as well as maintain 
the looping event that iterates the colony generations. The following are the controlls that this class handles: 

1) Playing the iteration loop.
2) Pausing the iteration loop.
3) Stepping through one generation at a time.
4) Changing the duration each generation is displayed on the screen.
5) Moving the canvas by dragging the mouse on it.
6) Centering the canvas to its orriginal possition.

# How to use

To start the program...Afterwards, you may load one if the [patterns](https://github.com/Paketche/Game-Of-Life/tree/master/patterns) 
contained in the projects. You are able to make your own CSV file and create an test as many colony patterns as you like, however there 
are two rules to the file structure. Each row in the file must contain the same amount of values and living cells must be denoted with a 
value of one. After you have loaded the file you are free to manipulate the canvas and the speed of the generation by using the controls. 
