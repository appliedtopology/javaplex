function [filename] = render_onscreen(stream, points)

    import edu.stanford.math.plex_viewer.*;

    Api.drawSimplexStream(stream, points);
end

