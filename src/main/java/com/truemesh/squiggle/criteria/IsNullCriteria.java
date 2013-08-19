package com.truemesh.squiggle.criteria;

import com.truemesh.squiggle.Criteria;
import com.truemesh.squiggle.Matchable;
import com.truemesh.squiggle.Table;
import com.truemesh.squiggle.output.Output;

import java.util.Set;

public class IsNullCriteria implements Criteria {
	private final Matchable matched;
	
	public IsNullCriteria(Matchable matched) {
		this.matched = matched;
	}

	@Override
	public void write(Output out) {
		matched.write(out);
		out.print(" IS NULL");
	}

	public void addReferencedTablesTo(Set<Table> tables) {
		matched.addReferencedTablesTo(tables);
	}
}
