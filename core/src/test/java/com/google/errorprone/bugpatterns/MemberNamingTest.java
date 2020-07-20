/*
 * Copyright 2020 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link MemberNaming}. */
@RunWith(JUnit4.class)
public class MemberNamingTest {
  private final CompilationTestHelper helper =
      CompilationTestHelper.newInstance(MemberNaming.class, getClass());
  private final BugCheckerRefactoringTestHelper refactoringHelper =
      BugCheckerRefactoringTestHelper.newInstance(MemberNaming.class, getClass());

  @Test
  public void nameWithUnderscores() {
    refactoringHelper
        .addInputLines(
            "Test.java",
            "class Test {",
            "  private int foo_bar;",
            "  int get() {",
            "    return foo_bar;",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "class Test {",
            "  private int fooBar;",
            "  int get() {",
            "    return fooBar;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void localVariable_renamed() {
    refactoringHelper
        .addInputLines(
            "Test.java",
            "class Test {",
            "  int get() {",
            "    int foo_bar = 1;",
            "    return foo_bar;",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "class Test {",
            "  int get() {",
            "    int fooBar = 1;",
            "    return fooBar;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void nameWithUnderscores_public_notRenamed() {
    refactoringHelper
        .addInputLines(
            "Test.java",
            "class Test {",
            "  public int foo_bar;",
            "  int get() {",
            "    return foo_bar;",
            "  }",
            "}")
        .expectUnchanged()
        .doTest();
  }

  @Test
  public void nameWithLeadingUppercase() {
    helper
        .addSourceLines(
            "Test.java",
            "class Test {",
            "  // BUG: Diagnostic contains:",
            "  private int Foo;",
            "  int get() {",
            "    return Foo;",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void upperCamelCaseAndNotStatic_noFinding() {
    helper
        .addSourceLines(
            "Test.java", //
            "class Test {",
            "  private int FOO;",
            "}")
        .doTest();
  }
}
