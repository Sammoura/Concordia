package model;

public enum Status {
	LOCKED,				// Prerequisites unmade
	UNLOCKED,			// Prerequisites made, could be started any time
	IN_PROGRESS,		// Started but has not been finished yet 
	FINISHED
}