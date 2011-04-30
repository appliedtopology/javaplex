function points = pointsTorus(gridLength)

% INPUT:
%   gridLength - the length of the grid
%
% OUTPUT:
%   points - a gridLength^2 x 4 matrix. Each row contains the x, y, z, and
%       w coordinates of a point from the gridLength x gridLength square 
%       grid on the 2D unit torus in 4D, plus noise.
%
% henrya@math.stanford.edu

% CONTSTANTS:
NOISE_COEFFICIENT = 0.25; % A scalar which determines how much noise is added.

vectConst = (2*pi/gridLength) * reshape(repmat((1:gridLength), gridLength, 1), gridLength^2, 1);
vectIncr  = (2*pi/gridLength) * reshape(repmat((1:gridLength), 1, gridLength), gridLength^2, 1);
points = [cos(vectConst), sin(vectConst), cos(vectIncr), sin(vectIncr)] + NOISE_COEFFICIENT*rand(gridLength^2,4);