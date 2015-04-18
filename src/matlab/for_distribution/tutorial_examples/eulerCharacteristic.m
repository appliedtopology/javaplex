function  eulerCharacteristic(stream, max_dimension)

% INPUT:
%   stream - an instance of the class AbstractFilteredStream<Simplex>.
%   max_dimension - the maximal dimension of a simplex in stream.
%
% OUTPUT:
%   This function prints two Euler characteristic calculations: the first
%   using the alternating sum of cells, and the second using the 
%   alternating sum of Betti numbers.

import edu.stanford.math.plex4.*;

persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension + 1, 2);

intervals = persistence.computeIntervals(stream);
infinite_barcodes = intervals.getInfiniteIntervals();

betti_sequence = infinite_barcodes.getBettiSequence();

eulerCharCell = 0;
eulerCharBetti = 0;
cellStr = [];
bettiStr = [];

for i = 0:(length(betti_sequence) - 1)
    eulerCharBetti = eulerCharBetti + (-1)^(i) * betti_sequence(i + 1);
    
    if mod(i,2)==0
        bettiStr = [bettiStr, ' + ', int2str(betti_sequence(i + 1))];
    else
        bettiStr = [bettiStr, ' - ', int2str(betti_sequence(i + 1))];
    end 
end

for i = 0:(max_dimension)
    skeleton_size = streams.utility.StreamUtility.getSkeletonSize(stream, i);
    eulerCharCell = eulerCharCell + (-1)^(i) * skeleton_size;
    
    if mod(i,2)==0
        cellStr = [cellStr, ' + ', int2str(skeleton_size)];
    else
        cellStr = [cellStr, ' - ', int2str(skeleton_size)];
    end 
end

disp(['The Euler characteristic is ', int2str(eulerCharCell), ' = ', cellStr(4:end), ', using the alternating sum of cells.'])
disp(['The Euler characteristic is ', int2str(eulerCharBetti), ' = ', bettiStr(4:end), ', using the alternating sum of Betti numbers.'])