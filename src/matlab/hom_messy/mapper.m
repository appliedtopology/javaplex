function [adja, nodeInfo, levelIdx] = mapper(d, filter, resolution, overlap, magicFudge)
% [adja, nodeInfo, levelIdx] = mapper(d, filter, resolution, overlap, magicFudge)
%
% INPUTS
%  d: Distance matrix
%  filter: The filter function used to decompose the space
%  resolution: Defines the length of each interval
%              length  = (max(filter) - min(filter))*resolution
%              Default : 1/5
%  overlap : The % overlap between successive intervals
%            Default : 50%
%  magicFudge: The number of histograms to divide each homology plot into
%              Increase this number if you expect to see more clusters
%              and decrease it if you are seeing more clusters.
%              Default : 10
% OUTPUTS
%  adja: The adjacency matrix of the output graph.
%  nodeInfo: Cell array with some information about each node of the output
%            graph.
%       nodeInfo{i}.level : The index of the interval to which this
%                           node belongs.
%       nodeInfo{i}.filter : The maximum filter value of all the points
%                           belonging to this node's corresponding cluster.
%       nodeInfo{i}.set : The set of all points belonging to this node's
%                         cluster.
%  levelIdx: Cell array such that
%       levelIdx{i} contains the list of all output nodes belonging to 
%       each subinterval of the filter.

%    BEGIN COPYRIGHT NOTICE
%
%    Mapper code -- (c) 2007-2009 Gurjeet Singh
%
%    This code is provided as is, with no guarantees except that 
%    bugs are almost surely present.  Published reports of research 
%    using this code (or a modified version) should cite the 
%    article that describes the algorithm: 
%
%      G. Singh, F. Memoli, G. Carlsson (2007).  Topological Methods for 
%      the Analysis of High Dimensional Data Sets and 3D Object 
%      Recognition,? Point Based Graphics 2007, Prague, September 2007.
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

%% Check input and set defaults
if nargin < 2
    error('Too few input arguments');
elseif nargin < 3
    % Set resolution to 1/5
    resolution = 1/5;
    % Set overlap to 50%
    overlap = 50;
    % Set magicFudge to 10
    magicFudge = 10;
elseif nargin < 4
    % Set overlap to 50%
    overlap = 50;
    % Set magicFudge to 10
    magicFudge = 10;
elseif nargin < 5
    % Set magicFudge to 10
    magicFudge = 10;
end

if(resolution >= 1 || resolution <=0)
    error('Resolution must be between (0,1)');
end

if(overlap <=0 || overlap >= 100)
    error('Overlap must be between (0,100)');
end

if(magicFudge < 0)
    error('magicFudge must be >0');
end

step = (max(filter) - min(filter))*resolution;
numGraphNodes = 0;
level = 1;
nodeInfo = cell(0);
levelIdx = cell(0);
adja = [];

disp(sprintf('Mapper : Filter Range [%0.2f-%0.2f]', min(filter), max(filter)));
disp(sprintf('Mapper : Interval Length : %0.2f', step));
disp(sprintf('Mapper : Overlap : %0.2f', overlap));
disp(sprintf('Mapper : magicFudge : %0.2f', magicFudge));
disp('');

for i=min(filter):step*(1-overlap/100):max(filter)
    disp(sprintf('Mapper : Filter Indices from range [%0.2f-%0.2f]', i, i+step));
    %% Select points in this filter range
    idx = find(filter >= i & filter <= i + step);
    %% If we have enough points, grow the graph.
    if(max(size(idx)) > 5)
        numPoints = max(size(d(idx, idx)));
        [I, R, simp1, F] = returnBarCode_linkage_noplex(d(idx,idx));
        lens = I{1}(2,:) - I{1}(1,:);
        lens(find(lens == inf)) = R;
        numBin = hist(lens, magicFudge);
        z = find(numBin == 0);
        if(isempty(z))
            numClusts = 1;
        else
            numClusts = sum(numBin(z(1):length(numBin)));
        end
        [srt longIdx] = sort(lens, 'descend');
        longIdx = longIdx(1:numClusts);
        minDeathTime = min(I{1}(2,longIdx));
        if(minDeathTime == inf)
            si = sort(I{1}(2,:), 'descend');
            minDeathTime = si(2) + eps;
        end
        simp1 = simp1(:,find(F{1,2} < minDeathTime));
        G = sparse(numPoints, numPoints);
        [rws numSimps] = size(simp1);
        for iter=1:numSimps
            G(simp1(1,iter), simp1(2,iter)) = 1;
            G(simp1(2,iter), simp1(1,iter)) = 1;
        end

        %% Color the graph
        nodeColors = colorGraph(G);
        numColors = length(unique(nodeColors));
        numGraphNodes = max(size(nodeInfo));
        levelIdx{level} = [];
        for j=1:numColors
            levelIdx{level} = [levelIdx{level} (j+numGraphNodes)];
            nodeInfo{j+numGraphNodes}.level = level;
            nodeInfo{j+numGraphNodes}.fnval = i;
            nodeInfo{j+numGraphNodes}.set = idx(nodeColors == j);
            nodeInfo{j+numGraphNodes}.filter = max(filter(idx(nodeColors == j)));
        end
        if(level > 1)
            prevLvlIdx  = levelIdx{level-1};
            thisLvlIdx  = levelIdx{level};
            for i1 = 1:length(prevLvlIdx)
                for i2 = 1:length(thisLvlIdx)
                    a = prevLvlIdx(i1);
                    b = thisLvlIdx(i2);
                    if(length(intersect(nodeInfo{a}.set, nodeInfo{b}.set)) ~= 0)
                        adja(a, b) = 1;
                        adja(b, a) = 1;
                    end
                end
            end

        end
        level = level + 1;
%     elseif(max(size(idx)) >= 1)
%         numGraphNodes = max(size(nodeInfo));
%         levelIdx{level} = [(numGraphNodes+1)];
%         nodeInfo{1+numGraphNodes}.level = level + 1;
%         nodeInfo{1+numGraphNodes}.fnval = i;
%         nodeInfo{1+numGraphNodes}.set = idx;
%         nodeInfo{1+numGraphNodes}.filter = max(filter(idx));
%         if(level > 1)
%             prevLvlIdx  = levelIdx{level-1};
%             thisLvlIdx  = levelIdx{level};
%             for i1 = 1:length(prevLvlIdx)
%                 for i2 = 1:length(thisLvlIdx)
%                     a = prevLvlIdx(i1);
%                     b = thisLvlIdx(i2);
%                     if(length(intersect(nodeInfo{a}.set, nodeInfo{b}.set)) ~= 0)
%                         adja(a, b) = 1;
%                         adja(b, a) = 1;
%                     end
%                 end
%             end
% 
%         end
%         level = level + 1;
    end
end

disp('Mapper : Finished');
