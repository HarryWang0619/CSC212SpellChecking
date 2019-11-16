package edu.smith.cs.csc212.speller;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a Character Trie that stores Strings!
 * 
 * @author jfoley
 *
 */
public class CharTrie extends AbstractSet<String> {
	/**
	 * The nodes don't actually store characters in them - the links do.
	 */
	Node root = new Node();
	/**
	 * This gets updated to account for the size.
	 */
	int size = 0;

	/**
	 * Insert a word into this CharTrie; one letter at a time.
	 * 
	 * @param word - the string to insert.
	 */
	public void insert(String word) {
		LinkedList<Character> chars = new LinkedList<>();
		for (char c : word.toCharArray()) {
			chars.add(c);
		}
		root.insert(chars);
		size++;
	}

	/**
	 * Contains is a default Java Method so it doesn't warrant its own comment.
	 */
	public boolean contains(Object o) {
		String word = (String) o;
		LinkedList<Character> chars = new LinkedList<>();
		for (char c : word.toCharArray()) {
			chars.add(c);
		}
		return root.find(chars);
	}

	/**
	 * Every node in a Trie may have many links (to future letters) AND it may be
	 * the "terminal" state of a word.
	 */
	private static class Node {
		/** a..z and dash/hyphen/- */
		private static int NUM_LINKS = 26 + 1;
		/**
		 * Did a word end at this node?
		 */
		boolean isEndOfWord;
		/**
		 * This is an array of links.
		 */
		List<Node> links;

		/**
		 * Construct a new node (with space for 27 links!).
		 */
		public Node() {
			this.isEndOfWord = false;
			this.links = new ArrayList<>(NUM_LINKS);
			for (int i = 0; i < NUM_LINKS; i++) {
				links.add(null);
			}
		}

		/**
		 * This maps a character to it's index in our array of links.
		 * 
		 * @param c (a letter, a-z or a hyphen.)
		 * @return 0-25 for a-z and - for 26
		 */
		public int getLinkIndex(char c) {
			if (c == '-') {
				return NUM_LINKS - 1;
			} else if (c >= 'A' && c <= 'Z') {
				return c - 'A';
			} else if (c>= 'a' && c <= 'z') {
				return c - 'a';
			} else {
				// not a valid character
				return -1;
			}
		}

		/**
		 * Insert a word in this trie (or a suffix of a word, because recursion).
		 * 
		 * @param chars - a list of characters that used to be a word.
		 */
		public void insert(LinkedList<Character> chars) {
			if (chars.isEmpty()) {
				this.isEndOfWord = true;
			} else {
				char c = chars.pollFirst();
				int link = getLinkIndex(c);
				if (link == -1) {
					throw new RuntimeException("Bad Character: " + c);
				}
				if (links.get(link) == null) {
					links.set(link, new Node());
				}
				links.get(link).insert(chars);
			}
		}

		/**
		 * Find a word in this trie (or a suffix of a word, because recursion).
		 * 
		 * @param chars - a list of characters that used to be a word.
		 * @return true if this trie contains that word.
		 */
		public boolean find(LinkedList<Character> chars) {
			if (chars.isEmpty()) {
				return this.isEndOfWord;
			} else {
				int link = getLinkIndex(chars.pollFirst());
				if (link == -1) {
					return false;
				}
				// no more children!
				if (links.get(link) == null) {
					return false;
				}
				// recurse!
				return links.get(link).find(chars);
			}
		}

		/**
		 * Incomplete method to compute how many nodes are in this Trie. Recursive.
		 * 
		 * @return the count of nodes that exist in the Trie, starting from here.
		 */
		public int countNodes() {
			int count = 1;
			for (int i = 0; i < links.size(); i++) {
				if (links.get(i) != null ) {
					count ++;
					count += links.get(i).countNodes();
				}
			}
			// loop over links
			// if they're not null
			// count them, too
			return count;
		}
	}

	/**
	 * How do you count the nodes in this Trie? Recursion!
	 * 
	 * @return the number of nodes in the trie.
	 */
	public int countNodes() {
		return root.countNodes();
	}

	/**
	 * We would need to create an object that kept the recursion state around. We
	 * will talk about depth-first search (part of the solution to this) later in
	 * the course.
	 */
	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("Trie Traversal is really hard.");
	}

	/**
	 * Just keeping track of the size is cheap. We could also count terminal
	 * nodes...
	 */
	@Override
	public int size() {
		return size;
	}

}
