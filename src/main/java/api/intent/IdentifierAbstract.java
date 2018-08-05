/*
 * Copyright ${2018} [Adib Rastegarnia, Rajas H.Karandikar, Douglas Comer]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package api.intent;

import java.util.List;

public abstract class IdentifierAbstract {

    protected int findFirstMissing(List<Integer> array, int start, int end) {
        if (start > end) {
            return end + 1;
        }

        if (start != array.get(start)) {
            return start;
        }

        int mid = (start + end) / 2;

        // Left half has all elements from 0 to mid
        if (array.get(mid) == mid) {
            return findFirstMissing(array, mid + 1, end);
        }

        return findFirstMissing(array, start, mid);
    }
}
