package com.inzynierka2k24.messagingservice.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.Configuration;

@AnalyzeClasses(packages = "com.inzynierka2k24.messagingservice", importOptions = DoNotIncludeTests.class)
public class NamingConventionTest {

  @ArchTest
  static final ArchRule configs_should_be_suffixed =
      classes()
          .that().areAnnotatedWith(Configuration.class)
          .should().haveSimpleNameEndingWith("Config")
          .because("we don't want developers to go creative with names");

  @ArchTest
  static final ArchRule services_should_be_in_service_package =
      classes()
          .that().haveSimpleNameEndingWith("Service")
          .should().resideInAPackage("..service..");

  @ArchTest
  static final ArchRule configs_should_be_in_config_package =
      classes()
          .that().haveSimpleNameContaining("Config")
          .should().resideInAPackage("..config..");
}
