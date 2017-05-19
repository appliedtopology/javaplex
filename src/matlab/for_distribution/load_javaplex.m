% This script prepares the javaplex library for use

clc; clear all; close all;
% clear import;

javaaddpath('./lib/javaplex.jar');
import edu.stanford.math.plex4.*;

javaaddpath('./lib/plex-viewer.jar');
import edu.stanford.math.plex_viewer.*;

cd './utility';
addpath(pwd);
cd '..';


