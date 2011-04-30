function vector = betti2vect(betti)

% INPUT:
%   betti - An instance of the JPlex Java class Plex.BettiNumbers.
%
% OUTPUT:
%   vector - a vector containing the betti numbers. Entry i contains the
%       Betti number in dimension i-1.
%
% henrya@math.stanford.edu

import edu.stanford.math.plex.*;
string = char(betti);
vector = str2num(string(4:length(string)-1));