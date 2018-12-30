/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Output;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.util.CollectionWriter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public abstract class CriteriaGroup implements Criteria {

    private final Collection<Criteria> criterias;

    public CriteriaGroup(Collection<Criteria> criterias) {
        this.criterias = criterias;
    }

    public CriteriaGroup(Criteria... criterias) {
        this.criterias = Arrays.asList(criterias);
    }

    protected abstract String getOperator();

    @Override
    public void write(Output output) {
        if (criterias.isEmpty()) {
            output.write("1 = 1");
            return;
        }
        CollectionWriter.writeCollection(output, criterias, getOperator(), true, true);
    }

    @Override
    public void collectTableReferences(Set<TableReference> tableReferences) {
        for (Criteria criteria : criterias) {
            criteria.collectTableReferences(tableReferences);
        }
    }
}
