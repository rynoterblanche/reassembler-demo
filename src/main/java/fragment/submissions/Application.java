package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
			in.lines().map(Reassembler::reassemble)
				.forEach(System.out::println);
		}
	}

	static class Reassembler {
		
		static class FragmentPair {
			final String first;
			final String second;
			
			public FragmentPair(String first, String second) {
				this.first = first;
				this.second = second;
			}
		}
		
		static String reassemble(String fragments) {
			final String delimiter = ";";
			LinkedList<String> fragmentList = Arrays.asList(fragments.split(delimiter)).stream()
					.collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
			
			FragmentPair pair = getMaxOverlapping(fragmentList);
			Optional<String> merged = Merger.mergeOverlapped(pair.first, pair.second);
			if (merged.isPresent()) {
				fragmentList.push(merged.get());
				fragmentList.remove(pair.first);
				fragmentList.remove(pair.second);
			}
			
			String result = fragmentList.stream().collect(Collectors.joining(delimiter));
			return result.contains(delimiter) ? reassemble(result) : result;
		}
		
		static FragmentPair getMaxOverlapping(LinkedList<String> fragmentList) {
			int maxIntersectionSize = 0;
			int fragment1 = 0, fragment2 = 0;
			for (int i = 0; i < fragmentList.size(); i++) {
				for (int j = i + 1; j < fragmentList.size(); j++) {
					int tmpSize = getOverlap(fragmentList.get(i), fragmentList.get(j));
					if (tmpSize >= 2 && tmpSize > maxIntersectionSize) {
						maxIntersectionSize = tmpSize;
						fragment1 = i;
						fragment2 = j;
					}
				}
			}
			return new FragmentPair(fragmentList.get(fragment1), fragmentList.get(fragment2));
		}
		
		static int getOverlap(String s1, String s2) {
			int overlap = calcOverlap(s1, s2);
			return overlap > 0 ? overlap : calcOverlap(s2, s1);
		}
		
		static int calcOverlap(String s1, String s2)  {
			if (s1.contains(s2))
				return s2.length();
			
			char[] c1 = s1.toCharArray();
			char[] c2 = s2.toCharArray();
			int overlap = 0;
			for (int i = 0; i < c1.length; i++) {
				overlap = 0;
				for (int j = 0; j < c2.length && i < c1.length; j++) {
					if (c1[i] != c2[j]) {
						break;
					}
					i++; overlap++;
				}
			}
			return overlap;
		}
		
		static class Merger {
			
			static Optional<String> mergeOverlapped(String s1, String s2) {
				Optional<String> merged = merge(s1, s2);
				return merged.isPresent() ? merged : merge(s2, s1);
			}
			
			static Optional<String> merge(String s1, String s2) {
				return (s1.contains(s2)) ? Optional.of(s1) : findAndMergeSequence(s1, s2);
			}
			
			static Optional<String> findAndMergeSequence(String s1, String s2) {
				char[] first = s1.toCharArray();
				char[] second = s2.toCharArray();
				int intersection = 0;
				while (!seqCharsMatch(first, second, intersection)) {
					intersection++;
				}
				if (intersection > 0 && intersection < first.length) {
					String merged = String.copyValueOf(first, 0, intersection).concat(String.valueOf(second));
					return Optional.of(merged);
				}
				return Optional.empty();
			}
			
			static boolean seqCharsMatch(char[] fSeq, char[] sSeq, int index) {
				return IntStream.range(index, fSeq.length)
						.allMatch(i -> fSeq[i] == sSeq[i - index]);
			}
		}
	}
		
}
