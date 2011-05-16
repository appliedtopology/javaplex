function  [nodeColors] = colorGraph(G)
% [nodeColors] = colorGraph(G)
% INPUT :
% G : Adjacency Matrix, '1' indicates a link.
%
% OUTPUT:
% nodeColors: Color of each node

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

[numNodes, numNodes] = size(G);
nodeColors = zeros(1, numNodes);
color = 1;
%start searching from node 1...
q = [1];
nodeColors(q(1)) = color;
%gr = zeros(size(G));
while(length(find(nodeColors == 0)) ~= 0)
    while(length(q) ~= 0)
        currnode = q(1);
        q = q(2:length(q));
        connectednodes = find(G(currnode,:)==1);
        for i=1:length(connectednodes)
            if(nodeColors(connectednodes(i)) == 0)
                %gr(currnode, connectednodes(i)) = color;
                %gr(connectednodes(i), currnode) = color;
                nodeColors(connectednodes(i)) = color;
                q = [q connectednodes(i)];
            end
            %imagesc(gr);
        end
    end
    colorless = find(nodeColors == 0);
    if(length(colorless) > 0)
        q = [colorless(1)];
        color = color + 1;
        nodeColors(q(1)) = color;
    end
end