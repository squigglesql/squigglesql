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
package io.zatarox.squiggle;

import io.zatarox.squiggle.output.Output;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ParameterTest {

    private Parameter parameter;

    @Before
    public void setUp() {
        parameter = new Parameter();
    }

    @Test
    public void write() {
        parameter.write(new Output("") {
            @Override
            public Output print(Object o) {
                assertEquals("?", o);
                return this;
            }
        });
    }
    
    @Test
    public void addReferencedTablesTo() {
        parameter.addReferencedTablesTo(new HashSet<Table>());
    }
}
