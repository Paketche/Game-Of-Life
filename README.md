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

The model of the application is repressented by the [Colony]() and the [Cell]() classes. The Colony class maintains a collection
of active cells. 
