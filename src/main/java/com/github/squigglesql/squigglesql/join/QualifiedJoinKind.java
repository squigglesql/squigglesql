/*
 * Copyright 2019-2020 Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.join;

/**
 * All qualified join kinds. Qualified is a join kind which can be supplied with an ON expression. CROSS JOIN is not
 * a qualified join kind, because it doesn't have an ON expression, so it is represented by a separate Join subclass.
 * <p>
 * MySQL and H2 don't support FULL JOIN.
 *
 * @since 4.1.0
 */
public enum QualifiedJoinKind {

    INNER,
    LEFT,
    RIGHT,
    FULL
}
