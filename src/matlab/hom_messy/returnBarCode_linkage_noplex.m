function [I, R, Ca, F] = returnBarCode_linkage_noplex(dist)
%function [I, R, Ca, F] = returnBarCode_linkage_noplex(dist)
%Returns 0 dimensional homology, max(max(dist)), complex, F

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

Z = linkage(dist(tril(true(length(dist)),-1))');
I = cell(1);
R = max(max(dist));
N = length(dist);
I{1}(1,:) = zeros(1, length(Z(:,3)) +1);
I{1}(2,1:length(Z(:,3))) = Z(:,3)';
I{1}(2, length(Z(:,3)) + 1) = inf;

D = triu(dist <= R);
[II, JJ] = find(D);
Ca = zeros(2, length(II));
Ca(1,:) = II(:)';
Ca(2,:) = JJ(:)';
ind = sub2ind([N N], Ca(1,:), Ca(2,:));

F = {zeros(1,N), dist(ind)};