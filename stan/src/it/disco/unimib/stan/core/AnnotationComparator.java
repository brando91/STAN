package it.disco.unimib.stan.core;

import java.util.Comparator;

public class AnnotationComparator implements Comparator<SemanticAnnotation> {

	@Override
	public int compare(SemanticAnnotation first, SemanticAnnotation second) {
		if(first.getScore() > second.getScore()) {
			return -1;
		}
		else if(first.getScore() == second.getScore()) {
			return 0;
		}
		else return 1;
	}

}
