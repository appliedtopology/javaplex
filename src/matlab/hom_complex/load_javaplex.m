% This script prepares the javaplex library for use

clc; clear all; close all;
clear import;

javaaddpath('./../javaplex.jar');
javaaddpath('./../plex-viewer.jar');
javaaddpath('./../jogl.jar');
java.lang.System.load('D:/Documents/Code/javaplex/src/matlab/jogl.dll');
%java.lang.System.load('D:/Documents/Code/javaplex/src/matlab/jogl.dll');

import edu.stanford.math.plex4.*;
import edu.stanford.math.plex_viewer.*;