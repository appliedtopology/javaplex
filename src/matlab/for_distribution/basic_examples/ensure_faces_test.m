clc; clear; close all;
import edu.stanford.math.plex4.*;

stream = api.Plex4.createExplicitSimplexStream();
for i=1:4,
    stream.addVertex(i,0);
end
stream.addElement([1,2,3,4], 2);
stream.addElement([1,2,3], 1)
stream.ensureAllFaces();
stream.finalizeStream();

iterator = stream.iterator();

while (iterator.hasNext())
  % the next line will print the current simplex
  simplex = iterator.next();
  % here you can do whatever is needed with the simplex
  filtration_value = stream.getFiltrationIndex(simplex);
  display(sprintf('%s: %d', char(simplex.toString()), filtration_value));
end
