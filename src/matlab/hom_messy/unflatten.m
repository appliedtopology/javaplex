function [i, j] = unflatten(index, I, J)
    %index = J * (i - 1) + j;
    i = (index / J) + 1;
    j = mod(index, J);
end