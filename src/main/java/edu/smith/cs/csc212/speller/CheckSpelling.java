package edu.smith.cs.csc212.speller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class contains experimentation code.
 * @author jfoley
 *
 */
public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		// Read all lines from a file:
		// This file has one word per line.
		List<String> words = WordSplitter.readUTF8File("src/main/resources/words")
				.lines()
				.collect(Collectors.toList());
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println("  "+dictionary.getClass().getSimpleName()+": Lookup of items found="+fractionFound+" time="+nsPerItem+" ns/item");
	}
	
	/**
	 * This is **an** entry point of this assignment.
	 * @param args - unused command-line arguments.
	 */

	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		// --- Create a bunch of data structures for testing:
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		//     This feels rather silly, but we're outputting timing information!
		timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);
		
		
        
        

        long startTreeTime1 = System.nanoTime();
		long endTreeTime1 = System.nanoTime();
		System.out.println("TreeSet normal: "+ (endTreeTime1 - startTreeTime1)/1e9 + " seconds");
		
		long startTreeTime2 = System.nanoTime();
		TreeSet<String> tree = new TreeSet<>();
		for(String w : listOfWords) {
			tree.add(w);
		}
		long endTreeTime2 = System.nanoTime();
		System.out.println("TreeSet calling add: "+(endTreeTime2 - startTreeTime2)/1e9+" seconds");
		
		long startHashTime1 = System.nanoTime();
		long endHashTime1 = System.nanoTime();
		System.out.println("HashSet normal: "+ (endHashTime1 - startHashTime1)/1e9 + " seconds");
		
		long startHashTime2 = System.nanoTime();
		HashSet<String> hash = new HashSet<>();
		for(String w : listOfWords)
			hash.add(w);
		long endHashTime2 = System.nanoTime();
		System.out.println("HashSet calling add: "+(endHashTime2 - startHashTime2)/1e9 + " seconds" );
		
		long startSSLSTime = System.nanoTime();
		long endSSLSTime = System.nanoTime();
		System.out.println("SortedStringListSet: "+ (endSSLSTime - startSSLSTime)/1e9 + " seconds");
		
		long startCharTime = System.nanoTime();
		long endCharTime = System.nanoTime();
		System.out.println("CharTrie: "+ (endCharTime - startCharTime)/1e9 + " seconds");
		
		long startLHTime = System.nanoTime();
		long endLHTime = System.nanoTime();
		System.out.println("LLHash: "+ (endLHTime - startLHTime)/1e9 + " seconds");
		
		
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		System.out.println("Done!");
	}
}
