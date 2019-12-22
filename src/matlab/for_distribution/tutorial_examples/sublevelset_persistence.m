function intervals = sublevelset_persistence(M,filename)

% INPUT:
%   M - a matrix of size n x m
%
% OUTPUT:
%   This function computes the sublevelset persistent homology of M in
%   homological dimension 0 and 1.

import edu.stanford.math.plex4.*;

[n,m]=size(M);

max_dimension=2;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream(max(max(M)));
% The parameter max(max(M)) is supposed to be an upper bound on all
% filtration values; if it is not added then Javaplex will round all
% filtration values to be integers.

% add vertices
for i=1:n
    for j=1:m
        stream.addVertex(ijNum(i,j,n),M(i,j));
    end
end

% add horizontal edges
for i=1:n
    for j=1:m-1
        stream.addElement([ijNum(i,j,n),ijNum(i,j+1,n)],max(M(i,j),M(i,j+1)));
    end
end

% add vertical edges
for i=1:n-1
    for j=1:m
        stream.addElement([ijNum(i,j,n),ijNum(i+1,j,n)],max(M(i,j),M(i+1,j)));
    end
end

% add triangles and diagonal edges
for i=1:n-1
    for j=1:m-1
        filtValue=max([M(i,j),M(i,j+1),M(i+1,j),M(i+1,j+1)]);
        stream.addElement([ijNum(i,j,n),ijNum(i+1,j+1,n)],filtValue);
        stream.addElement([ijNum(i,j,n),ijNum(i,j+1,n),ijNum(i+1,j+1,n)],filtValue);
        stream.addElement([ijNum(i,j,n),ijNum(i+1,j,n),ijNum(i+1,j+1,n)],filtValue);
    end
end

%iterator = stream.iterator();
%while (iterator.hasNext())
%  simplex = iterator.next()
%  filtration_value = stream.getFiltrationValue(simplex);
%  disp(filtration_value)
%end

stream.finalizeStream();
% num_simplimces = stream.getSize();
persistence = api.Plex4.getModularSimplicialAlgorithm(max_dimension, 2);
intervals = persistence.computeIntervals(stream);
% create the barcode plots
options.filename = filename;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

end

% The input to this function are the indices i,j in a matrix, and the
% number of rows in the matrix n.
% The output is the corresponding vertex number from 0 to (n+1)*(m+1)-1.
% Vertices are counted from 0 to n*m-1, with the first column
% labeled from 0 to n-1, where i ranges from 1 to the number of rows n, and
% j ranges from 1 to the number of columns m.
function vertexNumber=ijNum(i,j,n)

vertexNumber=(i-1)+(j-1)*n;

end