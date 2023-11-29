package com.inzynierka2k24.external.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.OnlyIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.inzynierka2k24.external", importOptions = OnlyIncludeTests.class)
public class TestsRulesTest {

  @ArchTest
  static final ArchRule no_junit4 =
      noClasses()
          .should().dependOnClassesThat().resideInAnyPackage("org.junit", "org.junit.*")
          .because("we use junit5");

  @ArchTest
  static final ArchRule no_hamcrest_assertThat =
      noClasses()
          .should().dependOnClassesThat().haveFullyQualifiedName("org.hamcrest.MatcherAssert")
          .because("we use AssertJ implementation of assertThat()");
}
