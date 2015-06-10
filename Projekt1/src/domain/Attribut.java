/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package domain;

/**
 * Only the listed attributs are stored
 */
public enum Attribut {
	pos_x,
	pos_y,
	color,
	name,
	weight,
	string,
    NUMBER,
    VISITED,
    DISCOVERY,
    // for dijkstra:
    DISTANCE,
    PQLOCATOR,    
    //for kruskal 
    MSF,
    CLUSTER 

}
