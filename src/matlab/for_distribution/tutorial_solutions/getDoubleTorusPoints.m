function points = getDoubleTorusPoints(numPoints, tolerance)

% INPUT:
%   numPoints - number of points
%   tolerance - tolerance level for accepting a point on the torus
%
% OUTPUT:
%   distances - numPoints x 3 matrix of random points on a double torus
%       embedded in R^3.

import edu.stanford.math.plex4.*;

points = zeros(numPoints, 3);
accepted = 0;
while (accepted < numPoints)
    x = 1.1*(2*rand(1) - 1);
    y = 0.7*(2*rand(1) - 1);
    z = 0.3*(2*rand(1) - 1);
    if (abs(((1-x^2)*x^2-y^2)^2 + z^2 - 0.045) < tolerance)
        accepted = accepted + 1;
        points(accepted, :) = [x, y, z];
    end
end