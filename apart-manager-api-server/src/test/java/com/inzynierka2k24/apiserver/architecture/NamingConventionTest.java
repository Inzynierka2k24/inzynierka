package com.inzynierka2k24.apiserver.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.inzynierka2k24.apiserver", importOptions = DoNotIncludeTests.class)
public class NamingConventionTest {

  @ArchTest
  static final ArchRule services_should_be_suffixed =
      classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .haveSimpleNameEndingWith("Service")
          .because("we don't want developers to go creative with names");

  @ArchTest
  static final ArchRule controllers_should_be_suffixed =
      classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .or()
          .areAnnotatedWith(Controller.class)
          .should()
          .haveSimpleNameEndingWith("Controller")
          .because("we don't want developers to go creative with names");

  @ArchTest
  static final ArchRule configs_should_be_suffixed =
      classes()
          .that()
          .areAnnotatedWith(Configuration.class)
          .should()
          .haveSimpleNameEndingWith("Config")
          .because("we don't want developers to go creative with names");

  @ArchTest
  static final ArchRule repositories_should_be_suffixed =
      classes()
          .that()
          .areAnnotatedWith(Repository.class)
          .should()
          .haveSimpleNameEndingWith("Dao")
          .because("that's the convention we agreed on");

  @ArchTest
  static final ArchRule controllers_should_be_in_controller_package =
      classes()
          .that()
          .haveSimpleNameContaining("Controller")
          .should()
          .resideInAPackage("..controller..");

  @ArchTest
  static final ArchRule services_should_be_in_service_package =
      classes().that().haveSimpleNameEndingWith("Service").should().resideInAPackage("..service..");

  @ArchTest
  static final ArchRule configs_should_be_in_config_package =
      classes().that().haveSimpleNameContaining("Config").should().resideInAPackage("..config..");

  @ArchTest
  static final ArchRule repositories_should_be_in_dao_package =
      classes().that().haveSimpleNameContaining("Dao").should().resideInAPackage("..dao..");
}
