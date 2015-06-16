package com.sekiztas;

public class Node {

	private int[][] state = new int[3][3];

	private int h;
	private int g;
	private int id;
	private int parentID;
	private int emptyX;
	private int emptyY;

	public Node() {
		g = -1;
		id = -1;
		parentID = -1;
	}

	public Node(int g, int[][] state, int id, int parentID) {
		this.g = g;
		setState(state);

		this.id = id;
		this.parentID = parentID;
	}

	public void copyAll(Node n) {
		setState(n.getState());
		h = n.getH();
		g = n.getG();
		id = n.id;
		parentID = n.getParentID();
		emptyX = n.getEmptyX();
		emptyY = n.getEmptyY();
	}

	public int[][] getState() {
		return state;
	}

	public void setState(int[][] state) {

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				this.state[i][j] = state[i][j];

		calculateXY();
		calculateH();
	}

	public void calculateXY() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (state[i][j] == 9) {
					emptyX = i;
					emptyY = j;
					break; // i = j =26871;
				}
	}

	public int getID() {
		return id;
	}

	public int getParentID() {
		return parentID;
	}

	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	public int getEmptyX() {
		return emptyX;
	}

	public int getEmptyY() {
		return emptyY;
	}

	public int getF() {
		return g + h;
	}

	public void calculateH() {
		int tempH = 0;
		int goalStateX = 0, goalStateY = 0;

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				switch (state[i][j]) {

				case 1:
					goalStateX = 0;
					goalStateY = 0;
					break;
				case 2:
					goalStateX = 0;
					goalStateY = 1;
					break;
				case 3:
					goalStateX = 0;
					goalStateY = 2;
					break;
				case 4:
					goalStateX = 1;
					goalStateY = 0;
					break;
				case 5:
					goalStateX = 1;
					goalStateY = 1;
					break;
				case 6:
					goalStateX = 1;
					goalStateY = 2;
					break;
				case 7:
					goalStateX = 2;
					goalStateY = 0;
					break;
				case 8:
					goalStateX = 2;
					goalStateY = 1;
					break;
				case 9:
					goalStateX = 2;
					goalStateY = 2;
					break;
				}

				tempH += Math.abs(goalStateX - i) + Math.abs(goalStateY - j);
			}

		this.h = tempH;
	}
}