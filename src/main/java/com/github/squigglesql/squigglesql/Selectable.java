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
package com.github.squigglesql.squigglesql;

/**
 * Something that can be returned from a select query.
 * Basically, this is any matchable element except a parameter.
 * Unfortunately, this is just a shallow type protection, not a deep one.
 * For example, you can build the following invalid SQL request at the moment:
 * SELECT digest(?, 'sha256');
 * Because FunctionCall is a Selectable item accepting any Matchable item as input.
 * TODO: Find a way to implement deep protection.
 */
public interface Selectable extends Matchable {
}
