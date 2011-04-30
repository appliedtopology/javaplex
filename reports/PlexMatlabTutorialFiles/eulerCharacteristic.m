function  eulerCharacteristic(explicitStream)

% INPUT:
%   explicitStream - an instance of the JPlex Java class ExplicitStream.
%
% OUTPUT:
%   This function prints two Euler characteristic calculations: using the
%   alternating sum of cells, and using the alternating sum of Betti
%   numbers.
%
% henrya@math.stanford.edu

import edu.stanford.math.plex.*;
eulerCharCell = 0;
eulerCharBetti = 0;
cellStr = [];
bettiStr = [];
bettiVect = betti2vect(Plex.FilterInfinite(Plex.Persistence.computeIntervals(explicitStream)));
bettiVect = [bettiVect, zeros(1, explicitStream.maxDimension-length(bettiVect))];
for i=0:explicitStream.maxDimension-1
    numCells = length(explicitStream.dump(i).F);
    eulerCharCell = eulerCharCell + (-1)^i * numCells;
    eulerCharBetti = eulerCharBetti + (-1)^i * bettiVect(i+1);
    if mod(i,2)==0
        cellStr = [cellStr, ' + ', int2str(numCells)];
        bettiStr = [bettiStr, ' + ', int2str(bettiVect(i+1))];
    else
        cellStr = [cellStr, ' - ', int2str(numCells)];
        bettiStr = [bettiStr, ' - ', int2str(bettiVect(i+1))];
    end 
end
disp(['The Euler characteristic is ', int2str(eulerCharCell), ' = ', cellStr(4:end), ', using the alternating sum of cells.'])
disp(['The Euler characteristic is ', int2str(eulerCharBetti), ' = ', bettiStr(4:end), ', using the alternating sum of Betti numbers.'])