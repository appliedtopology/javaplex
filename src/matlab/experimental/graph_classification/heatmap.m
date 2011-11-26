function [hImage, hText] = heatmap(mat, xlab, ylab, textmat, varargin)
% HEATMAP displays a matrix as a heatmap image
%
% USAGE:
% [hImage, hText] = heatmap(matrix, xlabels, ylabels, textmatrix, 'param', value, ...)
%
% INPUTS:
% * HEATMAP displays "matrix" as an image whose color intensities reflect
%   the magnitude of the values in "matrix". 
%
% * "xlabels" (and "ylabels") can be either a numeric vector or cell array
%   of strings that represent the columns (or rows) of the matrix. If either
%   is not specified or empty, no labels will be drawn. 
%
% * "textmat" can either be: 1, in which case the "matrix" values will be
%   displayed in each square, a format string, in which case the matrix
%   values will be displayed formatted according to the string specified, a numeric
%   matrix the size of "matrix", in which case those values will be displayed as
%   strings or a cell matrix of strings the size of "matrix", in which case each
%   string will be displayed. If not specified or empty, no text will be
%   displayed on the image
%
% OTHER PARAMETERS (oassed as parameter-value pairs)
% * 'Colormap': Either a matrix of size numLevels-by-3 representing the
%   colormap to be used or a string or function handle representing a
%   function that returns a colormap, example, 'jet', 'hsv' or @cool.
%   Non-standard colormaps available within HEATMAP include 'money' and 'red'.
%   By default, the current figure's colormap is used.
%
% * 'ColorLevels': The number of distinct levels in the colormap (default:
%   64). If more levels are specified than are present in the colormap, the
%   levels in the colormap are interpolated. If fewer are specified the
%   colormap is downsampled.
%
% * 'UseLogColorMap': A true/false value which, if true, specifies that the
%   intensities displayed should match the log of the "matrix" values. Use
%   this if the data is naturally on a logarithmic scale (default: false)
%
% * 'UseFigureColorMap': Specifies whether the figure's colormap should be
%   used. If false, the color intensities after applying the
%   specified/default colormap will be hardcoded, so that the image will be
%   independent of the figures colormap. If this option is true, the figure
%   colormap in the end will be replaced by specified/default colormap.
%   (default = true)
%
% * 'Parent': Handle to an axes object
%
% * 'TextColor': Either a color specification of all the text displayed on
%   the image or a string 'xor' which sets the EraseMode property of the text
%   objects to 'xor'. This will display all the text labels in a color that
%   contrasts its background.
%
% * 'FontSize': The initial fontSize of the text labels on the image. As
%   the image size is scaled the fontSize is shrunk appropriately.
%
% * 'ColorBar': Display colorbar. The corresponding value parameter should
%   be either logical 1 or 0 or a cell array of any additional parameters
%   you wish to pass to the colorbar function (such as location)
%
% * 'GridLines': Draw grid lines separating adjacent sections of the
%   heatmap. The value of the parameter is a LineStyle specification, for example,
%   :, -, -. or --. By default, no grid lines are drawn.
%
% OUTPUTS:
% * hImage: handle to the image object
% * hText : handle to the text objects (empty if no text labels are drawn)
%
% Notes:
% * The 'money' colormap displays a colormap where 0 values are mapped to
%   white, negative values displayed in varying degrees of red and positive
%   values in varying degrees of green
% * The 'red' colormap maps 0 values to white and higher values to red
%
% EXAMPLES:
% data = reshape(sort(randi(100,10)),10,10)-50;
% heatmap(data, cellstr(('A':'J')'), mean(data,2), '%0.0f%%',...
%         'Colormap', 'money', 'Colorbar', true, 'GridLines', ':',...
%         'TextColor', 'b')
% For detailed examples, see the associated document heatmap_examples.m

% Parse inputs -----------------------------------------------------------
p = inputParser;
p.addParamValue('Colormap',[]);
p.addParamValue('ColorLevels',[]);
p.addParamValue('TextColor',[0 0 0]);
p.addParamValue('UseFigureColorMap',true);
p.addParamValue('UseLogColorMap',false);
p.addParamValue('Parent',gca);
p.addParamValue('FontSize',[]);
p.addParamValue('Colorbar',[]);
p.addParamValue('GridLines','none');
p.parse(varargin{:});
cmap = p.Results.Colormap;
clevels = p.Results.ColorLevels;
usefigcmap = p.Results.UseFigureColorMap;
uselogcmap = p.Results.UseLogColorMap;
hAxes = p.Results.Parent;
hFig = get(hAxes,'Parent');
txtcol = p.Results.TextColor;
fontSize = p.Results.FontSize;
showcbar = p.Results.Colorbar;
gridlines = p.Results.GridLines;

% Figure out the colormap to use -----------------------------------------
if isempty(cmap)
    cmap = get(hFig,'Colormap');
    if isempty(clevels)
        clevels = size(cmap,1);
    else
        cmap = resamplecmap(cmap, clevels);
    end
elseif ischar(cmap) || isa(cmap,'function_handle')
    if isempty(clevels), clevels = 64; end
    if strcmp(cmap, 'money')
        cmap = money(mat, clevels);
    else
        cmap = feval(cmap,clevels);
    end
elseif iscell(cmap)
    cmap = feval(cmap{1}, cmap{2:end});
    clevels = size(cmap,1);
elseif isnumeric(cmap) && size(cmap,2) == 3
    clevels = size(cmap,1);
else
    error('Incorrect value for colormap parameter');
end % cmap is now a clevels-by-3 rgb vector
assert(clevels == size(cmap,1));

% Plot the image --------------------------------------------------
if uselogcmap
    cmap = resamplecmap(cmap, clevels, logspace(0,log10(clevels),clevels));
end
if usefigcmap
    set(hFig,'Colormap',cmap);
    hImage = imagesc(mat, 'Parent', hAxes);
else
    n = min(mat(:));
    x = max(mat(:));
    if x == n, x = n+1; end
    cdata = round((mat-n)/(x-n)*(clevels-1)+1);
    %cdata = ceil((mat-n)/(x-n)*clevels);
    cdata = reshape(cmap(cdata(:),:),[size(cdata) 3]);
    hImage = image(cdata, 'Parent', hAxes);
end

% Draw Grid Lines ----------------------------------------------------
if ~strcmp(gridlines,'none')
    xlim = get(hAxes,'XLim');
    ylim = get(hAxes,'YLim');
    for i = 1:diff(xlim)-1
        line('Parent',hAxes,'XData',[i i]+.5, 'YData', ylim, 'LineStyle', gridlines);
    end
    for i = 1:diff(ylim)-1
        line('Parent',hAxes,'XData',xlim, 'YData', [i i]+.5, 'LineStyle', gridlines);
    end
end

% Set axes labels ----------------------------------------------------
if nargin < 2 || isempty(xlab)
    set(hAxes,'XTick',[],'XTickLabel','');
else
    if isnumeric(xlab)
        xlab = arrayfun(@(x){num2str(x)},xlab);
    end
    set(hAxes,'XTick',1:length(xlab),'XTickLabel',xlab);
end
if nargin < 3 || isempty(ylab)
    set(hAxes,'YTick',[],'YTickLabel','');
else
    if isnumeric(ylab)
        ylab = arrayfun(@(x){num2str(x)},ylab);
    end
    set(hAxes,'YTick',1:length(ylab),'YTickLabel',ylab);
end

% Set text labels -----------------------------------------------------
if nargin >=4 && ~isempty(textmat)
    displaytext = textmat;
    if isscalar(textmat) && textmat
        textmat = arrayfun(@(x){num2str(x)},mat);
    elseif ischar(textmat)
        textmat = arrayfun(@(x){sprintf(textmat,x)},mat);
    elseif isnumeric(textmat) && numel(textmat)==numel(mat)
        textmat = arrayfun(@(x){num2str(x)},textmat);
    elseif ~iscell(textmat) && numel(textmat)==numel(mat)
        error('texmat is incorrectly specified');
    end
    if ischar(txtcol) && strcmp(txtcol,'xor')
        colorprop = 'EraseMode';
    else
        colorprop = 'Color';
    end
    
    if isempty(fontSize)
        fontSize = getBestFontSize(hAxes);
    end
    [xpos,ypos] = meshgrid(1:size(mat,2),1:size(mat,1));
    hText = text(xpos(:),ypos(:),textmat(:),'FontSize',fontSize,...
        'HorizontalAlignment','center', colorprop, txtcol);
       
    % Set up listeners to handle appropriate zooming
    factor = fontSize/getBestFontSize(hAxes);
    addlistener(hAxes,{'XLim','YLim'},'PostSet',@(obj,evdata)resizeText);
    addlistener(hFig,'Resize',@(obj,evdata)resizeText);
else
    hText = [];
    displaytext = [];
end
    function resizeText
        if ~isempty(hText) && ishandle(hText(1))
            fs = factor*getBestFontSize(hAxes);
            if fs > 0
                set(hText,'fontsize',fs,'visible','on');
            else
                set(hText,'visible','off');
            end
        end
    end

% Add a colorbar if necessary -------------------------------------------
if ~isempty(showcbar)
    if iscell(showcbar)
        c = colorbar(showcbar{:});
    else
        c = colorbar;
    end
    if ~usefigcmap
        d = findobj(get(c,'Children'),'Tag','TMW_COLORBAR'); % Image
        %cdata = get(d,'Cdata');
        %ytick = get(d,'YTick');
        %ytl = get(g,'YTickLabel');
        set(d,'CData',reshape(cmap,[clevels 1 3]))
        set(c,'YLim',[1 clevels])
        ytick = get(c,'YTick');
        ytick = (ytick-1)*(x-n)/(clevels-1)+n;
        if ischar(displaytext)
            yticklabel = arrayfun(@(x){sprintf(displaytext,x)},ytick);
        else
            yticklabel = num2str(ytick(:));
        end
        set(c,'YTickLabel',yticklabel);
    end
end
end


function cmap = red(levels) %#ok<*DEFNU>
r = ones(levels, 1);
g = linspace(1, 0, levels)'; 
cmap = [r g g];
end

function cmap = money(data, clevels)
% Function to make the heatmap have the green, white and red effect
n = min(data(:));
x = max(data(:));
if x == n, x = n+1; end
zeroInd = round(-n/(x-n)*(clevels-1)+1);
if zeroInd <= 1 % Just green
    b = interp1([1 clevels], [1 0], 1:clevels);
    g = interp1([1 clevels], [1 1], 1:clevels);
    r = interp1([1 clevels], [1 0], 1:clevels);
elseif zeroInd >= clevels, % Just red
    b = interp1([1 clevels], [0 1], 1:clevels);
    g = interp1([1 clevels], [0 1], 1:clevels);
    r = interp1([1 clevels], [1 1], 1:clevels);
else
    b = interp1([1 zeroInd clevels], [0 1 0], 1:clevels); 
    g = interp1([1 zeroInd clevels], [0 1 1], 1:clevels);
    r = interp1([1 zeroInd clevels], [1 1 0], 1:clevels);
end

cmap = [r' g' b'];
end

function cmap = resamplecmap(cmap, clevels, xi)
% This function resamples a colormap by interpolation or decimation
t = cmap;
if nargin < 3
    xi = linspace(1,clevels,size(t,1)); 
end
xi([1 end]) = [1 clevels]; % These need to be exact for the interpolation to 
% work and we don't want machine precision messing it up
cmap = [interp1(xi, t(:,1), 1:clevels);...
        interp1(xi, t(:,2), 1:clevels);...
        interp1(xi, t(:,3), 1:clevels)]';
end



function fs = getBestFontSize(imAxes)
% Try to keep font size reasonable for text
hFig = get(imAxes,'Parent');
magicNumber = 80;
nrows = diff(get(imAxes,'YLim'));
ncols = diff(get(imAxes,'XLim'));
if ncols < magicNumber && nrows < magicNumber
    ratio = max(get(hFig,'Position').*[0 0 0 1])/max(nrows,ncols);
elseif ncols < magicNumber
    ratio = max(get(hFig,'Position').*[0 0 0 1])/ncols;
elseif nrows < magicNumber
    ratio = max(get(hFig,'Position').*[0 0 0 1])/nrows;
else
    ratio = 1;
end
fs = min(9,ceil(ratio/4));    % the gold formula
if fs < 4
    fs = 0;
end
end