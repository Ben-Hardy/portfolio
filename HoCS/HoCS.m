% Ben Hardy
% 
% 
% CMPT 487
% Assignment 4 Question 2

function [  resHists, resimg ] = HoCS( I, minScale, maxScale, increment, numBins )
% HoCS takes a binary image and calculates the histogram of curvature for
% it.
%   Inputs:
%   I = the image we are examining
%   minScale : the minimum radius/scale to use
%   maxScale : the maximum radius/scale to use
%   increment : the size of incremental scales between min and max
%   numBins : the number of bins to use for our histograms
    
    if size(I,3) ~= 1
        error('attempted to input image of incorrect dimensions');
    end
    
    if minScale > maxScale
        error('scale size mismatch');
    end
    
    if numBins < 1
        error('must have at least one bin.');
    end
    
    if increment < 1
        error('increment is too small. Program will never finish.');
    end
    
    % We get the number of radii to be examined so we can preallocate the
    % histogram array
    width = size(minScale:increment:maxScale);
    
    % pad the matrix with the largest potential scale size number of zeros
    % so that we don't end up trying to access negative values
    I = padarray(I, maxScale);
    
    % the radii that we will be examining. We will store them in a vector
    % for easy access
    rads = minScale:increment:maxScale;

    % preallocate our histogram array to be 
    resHists = zeros(width(2), numBins);
    
    % get the boundaries
    [bounds, thresh] = bwboundaries(I);
    
    resimg = zeros(width(2), size(bounds{1},1));
    
    % for the number of radii
    for q=1:width(2) 
        % calculate the circle of the current scale
        circ = strel('disk', rads(q), 0);
        cn = double(circ.getnhood); % circular neighbourhood
        ca = bwarea(cn); % the area of the circular neighbourhood
        for p=1:size(bounds{1},1)
            x = bounds{1}(p,1);
            y = bounds{1}(p,2);
            
            % get the neighbourhood of radius rad(i) from around the
            % current pixel
            nhood = I(x-rads(q):x+rads(q), y-rads(q):y+rads(q));

            % now we will find overlap in a sketchy, hacky fashion I figured
            % out. Doing this cut my runtime from 16 seconds to 5 on my desktop
            
            % add the circular neighbourhood to the pixel neighbourhood.
            % This will result in some elements equalling 2. res now
            % contains 0s,1s, and 2s
            res = cn + nhood;
            
            % subtract everything by 1. Now the matrix will contain -1s,0s,
            % and 1s, where the 1s are the overlapping pixels
            res = res -1;
            
            % convert it to unsigned. Now everything that was a 2, which
            % meant it overlapped, is a 1, and everything else is 0.
            res = uint8(res);
            
            % calculate the area of the result matrix
            resarea = bwarea(res);
            
            % calculate kappa
            kappa = resarea/ca;
            
            % the curvature image is simply the kappa value for each point.
            resimg(q,p) = kappa;
            
            % binval is used to figure out which bin should be incremented.
            binval = round(kappa * numBins);
            binval = binval + 1;
            
            % use that binval and increment the bin
            resHists(q, binval) = resHists(q, binval) + 1;
        end

    end


end

