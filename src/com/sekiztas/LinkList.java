package com.sekiztas;

public class LinkList {

	private ListNode firstNode;

	private ListNode lastNode;

	public LinkList() {
		firstNode = lastNode = null;
	}

	public void insertAtBack(Node node) {
		if (isEmpty())
			firstNode = lastNode = new ListNode(node);
		else {
			ListNode newNode = new ListNode(node);
			lastNode.nextNode = newNode;
			newNode.previousNode = lastNode;
			lastNode = newNode;
		}
	}

	public void insertAtFront(Node node) {
		if (isEmpty())
			firstNode = lastNode = new ListNode(node);
		else {
			ListNode newNode = new ListNode(node);
			newNode.nextNode = firstNode;
			firstNode.previousNode = newNode;
			firstNode = newNode;
		}
	}

	public Node getSmallestNode() {

		Node minimumNode = firstNode.getNode();
		ListNode current = firstNode.getNext();

		while (current != null) {
			if (current.getNode().getF() < minimumNode.getF())
				minimumNode = current.getNode();

			current = current.getNext();
		}

		return minimumNode;
	}

	public void removeNode(Node node) {
		if (isEmpty()) //
			return;
		else if (isSameState(node, firstNode.getNode())
				&& firstNode == lastNode) {
			firstNode = lastNode = null;
			return;
		}

		else if (isSameState(node, firstNode.getNode())) {
			firstNode = firstNode.getNext();
			firstNode.previousNode = null;
			return;
		} else if (isSameState(node, lastNode.getNode())) {
			lastNode = lastNode.getPrevious();
			lastNode.nextNode = null;

		} else {
			ListNode current = firstNode;

			while (current.getNext() != null) {
				if (isSameState(current.getNext().getNode(), node)) {

					current.nextNode.nextNode.previousNode = current;

					current.nextNode = current.getNext().getNext();
					return;
				} else
					current = current.nextNode;
			}
		}
	}

	public boolean isEmpty() {
		return firstNode == null;
	}

	public boolean alreadyExists(Node node) {
		if (!isEmpty()) {
			ListNode current = firstNode;

			while (current != null) {
				if (isSameState(current.getNode(), node))
					return true;
				else
					current = current.getNext();
			}
			return false;
		}
		return false;
	}

	public boolean isSameState(Node A, Node B) {
		int[][] stateA = A.getState();
		int[][] stateB = B.getState();

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (stateA[i][j] != stateB[i][j])
					return false;
		return true;
	}

	public ListNode getLastNode() {
		return lastNode;
	}

	public ListNode getFirstNode() {
		return firstNode;
	}

	public void setEmptyList() {
		firstNode = lastNode = null;
	}

}
