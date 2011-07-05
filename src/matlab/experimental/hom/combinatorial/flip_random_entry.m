function r = flip_random_entry(x)
    r = x;
    entry = ceil(rand * length(x));
    r(entry) = 1 - r(entry);
end