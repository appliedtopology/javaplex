function d = circle_distance(a, b)
    d = b - a;
    while d < 0
        d = d + 2 * pi;
    end
end