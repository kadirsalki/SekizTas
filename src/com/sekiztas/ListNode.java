package com.sekiztas;

public class ListNode {

	private Node node;

	public ListNode nextNode;

	public ListNode previousNode;

	public ListNode() {
		node = new Node();
	}

	public ListNode(Node node) {
		this(node, null, null);
	}

	public ListNode(Node node, ListNode previous, ListNode next) {
		this.node = node;

		previousNode = previous;
		nextNode = next;
	}

	public Node getNode() {
		return node;
	}

	public ListNode getNext() {
		return nextNode;
	}

	public ListNode getPrevious() {
		return previousNode;
	}
}