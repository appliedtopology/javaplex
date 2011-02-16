function op = objectivize(f)

    op = @(varargin)objective_impl(varargin{:} );

    function [v, g] = objective_impl(x, t )
        v = f(x);
        g = gradient(f, x);
    end

end