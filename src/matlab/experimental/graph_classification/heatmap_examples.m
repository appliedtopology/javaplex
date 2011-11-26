clc; clear; close all;

%% Single heatmap with default values
data = rand(3,4)*100;
heatmap(data);
%% Labelled heatmap
% Its easy to add different labels to the heatmap.
heatmap(data, [1.1 1.2 1.3 1.6], {'A','B','C'}, true);
colormap cool
%% 
% In the above figure the text in each square is the numerical value of the
% data. This can be formatted using a specific format string. 
heatmap(data, [1.1 1.2 1.3 1.6], {'A','B','C'}, '%0.2f m/s');
%%
% Or, a completely different matrix of data can be shown as strings
% enabling you to show an additional dimension of the data (for example, a
% categorial variable)
gt50 = repmat({''},size(data));
gt50(data>50) = {'>50'};
heatmap(data, [], [], gt50);

%% Setting other properties
% These are examples of changing the text color, grid lines, font size and
% specifying a colorbar
figure;
heatmap(data, [], [], gt50, 'TextColor', 'y', 'GridLines', '-');
figure;
heatmap(data, [], [], gt50, 'TextColor', 'xor', 'FontSize', 16);
figure;
heatmap(data, [], [], gt50, 'TextColor', 'xor', 'FontSize', 16, 'Colorbar', {'SouthOutside'});

%% Colormaps
% The benefit of not using the figure colormap is that you can have
% multiple heatmaps in a figure with different colormaps. Colormaps may be
% specified as function handles, strings and numerical matrices of RGB color
% levels. 
subplot(1,2,1)
heatmap(data, [], [], gt50, 'Colormap', @copper, 'UseFigureColormap', false);
subplot(1,2,2)
heatmap(data, [], [], '%0.2f', 'Colormap', 'cool', 'UseFigureColormap', false);
%%
% The heatmap function makes two more colormaps available: the money
% colormap and the red colormap.
figure;
heatmap(data-50, [], [], 1, 'Colormap', 'money', 'Colorbar', true);
%%
% Colormaps may also be specified on a linear or logarithmic scale, enabling
% you to choose the colormap scale that is more appropriate for your data
data2 = exprnd(1,100);
figure;
heatmap(data2,[],[],[],'Colormap',@cool,'Colorbar',true);
title('Linear Colormap');
figure;
heatmap(data2,[],[],[],'Colormap',@cool,'UseLogColormap',true,'Colorbar',true);
title('Logarithmic Colormap');

