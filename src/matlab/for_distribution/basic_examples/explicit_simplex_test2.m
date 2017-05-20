% This script demonstrates an example of the ExplicitSimplexStream class

%% Manual construction

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get a new ExplicitSimplexStream
stream = api.Plex4.createExplicitSimplexStream(10);

% construct a triangle
stream.addVertex(0, 0);
stream.addVertex(1, 1);
stream.addVertex(2, 2);
stream.addElement([0, 1], 3.88);
stream.addElement([0, 2], 4.12);
stream.addElement([1, 2], 5);

% print out the total number of simplices in the complex
size = stream.getSize()



iterator = stream.iterator();

while (iterator.hasNext())
  % the next line will print the current simplex
  simplex = iterator.next()
  % here you can do whatever is needed with the simplex
  filtration_value = stream.getFiltrationValue(simplex)
end






