function [sol] = gaussElimination (A,b)
% Gaussian Elimination 
%   Ben Hardy
%   MATH 211
%   Assignment 1

%   A program that uses Gaussian Elimination to solve nxn linear equations

% Inputs:
% A: a nxn matrix containing the a1,..., an values in each row for a system
% of linear equations.
% For example:
% the following system:
% -2x1 + 3x2 - x3 + 2x4 = 1
% x1 + 2x2 - x3 + 5x4 = 4
% -2x1 - x2 + x3 - x4 = 6
% -3x1 + 4x2 + 2x3 - 6x4 = 3
%
% would be input as:
% [-2, 3, -1, 2; 
%   1, 2, -1, 5;
%  -2, -1, 1, -1;
%  -3, 4, 2, -6]
%
% b: a nx1 vector containing the b values from the linear equation.
% For example:
% Our b values from the above example would give us the following as input:
% [1; 4; 6; 3]

% Output:
% Returns a column vector containing the x1...xn values if they exist.
% Returns a column vector of NaNs otherwise

% Check formatting to make sure matrix A and vector b are put in correctly!
if (size(A,1) ~= size(A,2))
   disp("A is not of the correct format!!!")
end

if (size(b, 1) ~= size(A,1) || size(b, 2) ~= 1)
    disp("b is not of the correct format!!!")
end   

len = size(A,1);

% concatenate A and b together to make it easier to keep track of what we
% are working on. One matrix is easier to keep track of than two
Ab = horzcat(A,b);

% maxVal stores the maximum value for a given column
maxVal = -inf;
% maxIdx stores the row index in which the maximum value was stored
maxIdx = 0;

% flag that tells if a solution has been found. Only changes if there are
% either no solutions or infinite solutions
solutionFound = true;

% temporary 1xn+1 vector to store rows that are being swapped, if swapping
% needs to occur
tempVec = zeros(1,len+1);

% for each row in the matrix
for j=1:len
    % for each column in the matrix, find the maximum value, so it can be
    % moved to row j if need be. Please note the bij column is skipped
    for i=j:len % from j to len because we skip above rows
        if (abs(Ab(i, j)) > maxVal)
            maxIdx = i;
            maxVal = Ab(i, j);
        end
    end
    
    % if we didn't find a bigger value than ajj, set our maxVal to that and
    % the row index to row j. If our pivot is 0, it will get dealt with shortly
    if (maxIdx == 0)
        maxIdx = j;
        maxVal = Ab(j,j);
    end
    
    % if the row with the column max value is not already where it needs to
    % be, swap the rows so it is!
    if (maxIdx ~= j)
        tempVec(1,:) = Ab(maxIdx, :);
        Ab(maxIdx,:) = Ab(j,:);
        Ab(j,:) = tempVec(1,:);
    end
    
    % If, after everything has been swapped and rearranged, we still end up
    % with 0 as our pivot and non-zero values above our pivot, then there
    % must be infinite solutions!
    % Likewise, if there is only the bij value left in the row and no aij,
    % there will be no solution since nothing can't equal something!
    if (Ab(j,j) == 0 && Ab(j,len + 1) ~= 0 && sum(Ab(:, j)) ~= 0) 
        disp("There are no solutions for this set of linear equations!");
        solutionFound = false;
        break;
    elseif (Ab(j,j) == 0 && sum(Ab(1:len,j) == 0) && sum(Ab(:, j)) ~= 0)
        disp("There are infinite solutions for this set of linear equations!");
        solutionFound = false;
        break;
    else
        % divide the pivot row by the pivot!
        Ab(j,:) = Ab(j,:)/Ab(j,j);
        
        % clear out elements above and below the pivot column
        for i=1:len
            if (i ~= j) % this prevents row j from subtracting itself
                if (Ab(i,j) ~= 0)
                        mulVal = Ab(i,j);
                    for k=1:len+1
                        Ab(i,k) = -1 * Ab(j,k) * mulVal + Ab(i,k);
                    end
                end
            end
        end
    end
    
    maxIdx = 0;
    maxVal = -inf;
    
end

% Results time!
disp("Final resulting Augmented matrix:")
disp(Ab);

% if solution was found, set return value.
if (solutionFound)
    sol = Ab(:,len+1);
else
    sol = [NaN; NaN; NaN; NaN];
end
end



