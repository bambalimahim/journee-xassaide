package touba.xassaide;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("touba.xassaide");

        noClasses()
            .that()
            .resideInAnyPackage("touba.xassaide.service..")
            .or()
            .resideInAnyPackage("touba.xassaide.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..touba.xassaide.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
