% writeDotFile(fileName, G, colorParam, sizeParam, extraLabel, nodeLabels)
% writeDotFile   writes a graph represented by a binary adjacency matrix 
%                in the DOT format for use with Graphviz.
% 
% INPUT:
% fileName - string containing location of output file
% G - Adjacency matrix for the UNDIRECED graph
% colorParam - A numNodes x 1 array dictating the color of each node
% sizeParam  - A numNodes x 1 array dictating the size of each node
% extraLabel - A cell array of string labels pasted at the 
%              bottom left of the graph. OPTIONAL
% nodeLabels - A cell array of size numNodes x 1 of label of each node.
%              OPTIONAL
%
% OUTPUT:
% The graph is written in the graphviz format at the location of fileName

%    BEGIN COPYRIGHT NOTICE
%
%    Mapper code -- (c) 2007-2009 Gurjeet Singh
%
%    This code is provided as is, with no guarantees except that 
%    bugs are almost surely present.
%
%    Comments and bug reports are welcome.  Email to 
%    gurjeet@stanford.edu. 
%    I would also appreciate hearing about how you used this code, 
%    improvements that you have made to it, or translations into other
%    languages.    
%
%    You are free to modify, extend or distribute this code, as long 
%    as this copyright notice is included whole and unchanged.  
%
%    END COPYRIGHT NOTICE


function writeDotFile(fileName, G, colorParam, sizeParam, extraLabel, nodeLabels)

nlPresent = 1;

if(nargin < 4)
    error('fileName, G, colorParam, sizeParam are required..');
elseif(nargin < 5)
    extraLabel{1} = '';
    nlPresent = 0;
elseif(nargin < 6)
    nlPresent = 0;
end

numNodes = max(size(G));
cparam = colorParam;
sparam = sizeParam;

if(nlPresent == 1)
    if(length(nodeLabels) ~= numNodes)
        error('Number of elements in nodeLables must be equal to the number of nodes.');
    end
end


%% color coordination
figure;
colorMat = colormap();
close;
numColors = length(colorMat);
colx = min(colorParam):(max(colorParam)-min(colorParam))/(numColors - 1):max(colorParam);
rgbColors = interp1(colx, colorMat, colorParam);
hsvColors = rgb2hsv(rgbColors);

%% Size thingy
sizeParam = 0.1 + sizeParam/max(sizeParam);


%% Print the graph to string
str = sprintf('Graph "G" {\n');

for i=1:numNodes
    if(nlPresent == 0)
        str = sprintf('%s node%d [label="%d", color="%0.15f %0.15f %0.15f",style=filled, shape=circle, width=%0.2f];\n', str, i, (i), hsvColors(i,1), hsvColors(i,2), hsvColors(i,3), sizeParam(i));
    else
        str = sprintf('%s node%d [label="%s", color="%0.15f %0.15f %0.15f",style=filled, shape=circle, width=%0.2f];\n', str, i, nodeLabels{i}, hsvColors(i,1), hsvColors(i,2), hsvColors(i,3), sizeParam(i));
    end
end

for i=1:numNodes
    connNodes = find(G(i,:) == 1);
    G(connNodes, i) = 0;
    for j=1:length(connNodes)
        str = sprintf('%s node%d -- node%d ;\n', str, i, connNodes(j));
    end
end

str = sprintf('%s label = "File Name    = %s\\lFilter Range = [%.2f-%.2f]\\lSize Range   = [%.2f-%.2f]\\l',...
      str, fileName, min(cparam), max(cparam), min(sparam), max(sparam));
  
for i=1:length(extraLabel)
    str = sprintf('%s%s\\l', str, extraLabel{i});
end
str = sprintf('%s";\n', str);

str = sprintf('%s labelloc="b";\nlabeljust="l";\n', str);

str = sprintf('%s center = 1;\n rankdir=LR;\n overlap=scale;\n', str);

str = sprintf('%s }', str);

%% Save the graph to file
fid = fopen(fileName, 'w');
fprintf(fid, '%s', str);
fclose(fid);
