function changeBasisDct = dct(patchSize)

% INPUT:
%   patchSize - size of the square patches.
%
% OUTPUT:
%   changeBasisDct - patchSize^2 x patchSize^2-1 change-of-basis matrix.
%       Let p be a matrix of patches, with mean zero and Euclidean norm 
%       one, in the pixel basis. We switch to the DCT (discrete cosine 
%       transform) basis by multiplying by matrix changeBasisDct:
%           pDct = p * changeBasisDct;

changeBasisDct = zeros(patchSize^2,patchSize^2-1);
v = zeros(patchSize^2,1);
for i = 0:patchSize-1
    for j = 0:patchSize-1
        for k = 0:patchSize-1
            for m = 0:patchSize-1
                v(1+k+patchSize*m,1) = cos(pi/patchSize*(k+1/2)*i)*cos(pi/patchSize*(m+1/2)*j);
            end
        end
        if i~=0 || j~=0
            changeBasisDct(:,i+patchSize*j) = v/norm(v);
        end
    end
end