/*
 * Copyright 2004-2015 Joe Walnes, Guillaume Chauvet.
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
package com.zatarox.squiggle.criteria;

import com.zatarox.squiggle.Criteria;

/**
 * Class NOT is a {@link BaseLogicGroup} extension that generates the SQL syntax
 * for the negation operator in a condition in an SQL Where clause.
 */
public class NOT extends BaseLogicGroup {

    /**
     * Initializes an SQL NOT operator with the given criteria that appears to
     * the right of the operator.
     *
     * @param right the criteria or operand to which the NOT operator applies.
     */
    public NOT(Criteria right) {
        super("NOT", new NoCriteria(), right);
    }
}
