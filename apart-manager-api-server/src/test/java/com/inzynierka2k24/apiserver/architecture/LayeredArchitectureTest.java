package com.inzynierka2k24.apiserver.architecture;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.inzynierka2k24.apiserver", importOptions = DoNotIncludeTests.class)
public class LayeredArchitectureTest {

  @ArchTest
  static final ArchRule layer_dependencies_are_respected = layeredArchitecture().consideringAllDependencies()

      .layer("Controllers").definedBy("com.inzynierka2k24.apiserver.web.controller..")
      .layer("Services").definedBy("com.inzynierka2k24.apiserver.service..")
      .layer("Persistence").definedBy("com.inzynierka2k24.apiserver.dao..")

      .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
      .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
      .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");
}
