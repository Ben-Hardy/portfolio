% Test script for my Gauss-Jordan Elimination function

%   Ben Hardy
%   MATH 211
%   Assignment 1

% Question 1 values
A1 = [2, 1 -1, -1; 0, 1, -2, 3; 1, 0, 1, -2; -1, 1, 3, 0];
b1 = [-1; -4; 3; 6];

disp("Q1 Output:");
gaussElimination(A1, b1)


% Question 2 values
A2 = [1, 0, 0, 2;0, 1, 0, 3; 0, 2, 1, 11; 0, 0, 1, 5];
b2 = [7; 11; 36; 14];

disp("Q2 Output:");
gaussElimination(A2, b2)


% Question 3 Values
A3 = [1, 0, 0, -1; 0, 1, 0, -2; 0, 0, 1, 3; 0, 1, 1, 5];
b3 = [0; 4; 0; 5];

disp("Q3 Output:");
gaussElimination(A3, b3)


% Bonus! A system with no solutions
Ab = [1, 0, 0, 2;0, 1, 0, 3; 0, 2, 1, 11; 0, 0, 1, 5];
bb = [7; 11; 36; 11];

disp("Bonus Output:");
gaussElimination(Ab, bb)


