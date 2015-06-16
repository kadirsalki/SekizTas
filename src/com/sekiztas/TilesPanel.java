package com.sekiztas;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Handler;
import android.view.MotionEvent;
import android.webkit.WebView.FindListener;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TilesPanel implements ActionListener {

	RelativeLayout layout;
	Context context;
	GridLayout gridLayout;
	int ekranGenisligi;

	// private static final long serialVersionUID = 4002405786699279293L;

	private TextView[][] tile = new TextView[3][3];

	private Node startNode, smallestNode, branchNode;
	private LinkList openList = new LinkList();
	private LinkList closeList = new LinkList();
	private LinkList finalPath = new LinkList();
	private ListNode currentListNode;

	private int[][] tileMatrix = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

	private int[][] goalState = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };

	private Drawable tempIcon;

	private int idCount;
	private int tempInt;

	private Random rand = new Random();

	private int countScramble;

	private Timer timer, timer1;

	private int iEmpty = 2, jEmpty = 2;

	TextView hamle;
	TextView enYuksek;
	int hamleSayisi = 0;
	int enKisaHamle = 0;

	TilesPanel(RelativeLayout gelenLayout, Context gelenContext,
			int ekranGenisligi) {

		hamle = (TextView) ((Activity) gelenContext).findViewById(R.id.hamle);
		enYuksek = (TextView) ((Activity) gelenContext)
				.findViewById(R.id.enYuksek);

		hamleSayisi = 0;
		hamle.setText("Hamle Sayýsý : " + hamleSayisi);

		this.layout = gelenLayout;
		this.context = gelenContext;
		this.ekranGenisligi = ekranGenisligi;

		gridLayout = new GridLayout(context);
		gridLayout.setColumnCount(3);
		gridLayout.setRowCount(3);
		gridLayout.setBackgroundColor(Color.WHITE);
		gridLayout.setY(ekranGenisligi / 3);
		layout.addView(gridLayout);
		int sayac = 1;
		timer = new Timer();
		timer1 = new Timer();

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				tile[i][j] = createJLabel(sayac);
				sayac++;
			}

		addTiles();

		randomize();

		

	}

	final Handler myHandler = new Handler();

	public void ekraniTemizle() {
		layout.removeView(gridLayout);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				layout.removeView(tile[i][j]);

			}
	}

	public void updateGUI() {
		myHandler.post(myRunnable);
	}

	public void durdur() {
		myHandler.removeCallbacks(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			finalPathTimer();
		}

	};

	public void timerStart() {
		timer1.schedule(new TimerTask() {
			@Override
			public void run() {
				updateGUI();
			}
		}, 0, 500);
	}

	final Handler myHandler1 = new Handler();

	public void updateGUI1() {
		myHandler1.post(myRunnable1);
	}

	public void durdur1() {
		myHandler1.removeCallbacks(myRunnable1);
		
	}

	final Runnable myRunnable1 = new Runnable() {
		public void run() {
			karistir();
		}

	};

	public void timerStart1() {
		timer1.schedule(new TimerTask() {
			@Override
			public void run() {
				updateGUI1();
			}
		}, 0, 1);
	}

	public TextView createJLabel(int resimId) {
		TextView newLabel = new TextView(context);
		newLabel.setId(resimId);
		newLabel.setWidth(ekranGenisligi / 3);
		newLabel.setHeight(ekranGenisligi / 3);
		Class resources = R.drawable.class;
		Field[] fields = resources.getFields();

		for (int i = 0; i < fields.length; i++) {

			if (fields[i].getName().equals("resim" + resimId)) {
				try {
					newLabel.setBackgroundResource(context.getResources()
							.getIdentifier(fields[i].getName(), "drawable",
									"com.sekiztas"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		return newLabel;
	}

	public void checkMove(String key) {

		if (key.equals("asagi")) {
			if (iEmpty == 0) {

			} else {
				hamleSayisi++;
				moveEmptyTileUp();
				iEmpty -= 1;
			}

		}

		else if (key.equals("yukari")) {
			if (iEmpty == 2) {

			} else {
				hamleSayisi++;
				moveEmptyTileDown();
				iEmpty += 1;
			}
		}

		else if (key.equals("sol")) {

			if (jEmpty == 2) {

			} else {
				hamleSayisi++;
				moveEmptyTileRight();
				jEmpty += 1;
			}

		} else if (key.equals("sag")) {

			if (jEmpty == 0) {

			} else {
				hamleSayisi++;
				moveEmptyTileLeft();
				jEmpty -= 1;
			}

		}

		hamle.setText("Hamle Sayýsý : " + hamleSayisi);

	}

	private void moveEmptyTileLeft() {
		animasyon.animasyonUygula(layout, context, tile[iEmpty][jEmpty - 1],
				tile[iEmpty][jEmpty], "saga");

		tempInt = tileMatrix[iEmpty][jEmpty];
		tileMatrix[iEmpty][jEmpty] = tileMatrix[iEmpty][jEmpty - 1];
		tileMatrix[iEmpty][jEmpty - 1] = tempInt;

		layout.invalidate();
		gridLayout.invalidate();
	}

	private void moveEmptyTileRight() {
		animasyon.animasyonUygula(layout, context, tile[iEmpty][jEmpty + 1],
				tile[iEmpty][jEmpty], "sola");

		tempInt = tileMatrix[iEmpty][jEmpty];
		tileMatrix[iEmpty][jEmpty] = tileMatrix[iEmpty][jEmpty + 1];
		tileMatrix[iEmpty][jEmpty + 1] = tempInt;

		layout.invalidate();
		gridLayout.invalidate();
	}

	private void moveEmptyTileUp() {
		animasyon.animasyonUygula(layout, context, tile[iEmpty - 1][jEmpty],
				tile[iEmpty][jEmpty], "asagi");

		tempInt = tileMatrix[iEmpty][jEmpty];
		tileMatrix[iEmpty][jEmpty] = tileMatrix[iEmpty - 1][jEmpty];
		tileMatrix[iEmpty - 1][jEmpty] = tempInt;

		layout.invalidate();
		gridLayout.invalidate();
	}

	AnimasyonIslemleri animasyon = new AnimasyonIslemleri();

	private void moveEmptyTileDown() {

		animasyon.animasyonUygula(layout, context, tile[iEmpty + 1][jEmpty],
				tile[iEmpty][jEmpty], "yukari");

		tempInt = tileMatrix[iEmpty][jEmpty];
		tileMatrix[iEmpty][jEmpty] = tileMatrix[iEmpty + 1][jEmpty];
		tileMatrix[iEmpty + 1][jEmpty] = tempInt;

		layout.invalidate();
		gridLayout.invalidate();

	}

	public int enKisaHamle() {
		System.gc();
		openList.setEmptyList();
		finalPath.setEmptyList();
		closeList.setEmptyList();
		idCount = 0;
		startNode = new Node(0, tileMatrix, idCount, -1);

		openList.insertAtBack(startNode);

		while (!openList.isEmpty()) {
			smallestNode = openList.getSmallestNode();
			if (isSameState(smallestNode.getState(), goalState)) {

				closeList.insertAtBack(smallestNode);
				break;
			} else {
				openList.removeNode(smallestNode);
				closeList.insertAtBack(smallestNode);

				if (smallestNode.getEmptyX() == 0
						&& smallestNode.getEmptyY() == 0) {
					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();
					branchNode = moveEmptyDown(smallestNode);

					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 0
						&& smallestNode.getEmptyY() == 1) {
					branchNode = moveEmptyLeft(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyDown(smallestNode);

					checkBranchNodeInLists();

					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 0
						&& smallestNode.getEmptyY() == 2) {
					branchNode = moveEmptyLeft(smallestNode);

					checkBranchNodeInLists();

					branchNode = moveEmptyDown(smallestNode);

					checkBranchNodeInLists();

				}

				else if (smallestNode.getEmptyX() == 1
						&& smallestNode.getEmptyY() == 0) {
					branchNode = moveEmptyUp(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyDown(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 1
						&& smallestNode.getEmptyY() == 1) {
					branchNode = moveEmptyUp(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyDown(smallestNode);

					checkBranchNodeInLists();

					branchNode = moveEmptyLeft(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 1
						&& smallestNode.getEmptyY() == 2) {
					branchNode = moveEmptyUp(smallestNode);

					checkBranchNodeInLists();

					branchNode = moveEmptyLeft(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyDown(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 2
						&& smallestNode.getEmptyY() == 0) {
					branchNode = moveEmptyUp(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 2
						&& smallestNode.getEmptyY() == 1) {
					branchNode = moveEmptyLeft(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyUp(smallestNode);
					checkBranchNodeInLists();

					branchNode = moveEmptyRight(smallestNode);
					checkBranchNodeInLists();
				}

				else if (smallestNode.getEmptyX() == 2
						&& smallestNode.getEmptyY() == 2) {
					branchNode = moveEmptyUp(smallestNode);

					checkBranchNodeInLists();
					branchNode = moveEmptyLeft(smallestNode);
					checkBranchNodeInLists();

				}

			}

		}

		backTrackNodes();
		currentListNode = finalPath.getFirstNode();

		while (currentListNode != null) {

			currentListNode = currentListNode.getNext();
			enKisaHamle++;
		}

		currentListNode = finalPath.getFirstNode();

		return enKisaHamle;
	}

	public void solve8puzzle() {
		//enKisaHamle();
		enKisaHamle = 0;
		enYuksek.setText("Bu oyun için en az hamle sayýsý: " + enKisaHamle()
				+ " hamledir.");
		hamleSayisi = 0 ;
		timerStart();
		resetTilesMatrix();
	}

	private void resetTilesMatrix() {
		iEmpty = jEmpty = 2;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tileMatrix[i][j] = goalState[i][j];
			}
		}
	}

	public void checkBranchNodeInLists() {
		if (closeList.alreadyExists(branchNode))
			;
		else {
			if (!openList.alreadyExists(branchNode)) {
				openList.insertAtBack(branchNode);
			}
		}
	}

	public void backTrackNodes() {

		Node insertNode = closeList.getLastNode().getNode();

		finalPath.insertAtFront(insertNode);

		ListNode current = closeList.getLastNode().previousNode;

		while (current != null) {
			if (insertNode.getParentID() == current.getNode().getID()) {
				insertNode = current.getNode();
				finalPath.insertAtFront(insertNode);
			}

			current = current.previousNode;
		}

	}

	public boolean isSameState(int[][] A, int[][] B) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (A[i][j] != B[i][j])
					return false;
		return true;
	}

	public void addTiles() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				gridLayout.addView(tile[i][j]);
	}

	public Node moveEmptyLeft(Node currentNode) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getState()[i][j];
		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x][y - 1];
		tmpState[x][y - 1] = tmp;

		return new Node(currentNode.getG() + 1, tmpState, ++idCount,
				currentNode.getID());
	}

	public Node moveEmptyRight(Node currentNode) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getState()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x][y + 1];
		tmpState[x][y + 1] = tmp;

		return new Node(currentNode.getG() + 1, tmpState, ++idCount,
				currentNode.getID());
	}

	public Node moveEmptyUp(Node currentNode) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getState()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x - 1][y];
		tmpState[x - 1][y] = tmp;

		return new Node(currentNode.getG() + 1, tmpState, ++idCount,
				currentNode.getID());
	}

	public Node moveEmptyDown(Node currentNode) {
		int[][] tmpState = new int[3][3];
		int i, j;
		for (i = 0; i < 3; i++)
			for (j = 0; j < 3; j++)
				tmpState[i][j] = currentNode.getState()[i][j];

		int x = currentNode.getEmptyX();
		int y = currentNode.getEmptyY();

		int tmp = tmpState[x][y];
		tmpState[x][y] = tmpState[x + 1][y];
		tmpState[x + 1][y] = tmp;

		return new Node(currentNode.getG() + 1, tmpState, ++idCount,
				currentNode.getID());
	}

	/**
	 * Randomize.
	 */
	public void randomize() {

		countScramble = 0;
		timerStart1();
		

	}

	public boolean checkGameFinish() {
		if (tileMatrix[0][0] == 1 && tileMatrix[0][1] == 2
				&& tileMatrix[0][2] == 3 && tileMatrix[1][0] == 4
				&& tileMatrix[1][1] == 5 && tileMatrix[1][2] == 6
				&& tileMatrix[2][0] == 7 && tileMatrix[2][1] == 8
				&& tileMatrix[2][2] == 9) {

			return true;
		}

		return false;
	}

	public void karistir() {

		int randNum;

		countScramble++;

		if (countScramble < 200) {

			if (iEmpty == 0 && jEmpty == 0) {
				randNum = rand.nextInt(2);

				if (randNum == 0) {

					tempInt = tileMatrix[0][0];
					tempIcon = tile[0][0].getBackground();
					tileMatrix[0][0] = tileMatrix[0][1];
					tile[0][0].setBackground(tile[0][1].getBackground());
					tileMatrix[0][1] = tempInt;
					tile[0][1].setBackground(tempIcon);

					jEmpty = 1;
				}

				else {
					tempInt = tileMatrix[0][0];
					tempIcon = tile[0][0].getBackground();
					tileMatrix[0][0] = tileMatrix[1][0];
					tile[0][0].setBackground(tile[1][0].getBackground());
					tileMatrix[1][0] = tempInt;
					tile[1][0].setBackground(tempIcon);
					iEmpty = 1;
				}

			}

			else if (iEmpty == 0 && jEmpty == 1) {
				randNum = rand.nextInt(3);

				if (randNum == 0) {

					tempInt = tileMatrix[0][1];
					tempIcon = tile[0][1].getBackground();
					tileMatrix[0][1] = tileMatrix[0][0];
					tile[0][1].setBackground(tile[0][0].getBackground());
					tileMatrix[0][0] = tempInt;
					tile[0][0].setBackground(tempIcon);

					jEmpty = 0;

				} else if (randNum == 1) {

					tempInt = tileMatrix[0][1];
					tempIcon = tile[0][1].getBackground();
					tileMatrix[0][1] = tileMatrix[0][2];
					tile[0][1].setBackground(tile[0][2].getBackground());
					tileMatrix[0][2] = tempInt;
					tile[0][2].setBackground(tempIcon);

					jEmpty = 2;
				}

				else {
					tempInt = tileMatrix[0][1];
					tempIcon = tile[0][1].getBackground();
					tileMatrix[0][1] = tileMatrix[1][1];
					tile[0][1].setBackground(tile[1][1].getBackground());
					tileMatrix[1][1] = tempInt;
					tile[1][1].setBackground(tempIcon);

					iEmpty = 1;
				}
			}

			else if (iEmpty == 0 && jEmpty == 2) {
				randNum = rand.nextInt(2);

				if (randNum == 0) {

					tempInt = tileMatrix[0][2];
					tempIcon = tile[0][2].getBackground();
					tileMatrix[0][2] = tileMatrix[0][1];
					tile[0][2].setBackground(tile[0][1].getBackground());
					tileMatrix[0][1] = tempInt;
					tile[0][1].setBackground(tempIcon);

					jEmpty = 1;
				}

				else {
					tempInt = tileMatrix[0][2];
					tempIcon = tile[0][2].getBackground();
					tileMatrix[0][2] = tileMatrix[1][2];
					tile[0][2].setBackground(tile[1][2].getBackground());
					tileMatrix[1][2] = tempInt;
					tile[1][2].setBackground(tempIcon);

					iEmpty = 1;
				}

			}

			else if (iEmpty == 1 && jEmpty == 0) {
				randNum = rand.nextInt(3);

				if (randNum == 0) {

					tempInt = tileMatrix[1][0];
					tempIcon = tile[1][0].getBackground();
					tileMatrix[1][0] = tileMatrix[0][0];
					tile[1][0].setBackground(tile[0][0].getBackground());
					tileMatrix[0][0] = tempInt;
					tile[0][0].setBackground(tempIcon);

					iEmpty = 0;
				}

				else if (randNum == 1) {

					tempInt = tileMatrix[1][0];
					tempIcon = tile[1][0].getBackground();
					tileMatrix[1][0] = tileMatrix[1][1];
					tile[1][0].setBackground(tile[1][1].getBackground());
					tileMatrix[1][1] = tempInt;
					tile[1][1].setBackground(tempIcon);

					jEmpty = 1;
				}

				else {

					tempInt = tileMatrix[1][0];
					tempIcon = tile[1][0].getBackground();
					tileMatrix[1][0] = tileMatrix[2][0];
					tile[1][0].setBackground(tile[2][0].getBackground());
					tileMatrix[2][0] = tempInt;
					tile[2][0].setBackground(tempIcon);

					iEmpty = 2;
				}
			}

			else if (iEmpty == 1 && jEmpty == 1) {
				randNum = rand.nextInt(4);

				if (randNum == 0) {

					tempInt = tileMatrix[1][1];
					tempIcon = tile[1][1].getBackground();
					tileMatrix[1][1] = tileMatrix[0][1];
					tile[1][1].setBackground(tile[0][1].getBackground());
					tileMatrix[0][1] = tempInt;
					tile[0][1].setBackground(tempIcon);

					iEmpty = 0;
				}

				else if (randNum == 1) {

					tempInt = tileMatrix[1][1];
					tempIcon = tile[1][1].getBackground();
					tileMatrix[1][1] = tileMatrix[1][0];
					tile[1][1].setBackground(tile[1][0].getBackground());
					tileMatrix[1][0] = tempInt;
					tile[1][0].setBackground(tempIcon);

					jEmpty = 0;
				}

				else if (randNum == 2) {

					tempInt = tileMatrix[1][1];
					tempIcon = tile[1][1].getBackground();
					tileMatrix[1][1] = tileMatrix[1][2];
					tile[1][1].setBackground(tile[1][2].getBackground());
					tileMatrix[1][2] = tempInt;
					tile[1][2].setBackground(tempIcon);

					jEmpty = 2;
				}

				else {

					tempInt = tileMatrix[1][1];
					tempIcon = tile[1][1].getBackground();
					tileMatrix[1][1] = tileMatrix[2][1];
					tile[1][1].setBackground(tile[2][1].getBackground());
					tileMatrix[2][1] = tempInt;
					tile[2][1].setBackground(tempIcon);

					iEmpty = 2;
				}

			}

			else if (iEmpty == 1 && jEmpty == 2) {
				randNum = rand.nextInt(3);

				if (randNum == 0) {

					tempInt = tileMatrix[1][2];
					tempIcon = tile[1][2].getBackground();
					tileMatrix[1][2] = tileMatrix[0][2];
					tile[1][2].setBackground(tile[0][2].getBackground());
					tileMatrix[0][2] = tempInt;
					tile[0][2].setBackground(tempIcon);

					iEmpty = 0;
				}

				else if (randNum == 1) {

					tempInt = tileMatrix[1][2];
					tempIcon = tile[1][2].getBackground();
					tileMatrix[1][2] = tileMatrix[1][1];
					tile[1][2].setBackground(tile[1][1].getBackground());
					tileMatrix[1][1] = tempInt;
					tile[1][1].setBackground(tempIcon);

					jEmpty = 1;
				}

				else {

					tempInt = tileMatrix[1][2];
					tempIcon = tile[1][2].getBackground();
					tileMatrix[1][2] = tileMatrix[2][2];
					tile[1][2].setBackground(tile[2][2].getBackground());
					tileMatrix[2][2] = tempInt;
					tile[2][2].setBackground(tempIcon);

					iEmpty = 2;
				}
			}

			else if (iEmpty == 2 && jEmpty == 0) {
				randNum = rand.nextInt(2);

				if (randNum == 0) {

					tempInt = tileMatrix[2][0];
					tempIcon = tile[2][0].getBackground();
					tileMatrix[2][0] = tileMatrix[1][0];
					tile[2][0].setBackground(tile[1][0].getBackground());
					tileMatrix[1][0] = tempInt;
					tile[1][0].setBackground(tempIcon);

					iEmpty = 1;
				}

				else {

					tempInt = tileMatrix[2][0];
					tempIcon = tile[2][0].getBackground();
					tileMatrix[2][0] = tileMatrix[2][1];
					tile[2][0].setBackground(tile[2][1].getBackground());
					tileMatrix[2][1] = tempInt;
					tile[2][1].setBackground(tempIcon);

					jEmpty = 1;
				}

			}

			else if (iEmpty == 2 && jEmpty == 1) {
				randNum = rand.nextInt(3);

				if (randNum == 0) {

					tempInt = tileMatrix[2][1];
					tempIcon = tile[2][1].getBackground();
					tileMatrix[2][1] = tileMatrix[1][1];
					tile[2][1].setBackground(tile[1][1].getBackground());
					tileMatrix[1][1] = tempInt;
					tile[1][1].setBackground(tempIcon);

					iEmpty = 1;

				} else if (randNum == 1) {

					tempInt = tileMatrix[2][1];
					tempIcon = tile[2][1].getBackground();
					tileMatrix[2][1] = tileMatrix[2][0];
					tile[2][1].setBackground(tile[2][0].getBackground());
					tileMatrix[2][0] = tempInt;
					tile[2][0].setBackground(tempIcon);

					jEmpty = 0;
				}

				else {

					tempInt = tileMatrix[2][1];
					tempIcon = tile[2][1].getBackground();
					tileMatrix[2][1] = tileMatrix[2][2];
					tile[2][1].setBackground(tile[2][2].getBackground());
					tileMatrix[2][2] = tempInt;
					tile[2][2].setBackground(tempIcon);

					jEmpty = 2;
				}
			}

			else if (iEmpty == 2 && jEmpty == 2) {
				randNum = rand.nextInt(2);

				if (randNum == 0) {

					tempInt = tileMatrix[2][2];
					tempIcon = tile[2][2].getBackground();
					tileMatrix[2][2] = tileMatrix[1][2];
					tile[2][2].setBackground(tile[1][2].getBackground());
					tileMatrix[1][2] = tempInt;
					tile[1][2].setBackground(tempIcon);

					iEmpty = 1;
				}

				else {

					tempInt = tileMatrix[2][2];
					tempIcon = tile[2][2].getBackground();
					tileMatrix[2][2] = tileMatrix[2][1];
					tile[2][2].setBackground(tile[2][1].getBackground());
					tileMatrix[2][1] = tempInt;
					tile[2][1].setBackground(tempIcon);

					jEmpty = 1;
				}
			}

		} else {
			durdur1();
			if (sayac == 0) {
				enYuksek.setText("Bu oyun için en az hamle sayýsý: "
						+ (enKisaHamle())+ " hamledir.");
				sayac++;
			}

		}
		
	}

	int sayac = 0;

	public int resimGetir(int resimId) {

		Class resources = R.drawable.class;
		Field[] fields = resources.getFields();

		for (int i = 0; i < fields.length; i++) {

			if (fields[i].getName().equals("resim" + resimId)) {
				try {
					return context.getResources().getIdentifier(
							fields[i].getName(), "drawable", "com.sekiztas");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return -1;
	}

	public void finalPathTimer() {

		if (currentListNode != null) {

			hamleSayisi++;
			hamle.setText("Hamle Sayýsý : " + hamleSayisi);

			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					tile[i][j].setBackgroundResource(resimGetir(currentListNode
							.getNode().getState()[i][j]));
				}
			currentListNode = currentListNode.getNext();

		} else
			durdur();
	}

	@Override
	public void onFailure(int reason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub

	}

}
