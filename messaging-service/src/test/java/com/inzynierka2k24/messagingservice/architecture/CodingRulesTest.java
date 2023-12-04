package com.inzynierka2k24.messagingservice.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.beans.factory.annotation.Autowired;

@AnalyzeClasses(
    packages = "com.inzynierka2k24.messagingservice",
    importOptions = DoNotIncludeTests.class)
public class CodingRulesTest {

  @ArchTest static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

  @ArchTest static final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

  @ArchTest
  static final ArchRule no_access_to_standard_streams =
      NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.because(
          "don't forget to remove your debugging prints");

  @ArchTest
  static final ArchRule no_field_injection =
      noFields()
          .should()
          .beAnnotatedWith(Autowired.class)
          .because(
              "field injection is considered harmful; use constructor injection or setter injection instead; see https://stackoverflow.com/q/39890849 for detailed explanations");
}
