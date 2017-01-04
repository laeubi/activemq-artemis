/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.config;

import java.io.Serializable;

public class WildcardConfiguration implements Serializable {

   static final char SINGLE_WORD = '*';

   static final char ANY_WORDS = '#';

   static final char DELIMITER = '.';

   boolean enabled = true;

   char singleWord = SINGLE_WORD;

   char anyWords = ANY_WORDS;

   char delimiter = DELIMITER;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof WildcardConfiguration)) return false;

      WildcardConfiguration that = (WildcardConfiguration) o;

      if (enabled != that.enabled) return false;
      if (singleWord != that.singleWord) return false;
      if (anyWords != that.anyWords) return false;
      return delimiter == that.delimiter;

   }

   @Override
   public int hashCode() {
      int result = (enabled ? 1 : 0);
      result = 31 * result + (int) singleWord;
      result = 31 * result + (int) anyWords;
      result = 31 * result + (int) delimiter;
      return result;
   }

   public boolean isEnabled() {

      return enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public char getAnyWords() {
      return anyWords;
   }

   public void setAnyWords(char anyWords) {
      this.anyWords = anyWords;
   }

   public char getDelimiter() {
      return delimiter;
   }

   public void setDelimiter(char delimiter) {
      this.delimiter = delimiter;
   }

   public char getSingleWord() {
      return singleWord;
   }

   public void setSingleWord(char singleWord) {
      this.singleWord = singleWord;
   }

}
