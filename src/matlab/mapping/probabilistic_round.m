function x = probabilistic_round(p)
    floor_p = floor(p);
    r = rand(size(p, 1), size(p, 2));
    x = (r < p - floor_p) .* (floor_p + 1) + (r >= p - floor_p) .* (floor_p);
end