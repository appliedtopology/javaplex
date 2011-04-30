function points = pointsFigure8(numPoints)

% INPUT:
%   numPoints - number of points
%
% OUTPUT:
%   points - a numpoints x 2 matrix. Each row contains the x and y
%       coordinates of a point randomly sampled from a figure eight, which
%       is the union of two unit circles centered at (0,1) and (0,-1), plus
%       noise.
%
% henrya@math.stanford.edu

% CONTSTANTS:
NOISE_COEFFICIENT = 0.25; % A scalar which determines how much noise is added.

angles = 2*pi*rand(numPoints,1);
yPlusMinus = 2*(rand(numPoints,1)<0.5)-1; %each entry of yPlusMinus is -1 or 1
points = [cos(angles), sin(angles)+yPlusMinus] + NOISE_COEFFICIENT*rand(numPoints,2);
